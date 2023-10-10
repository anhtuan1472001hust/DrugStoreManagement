package bk.pttkhdt.drugstoremanager.feature.main.repository;


import java.util.List;

import bk.pttkhdt.drugstoremanager.data.model.Bill;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.data.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface MainRepository {
    Single<List<Medicine>> getListMedicine();
    Single<List<String>> getListIndex();
    Single<List<Sales>> getListSales();
    Single<Medicine> getMedicineByName(String medicineName);
    Single<Boolean> updateBill(Bill bill);
    Single<List<Bill>> getListBill();

    Single<Boolean> addMedicine(Medicine medicine);

    Single<User> queryUser(String phoneNumber);

    Completable updateUserInfo(User user, String phoneNumber);

    Single<Boolean> updateSales(Sales sales);



}
