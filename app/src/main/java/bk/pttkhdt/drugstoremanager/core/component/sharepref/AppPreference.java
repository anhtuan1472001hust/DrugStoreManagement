package bk.pttkhdt.drugstoremanager.core.component.sharepref;

import android.content.SharedPreferences;

/**
 * Created by @Author: TuanNNA
 * Create Time : 11:00 - 28/04/2023
 * Interface này để định nghĩa các phương thức của SharedPreferences
 */

public interface AppPreference {

    SharedPreferences getSharedPreference();

    void saveString(String key, String value);
    String getString(String key, String defaultValue);

    void saveInt(String key, int value);
    int getInt(String key, int defaultValue);

    void saveBoolean(String key, Boolean value);
    Boolean getBoolean(String key, Boolean defaultValue);

    void saveFloat(String key, Float value);
    Float getFloat(String key, Float defaultValue);
}
