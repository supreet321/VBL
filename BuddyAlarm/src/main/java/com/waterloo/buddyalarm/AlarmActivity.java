package com.waterloo.buddyalarm;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Andersen on 21/09/13.
 */
public class AlarmActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {
    private Window w;
    private MediaPlayer mp;
    private RingtoneManager rtm;
    private Button stopBtn;
    private NfcAdapter mNfcAdapter;
    private TextView mInfoText;
    private int alarmId;
    private static final int MESSAGE_SENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.alarm_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alarmId = extras.getInt("alarmId");
        }

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
        } else {
            String s = getPasscodeFromDB(alarmId);
            NdefMessage newMsg = new NdefMessage(NdefRecord.createMime(
                    "application/com.waterloo.buddyalarm", s.getBytes())
                    ,NdefRecord.createApplicationRecord("com.waterloo.buddyalarm")
            );

            // Register callback to set NDEF message
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }

        stopBtn = (Button) findViewById(R.id.btn_stopAlarm);
        stopBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    {
                        startAlarm();
                        return true;
                    }

                    case MotionEvent.ACTION_DOWN:
                    {
                        stopAlarm();
                        return true;
                    }

                    default:
                        return false;
                }
            }
        });

        w = this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        w.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        startAlarm();
    }

    private void startAlarm() {
        rtm = new RingtoneManager(this);
        mp = new MediaPlayer();
        String str = rtm.getValidRingtoneUri(this).toString();

        mp.setVolume(0.1f, 0.1f);
        try {
            mp.setDataSource(this, Uri.parse(str));
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            mp.setLooping(true);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            Log.e("Media player", e.toString());
        }
    }

    private void stopAlarm() {
        mp.stop();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Time time = new Time();
        time.setToNow();

        Log.d("keyword", "createNdefMessage");

        String text = getPasscodeFromDB(alarmId);

        NdefMessage msg = new NdefMessage(NdefRecord.createMime(
                "application/com.example.android.beam", text.getBytes())
                /**
                 * The Android Application Record (AAR) is commented out. When a device
                 * receives a push with an AAR in it, the application specified in the AAR
                 * is guaranteed to run. The AAR overrides the tag dispatch system.
                 * You can add it back in to guarantee that this
                 * activity starts when receiving a beamed message. For now, this code
                 * uses the tag dispatch system.
                 */
                ,NdefRecord.createApplicationRecord("com.example.android.beam")
        );
        return msg;
    }

    /**
     * Implementation for the OnNdefPushCompleteCallback interface
     */
    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String msgText = new String(msg.getRecords()[0].getPayload());

        if (msgText.equals(getPasscodeFromDB(alarmId))) {
            mInfoText.setText(msgText);
        } else {
            mInfoText.setText("Keywords do not match");
        }
    }

    public String getPasscodeFromDB(int alarmId) {
        String code;

        Database db = new Database(this);
        db.open();
        code = db.getPasscode(alarmId);
        db.close();

        return code;
    }
}