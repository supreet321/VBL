package com.waterloo.buddyalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;

public class SettingsActivity extends Activity {

    Activity mActivity;

    ActionBar m_ActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        m_ActionBar = getActionBar();
        mActivity = this;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_cancel:
                super.onBackPressed();
                return true;
            case R.id.action_save:
                saveChanges();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SaveChangesDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.alert_NameSaveChanges)
                    .setPositiveButton(R.string.alert_OptionsOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

    }


    public void saveChanges()
    {

        EditText m_AlarmNameEdit;
        EditText m_AlarmPassEdit;
        String m_AlarmNameChange;
        String m_AlarmPassChange;
        TimePicker m_TimePicker;

        m_TimePicker = (TimePicker) findViewById((R.id.tp_AlarmTime));
        m_AlarmNameEdit   = (EditText) findViewById(R.id.et_AlarmName);
        m_AlarmPassEdit   = (EditText) findViewById(R.id.et_AlarmPass);

        if(m_AlarmNameEdit != null && !m_AlarmNameEdit.getText().toString().isEmpty())
            if(m_AlarmPassEdit != null !m_AlarmPassEdit.getText().toString().isEmpty())

    }

    private void showSaveChangesDialog() {
        DialogFragment newFragment = new SaveChangesDialog();
        newFragment.show(getFragmentManager(), "test");
    }

}
