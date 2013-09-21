package com.waterloo.buddyalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
            case R.id.action_save:
                saveChanges();
                return true;
            case R.id.action_cancel:
                //cancelChanges();
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
                    .setPositiveButton(R.string.alert_OptionsSaveChanges, new DialogInterface.OnClickListener() {
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

        m_AlarmNameEdit   = (EditText) findViewById(R.id.et_AlarmName);
        m_AlarmPassEdit   = (EditText) findViewById(R.id.et_AlarmPass);

        if(m_AlarmNameEdit.getText().toString().isEmpty() || m_AlarmNameEdit == null){
            showSaveChangesDialog();
        }
        else{
            m_AlarmNameChange = m_AlarmNameEdit.getText().toString();
        }

    }

    private void showSaveChangesDialog() {
        DialogFragment newFragment = new SaveChangesDialog();
        newFragment.show(getFragmentManager(), "test");
    }

}
