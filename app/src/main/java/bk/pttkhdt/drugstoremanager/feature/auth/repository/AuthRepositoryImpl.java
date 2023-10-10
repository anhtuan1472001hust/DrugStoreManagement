package bk.pttkhdt.drugstoremanager.feature.auth.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bk.pttkhdt.drugstoremanager.data.model.User;
import bk.pttkhdt.drugstoremanager.utils.Constant;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthRepositoryImpl implements AuthRepository{

    @Override
    public Completable sendOtp(PhoneAuthOptions phoneAuthOptions) {
        return Completable.create( emitter -> {
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            Log.e("Hello",phoneAuthOptions.toString());
            emitter.onComplete();
                }
        );
    }

    @Override
    public Completable createUser(User user) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        return Completable.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .child(userUid)
                    .child(Constant.NODE_PHONE_NUMBER)
                    .setValue(user.getPhoneNumber());
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .child(userUid)
                    .child(Constant.NODE_PASSWORD)
                    .setValue(user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    @Override
    public Single<Boolean> verifyOtp(PhoneAuthCredential phoneAuthCredential, String otpCode) {
        return Single.create( emitter -> FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String otpToken = phoneAuthCredential.getSmsCode();
                        if (otpCode.equals(otpToken)) {
                            emitter.onSuccess(true);
                        }
                    } else {
                        emitter.onError(task.getException());
                    }
                })
        );
    }

    @Override
    public Single<User> login(String phoneNumber, String password) {
        return Single.create(emitter -> FirebaseDatabase.getInstance().getReference(Constant.NODE_USER)
                .orderByChild(Constant.NODE_PHONE_NUMBER)
                .equalTo(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapShot: snapshot.getChildren()) {
                                User user = dataSnapShot.getValue(User.class);
                                if (user != null) {
                                    emitter.onSuccess(user);
                                    return;
                                }
                            }
                        }
                        emitter.onError(new Exception("Authentication failed"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                }));
    }

    @Override
    public Single<Boolean> checkExistAccount(String phoneNumber) {
        return Single.create( emitter -> FirebaseDatabase.getInstance().getReference(Constant.NODE_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                            FirebaseDatabase.getInstance().getReference(Constant.NODE_USER)
                                    .orderByChild(Constant.NODE_PHONE_NUMBER)
                                    .equalTo(phoneNumber)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                emitter.onSuccess(true);
                                            } else {
                                                emitter.onSuccess(false);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            emitter.onError(error.toException());
                                        }
                                    });
                        } else {
                            emitter.onSuccess(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                })
        );
    }

    @Override
    public Single<Boolean> checkIsLogin(String userUid) {
        return Single.create(emitter -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.NODE_USER).child(userUid);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                emitter.onSuccess(true);
                            } else {
                                emitter.onSuccess(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
                }

        );
    }
}
