package com.devseven.gympack.setlogger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    private boolean savedSettings;
    private UserSettings currentSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentSettings = UserSettings.getUserSettings(this);
        setContentView(R.layout.fragment_settings);
        EditText defaultReps = (EditText)findViewById(R.id.defaultReps);
        EditText defaultRest = (EditText)findViewById(R.id.defaultRest);
        Switch setToRecord = (Switch)findViewById(R.id.setStartToRecord);
        Switch useImperial = (Switch)findViewById(R.id.useImperial);
        final Switch showBanner = (Switch)findViewById(R.id.showBanner);
        final Switch showIntestital = (Switch)findViewById(R.id.showInterstital);
        setToRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currentSettings.setSetToRecord(b);
            }
        });
        useImperial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currentSettings.set_useImperial(b);
            }
        });
        showBanner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currentSettings.setShowBanner(b);
                if(!currentSettings.getShowInter() && !currentSettings.getShowBanner()) {
                    currentSettings.setShowInterstitial(true);
                    showIntestital.setChecked(true);
                    Toast.makeText(SettingsActivity.this, "Support the developer to disable both ads.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showIntestital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currentSettings.setShowInterstitial(b);
                if(!currentSettings.getShowInter() && !currentSettings.getShowBanner()) {
                    currentSettings.setShowBanner(true);
                    showBanner.setChecked(true);
                    Toast.makeText(SettingsActivity.this, "Support the developer to disable both ads.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showBanner.setChecked(currentSettings.getShowBanner());
        showIntestital.setChecked(currentSettings.getShowInter());
        useImperial.setChecked(currentSettings.getUseImperial());
        setToRecord.setChecked(currentSettings.getSetToRecord());
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.action_settings));


    }
    @Override
    public void onPause()
    {
        UserSettings.SaveUserSettings(this);
        savedSettings=true;
        super.onPause();

    }
    @Override
    public void onDestroy()
    {
        if(!savedSettings)
            UserSettings.SaveUserSettings(this);
        super.onDestroy();

    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
