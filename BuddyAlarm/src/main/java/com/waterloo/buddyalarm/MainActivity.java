package com.waterloo.buddyalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    Activity mActivity;

    ActionBar m_ActionBar;
    Button m_AddAlarmButton, m_EnableDisableAlarmSwitch;
    ListView m_AlarmsListView;

    AlarmListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        m_ActionBar = getActionBar();
        m_ActionBar.setTitle("Alarms");

        m_AddAlarmButton = (Button) findViewById(R.id.btn_addAlarm);
        m_AlarmsListView = (ListView) findViewById(R.id.lv_alarmList);

        m_AddAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, SettingsActivity.class);
                Intent currentIntent = new Intent(mActivity, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        ArrayList<String> listOfAlarmsFromDatabase = new ArrayList<String>();
        populateListView();
    }

    private void populateListView() {
        ArrayList<String> listOfAlarmsFromDatabase = new ArrayList<String>();

        Database db = new Database(mActivity);
        db.open();
        listOfAlarmsFromDatabase = db.getAlarmNames();
        db.close();

        adapter = new AlarmListAdapter(mActivity, listOfAlarmsFromDatabase);
        m_AlarmsListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class AboutDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.alert_About)
                    .setPositiveButton(R.string.alert_OptionsOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    private void showAboutDialog() {
        DialogFragment newFragment = new AboutDialog();
        newFragment.show(getFragmentManager(), "test");
    }

    class AlarmListAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> values;
        AlarmListAdapter adapter;
        AlarmListAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.row_alarm_list, values);
            this.context = context;
            this.values = values;
            adapter = this;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = LayoutInflater.from(context).inflate(R.layout.row_alarm_list, null);

             final TextView textView = (TextView) rowView.findViewById(R.id.tv_lv_alarm_row);
             Switch switchView = (Switch) rowView.findViewById(R.id.switch_lv_row);


            //ImageView imageView = (ImageView) rowView.findViewById(R.id.btn_lv_delete);
              textView.setText(values.get(position));

            ArrayList<String> listOfAlarmsFromDatabase = new ArrayList<String>();
            Database db = new Database(mActivity);
            db.open();
            listOfAlarmsFromDatabase = db.getAlarmNames();
            for(int i = 0; i<listOfAlarmsFromDatabase.size(); i++){
            if(listOfAlarmsFromDatabase.contains(String.valueOf(textView.getText()))){
                ArrayList<String> listAlarmDetails =  db.getAlarmDetails(String.valueOf(textView.getText()));
                if(listAlarmDetails.get(5).equals("true"))
                switchView.setChecked(true);
                else
                switchView.setChecked(false);
            }
            }
            db.close();


            switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Database db = new Database(mActivity);
                    db.open();
                    ArrayList<String> listOfAlarmsFromDatabase = new ArrayList<String>();
                    listOfAlarmsFromDatabase = db.getAlarmNames();
                    for(int i = 0; i<listOfAlarmsFromDatabase.size(); i++){
                        if(listOfAlarmsFromDatabase.contains(String.valueOf(textView.getText()))){
                            ArrayList<String> listAlarmDetails =  db.getAlarmDetails(String.valueOf(textView.getText()));
                            db.addOrUpdateAlarmInDatabase(Integer.parseInt(listAlarmDetails.get(0)), listAlarmDetails.get(1), Integer.parseInt(listAlarmDetails.get(2)), listAlarmDetails.get(3), listAlarmDetails.get(4), String.valueOf(isChecked));
                        }
                    }
                    db.close();
                }
            });


            final int finalPosition = position;
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, SettingsActivity.class);
                    intent.putExtra("NAME", values.get(finalPosition));
                    startActivityForResult(intent, 0);
                    //finish();
                }
            });
            return rowView;
        }
    }
}
