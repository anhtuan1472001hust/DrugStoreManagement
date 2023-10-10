package bk.pttkhdt.drugstoremanager.feature.main.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bk.pttkhdt.drugstoremanager.data.model.Bill;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.data.model.User;
import bk.pttkhdt.drugstoremanager.utils.Constant;
import io.reactivex.Completable;
import io.reactivex.Single;

public class MainRepositoryImpl implements MainRepository {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public Single<List<Medicine>> getListMedicine() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference().child(Constant.NODE_MEDICINE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Medicine> medicines = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                                    medicines.add(medicine);
                                }
                            }
                            emitter.onSuccess(medicines);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });

        });
    }

    @Override
    public Single<List<String>> getListIndex() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference().child(Constant.NODE_INDEX)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> listIndex = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String index = dataSnapshot.getKey();
                                    listIndex.add(index);
                                }
                                emitter.onSuccess(listIndex);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<List<Sales>> getListSales() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_INDEX)
                    .child(Constant.NODE_SALES)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Sales> listSales = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Sales sales = dataSnapshot.getValue(Sales.class);
                                    listSales.add(sales);
                                }
                                emitter.onSuccess(listSales);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<Medicine> getMedicineByName(String medicineName) {
        return Single.create(emitter -> {
            DatabaseReference databaseRef = firebaseDatabase.getReference(Constant.NODE_MEDICINE);
            Query query = databaseRef.orderByChild(Constant.NODE_NAME).equalTo(medicineName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Medicine medicine = dataSnapshot.getValue(Medicine.class);
                            emitter.onSuccess(medicine);
                        }
                    } else {
                        emitter.onError(new Throwable());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }

    @Override
    public Single<Boolean> updateBill(Bill bill) {
        return Single.create(emitter -> {
            DatabaseReference databaseReference = firebaseDatabase.getReference(Constant.NODE_BILL);
            databaseReference.child(String.valueOf(bill.getId())).setValue(bill)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    @Override
    public Single<List<Bill>> getListBill() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_INDEX)
                    .child(Constant.NODE_BILL)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Bill> listBill = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Bill bill = dataSnapshot.getValue(Bill.class);
                                    listBill.add(bill);
                                }
                                emitter.onSuccess(listBill);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<Boolean> addMedicine(Medicine medicine) {
        return Single.create(emitter -> {

        });
    }

    @Override
    public Single<User> queryUser(String phoneNumber) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .orderByChild(Constant.NODE_PHONE_NUMBER)
                    .equalTo(phoneNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (user != null) {
                                        emitter.onSuccess(user);
                                        return;
                                    }
                                }
                            }
                            emitter.onError(new Exception("Query failed"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Completable updateUserInfo(User user, String phoneNumber) {
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put(Constant.NODE_USER_NAME, user.getName());
        updateValues.put(Constant.NODE_USER_EMAIL, user.getEmail());
        updateValues.put(Constant.NODE_USER_ADDRESS, user.getAddress());
        updateValues.put(Constant.NODE_USER_BIRTHDAY, user.getBirthday());
        return Completable.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .orderByChild(Constant.NODE_PHONE_NUMBER)
                    .equalTo(phoneNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    String uid = dataSnapshot.getKey();
                                    firebaseDatabase.getReference(Constant.NODE_USER)
                                            .child(uid)
                                            .updateChildren(updateValues)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    emitter.onComplete();
                                                } else {
                                                    emitter.onError(task.getException());
                                                }
                                            });
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException()); // Lỗi khi truy vấn cơ sở dữ liệu
                        }
                    });
        });
    }

    @Override
    public Single<Boolean> updateSales(Sales sales) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_INDEX)
                    .child(Constant.NODE_SALES)
                    .child(sales.getDate())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                Sales previousSales = snapshot.getValue(Sales.class);

                                Sales updatedSales = new Sales();
                                updatedSales.setTotal(previousSales.getTotal() + sales.getTotal());
                                updatedSales.setDate(sales.getDate());


                                // Cập nhật thuộc tính của sales trong node formattedDate
                                snapshot.getRef().setValue(updatedSales)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                emitter.onSuccess(true);
                                            } else {
                                                emitter.onSuccess(false);
                                            }
                                        });

                            } else {
                                snapshot.getRef().setValue(sales)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                emitter.onSuccess(true);
                                            } else {
                                                emitter.onSuccess(false);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });

    }


}
