package com.waterloo.buddyalarm;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Andersen on 21/09/13.
 */
public class AlarmActivity extends Activity {
    private Window w;
    private MediaPlayer mp;
    RingtoneManager rtm;
    Button stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.alarm_page);

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

        mp.setVolume(0.5f, 0.5f);
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
}