package bk.pttkhdt.drugstoremanager.core.component.datamanager;

import android.content.SharedPreferences;

import bk.pttkhdt.drugstoremanager.core.component.sharepref.AppPreference;
import bk.pttkhdt.drugstoremanager.utils.Constant;


public class DataManagerImpl implements DataManager {

    private AppPreference appPreference;

    public DataManagerImpl(AppPreference appPreference) {
        this.appPreference = appPreference;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return appPreference.getSharedPreference();
    }

    @Override
    public void savePhoneNumber(String phoneNumber) {
        appPreference.saveString(Constant.KEY_PHONE_NUMBER_PREF,phoneNumber);
    }

    @Override
    public String getPhoneNumber() {
        return appPreference.getString(Constant.KEY_PHONE_NUMBER,Constant.EMPTY_STRING);
    }
}
