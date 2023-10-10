package bk.pttkhdt.drugstoremanager.feature.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseViewModel;
import bk.pttkhdt.drugstoremanager.data.model.Bill;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.data.model.User;
import bk.pttkhdt.drugstoremanager.feature.main.repository.MainRepository;
import bk.pttkhdt.drugstoremanager.feature.main.repository.MainRepositoryImpl;
import bk.pttkhdt.drugstoremanager.utils.livedata.SingleLiveEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {

    MainRepository mainRepository = new MainRepositoryImpl();

    private MutableLiveData<List<Medicine>> _listMedicine = new MutableLiveData<>();
    public LiveData<List<Medicine>> listMedicine() {
        return _listMedicine;
    }

    private MutableLiveData<List<String>> _listIndex = new MutableLiveData<>();
    public LiveData<List<String>> listIndex() {
        return _listIndex;
    }

    private MutableLiveData<List<Sales>> _listSales = new MutableLiveData<>();
    public LiveData<List<Sales>> listSales() {
        return _listSales;
    }

    private MutableLiveData<Medicine> _medicine = new MutableLiveData<>();
    public LiveData<Medicine> medicine() {
        return _medicine;
    }

    public SingleLiveEvent<Boolean> isUpdateBillSuccess = new SingleLiveEvent<>();

    private MutableLiveData<List<Bill>> _listBill = new MutableLiveData<>();
    public LiveData<List<Bill>> listBill() {
        return _listBill;
    }

    private MutableLiveData<User> _userInfo = new MutableLiveData<>();

    public LiveData<User> userInfo() {
        return _userInfo;
    }

    public SingleLiveEvent<Boolean> updateUserSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> updateSalesState = new SingleLiveEvent<>();




    public void getListMedicine() {
        compositeDisposable.add(
                mainRepository.getListMedicine()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listMedicine -> {
                                    _listMedicine.setValue(listMedicine);
                                },
                                throwable -> setErrorStringId(R.string.error_query)
                        )
        );
    }

    public void getListIndex() {
        compositeDisposable.add(
                mainRepository.getListIndex()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listIndex -> _listIndex.setValue(listIndex),
                                throwable -> setErrorStringId(R.string.error_query)
                        )
        );
    }

    public void getListSales() {
        compositeDisposable.add(
                mainRepository.getListSales()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listSales -> _listSales.setValue(listSales),
                                throwable -> setErrorStringId(R.string.error_query)
                        )
        );
    }

    public void getMedicineByName(String medicineName) {
        compositeDisposable.add(
                mainRepository.getMedicineByName(medicineName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                medicine -> _medicine.setValue(medicine),
                                throwable -> setErrorStringId(R.string.not_exist_medicine)
                        )
        );
    }

    public void updateBill(Bill bill) {
        compositeDisposable.add(
                mainRepository.updateBill(bill)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( isSuccess -> isUpdateBillSuccess.setValue(isSuccess),
                                throwable -> setErrorStringId(R.string.error_query)
                        )
        );
    }

    public void getListBill() {
        compositeDisposable.add(
                mainRepository.getListBill()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listBill -> _listBill.setValue(listBill),
                                throwable -> setErrorStringId(R.string.error_query)
                        )
        );
    }

    public void queryUserInfo(String phoneNumber) {
        compositeDisposable.add(
                mainRepository.queryUser(phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userInfo -> {
                                    _userInfo.setValue(userInfo);
                                }, throwable -> {

                                }
                        )
        );
    }

    public void updateUserInfo(User user, String phoneNumber) {
        compositeDisposable.add(
                mainRepository.updateUserInfo(user, phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> updateUserSuccess.setValue(true), throwable -> updateUserSuccess.setValue(false)
                        ));
    }

    public void updateSales(Sales sales) {
        compositeDisposable.add(
                mainRepository.updateSales(sales)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isSuccess -> {
                                    updateSalesState.setValue(isSuccess);
                                }, throwable -> {
                                    updateSalesState.setValue(false);
                                }
                        )
        );
    }
    }


//    public void getListIndexDetail(List<String> nodes) {
//        compositeDisposable.add(
//                Observable.merge(mainRepository.observeFireBaseNode(nodes.get(0),
//                                mainRepository.observeFireBaseNode(nodes.get(1)))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<DataSnapshot>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        }))
//        );
//    }

