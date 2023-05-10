package bk.pttkhdt.drugstoremanager.core.component.datamanager;

import android.content.SharedPreferences;


/**
 * Created by @Author: TuanNNA
 * Create Time : 11:00 - 28/04/2023
 * Interface này để định nghĩa các phương thức để lưu dữ liệu local vào sharePreferences
 */

public interface DataManager {

    SharedPreferences getSharedPreferences();

    void savePhoneNumber(String phoneNumber);

    String getPhoneNumber();
}
