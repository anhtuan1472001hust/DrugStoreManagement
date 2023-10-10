package bk.pttkhdt.drugstoremanager.feature.auth.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseViewModel;
import bk.pttkhdt.drugstoremanager.data.model.User;
import bk.pttkhdt.drugstoremanager.feature.auth.repository.AuthRepository;
import bk.pttkhdt.drugstoremanager.feature.auth.repository.AuthRepositoryImpl;
import bk.pttkhdt.drugstoremanager.utils.livedata.SingleLiveEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends BaseViewModel {

    private FirebaseAuth mAuth;

    private User user = new User();

    private String verificationId;

    private AuthRepository authRepository = new AuthRepositoryImpl();

    private final MutableLiveData<String> _otpToken = new MutableLiveData<>();

    public LiveData<String> otpToken() {
        return _otpToken;
    }

    public SingleLiveEvent<Boolean> validateOtpSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> registerSuccess = new SingleLiveEvent<>();

    private final MutableLiveData<User> _userResponse = new MutableLiveData<>();

    public LiveData<User> userResponse() {
        return _userResponse;
    }

    public SingleLiveEvent<Boolean> isAccountExisted = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> isLoginSession = new SingleLiveEvent<>();

//    private final NetWorkExtensions netWorkExtensions = new NetWorkExtensions();

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            setErrorStringId(R.string.activity_send_otp_expired_otp_code);
        }
    };

    public void requestOtp(String phoneNumber, Activity activity) {
        String formatPhoneNumber = phoneNumber.replaceFirst("0", "+84");
        mAuth = FirebaseAuth.getInstance();
//        dataManager.savePhoneNumber(phoneNumber);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(formatPhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallBacks)
                        .build();
//        netWorkExtensions.checkInternetConnection(isConnect -> {
//            if (isConnect) {
        compositeDisposable.add(
                authRepository.sendOtp(options)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {

                                }, throwable -> {
                                    setErrorStringId(R.string.error_send_otp_from_firebase);
                                }

                        ));
//            } else {
//                setErrorStringId(R.string.error_have_no_internet);
//            }
//            return null;
//        });
    }

    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        verifyWithCredential(credential, code);
    }


    /**
     * Call Api Firebase Auth xác nhận mã OTP
     * @param credential
     */
    private void verifyWithCredential(PhoneAuthCredential credential, String otpCode) {
        mAuth = FirebaseAuth.getInstance();
        compositeDisposable.add(
                authRepository.verifyOtp(credential, otpCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isSuccess -> {
                                    validateOtpSuccess.setValue(isSuccess);
                                }, throwable -> {
                                    validateOtpSuccess.setValue(false);
                                }
                        )
        );
    }

    /**
     * Kiểm tra số điện thoại có hợp lệ không
     *
     * @param phoneNumber
     * @return
     */
    public Boolean checkValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            setErrorStringId(R.string.error_edt_account_null);
            return false;
        }
        phoneNumber = phoneNumber.replaceAll("\\s", "").replaceAll("-", "");
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            setErrorStringId(R.string.activity_add_member_Invalid_format_Phone_number);
            return false;
        }
        Pattern pattern = Pattern.compile("^(0|\\+84)[3|5|7|8|9][0-9]{8}$");
        if (phoneNumber.length() == 10 && pattern.matcher(phoneNumber).matches()) {
            return true;
        } else {
            setErrorStringId(R.string.activity_add_member_Invalid_format_Phone_number);
            return false;
        }
    }


    /**
     * Kiểm tra password có hợp lệ không
     *
     * @param password
     * @return
     */
    public Boolean checkValidPassword(String password) {
        if (password.isEmpty()) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        } else {
            if (password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*")) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * Call Api realtime DB Tạo user với số điện thoại và mật khẩu
     *
     * @param password
     */
    public void createUserWithPhoneAndPassword(String password, String phoneNumber) {
//        String phoneNumber = dataManager.getPhoneNumber();
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        compositeDisposable.add(
                authRepository.createUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> registerSuccess.setValue(true),
                                throwable -> registerSuccess.setValue(false)

                        )
        );
    }

    public void loginRequest(String phoneNumber, String password) {
        setLoading(true);
//        netWorkExtensions.checkInternetConnection(isConnect -> {
//            if (isConnect) {
                compositeDisposable.add(
                        authRepository.login(phoneNumber, password)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(_userResponse::setValue, throwable -> {
                                    setLoading(false);
                                    setErrorStringId(R.string.error_authentication);
                                }
                                )
                );
//            } else {
//                setErrorStringId(R.string.error_have_no_internet);
//            }
//            return null;
//        });
    }

    public void checkExistedAccount(String phoneNumber) {
//        netWorkExtensions.checkInternetConnection( isConnect -> {
//            if (isConnect) {
        compositeDisposable.add(
                authRepository.checkExistAccount(phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isExisted -> {
                            isAccountExisted.setValue(isExisted);
                                },
                                throwable -> {
                                    setErrorStringId(R.string.error_send_otp_from_firebase);
                                }
                        )
        );
    }
//            else {
//                setErrorStringId(R.string.error_have_no_internet);
//            }
//            return null;
//        });

    public void checkIsLogin(String userUid) {
        compositeDisposable.add(
                authRepository.checkIsLogin(userUid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isLogin -> {
                            isLoginSession.setValue(isLogin);
                        })
        );
    }
}


