package com.devseven.gympack.setlogger;

import android.content.Context;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * Created by Klejnot Nilu on 26.10.2017.
 */

public class UserSettings
{
    private static UserSettings _userSettings;
    private boolean _useImperial = false;
    private int _defaultRest = 90;
    private int _defaultReps = 12;
    private boolean showBanner = true;
    private boolean showInterstitial = true;
    private boolean setToRecord = true;

    public boolean getSetToRecord()
    {
        return setToRecord;
    }
    public boolean getUseImperial()
    {
        return _useImperial;
    }
    public boolean getShowBanner()
    {
        return showBanner;
    }
    public boolean getShowInter()
    {
        return showInterstitial;
    }
    public void setSetToRecord(boolean setToRecord) {
        this.setToRecord = setToRecord;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public void setShowInterstitial(boolean showInterstitial) {
        this.showInterstitial = showInterstitial;
    }

    public void set_useImperial(boolean _useImperial) {
        this._useImperial = _useImperial;
    }

    public void set_defaultReps(int _defaultReps) {
        this._defaultReps = _defaultReps;
    }

    public void set_defaultRest(int _defaultRest) {
        this._defaultRest = _defaultRest;
    }

    public int get_defaultRest()
    {
        return _defaultRest;
    }
    public int get_defaultReps()
    {
        return _defaultReps;
    }
    public static UserSettings getUserSettings(Context context)
    {
        if(_userSettings==null)
        {
            File loc = new File(context.getFilesDir(),GlobalSettings.FILENAME_USERSETTINGS);
            Serializer s = new Persister();
            try {
                _userSettings = s.read(UserSettings.class,loc);
            } catch (Exception e) {
                e.printStackTrace();
                _userSettings = new UserSettings();
            }

        }
        return _userSettings;
    }
    public static void SaveUserSettings(Context context)
    {

    }

}