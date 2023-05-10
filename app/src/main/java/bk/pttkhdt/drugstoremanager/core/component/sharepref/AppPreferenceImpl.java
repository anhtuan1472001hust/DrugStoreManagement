package bk.pttkhdt.drugstoremanager.core.component.sharepref;

import android.content.Context;
import android.content.SharedPreferences;

import bk.pttkhdt.drugstoremanager.utils.Constant;

public class AppPreferenceImpl implements AppPreference {

    private Context context;


    public AppPreferenceImpl(Context context) {
        this.context = context;
    }

    @Override
    public SharedPreferences getSharedPreference() {
        return context.getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveString(String key, String value) {
        getSharedPreference().edit().putString(key, value).apply();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getSharedPreference().getString(key, defaultValue);
    }

    @Override
    public void saveInt(String key, int value) {
        getSharedPreference().edit().putInt(key, value).apply();
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return getSharedPreference().getInt(key, defaultValue);
    }

    @Override
    public void saveBoolean(String key, Boolean value) {
        getSharedPreference().edit().putBoolean(key, value).apply();
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return getSharedPreference().getBoolean(key, defaultValue);
    }

    @Override
    public void saveFloat(String key, Float value) {
        getSharedPreference().edit().putFloat(key, value).apply();
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return getSharedPreference().getFloat(key, defaultValue);
    }
}
