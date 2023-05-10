package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import com.mukesh.OnOtpCompletionListener;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityValidateOtpBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class ValidateOtpActivity extends BaseActivity<ActivityValidateOtpBinding, AuthViewModel> {

    private OnOtpCompletionListener mOnOtpCompletionListener = otp -> {
        onLoading(true);
        viewModel.verifyCode(otp);
    };

    private CountDownTimer countDownTimer;

    private final String timeFormat = "%02d:%02d";

    private int mCountDown = 60;

    @Override
    protected ActivityValidateOtpBinding getViewBinding() {
        return ActivityValidateOtpBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.validateOtpSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                openActivity(ConfigureAccountActivity.class);
            } else {
                showToastShort(getString(R.string.activity_send_otp_failed_otp_code));
                onLoading(false);
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
        binding.getRoot().setPadding(0, 0, 0, 0);
        binding.edtOTP.requestFocus();
        binding.edtOTP.setEnabled(true);
        binding.edtOTP.setClickable(true);
        binding.edtOTP.setOtpCompletionListener(mOnOtpCompletionListener);
        sendOtp();
    }

    @Override
    public void addViewListener() {
        binding.btnRequestOTP.setOnClickListener(v -> resendOtp());
        binding.customToolbar.setNavigationOnClickListener(v -> {
            countDownTimer.cancel();
            openActivity(LoginActivity.class);
            finish();
        });
    }

    private void setOtpDuration() {
        mCountDown = 60;
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    mCountDown -= 1;
                    binding.tvTimer.setText(String.format(timeFormat,0,mCountDown));
                }

                @SuppressLint("DefaultLocale")
                @Override
                public void onFinish() {
                    mCountDown = 0;
                    binding.tvTimer.setText(String.format(timeFormat,0,0));
                    countDownTimer.cancel();
                }
            };
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void sendOtp() {
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF,intent.getStringExtra(Constant.KEY_PHONE_NUMBER)).apply();
        viewModel.requestOtp(intent.getStringExtra(Constant.KEY_PHONE_NUMBER), this);
        setOtpDuration();
    }

    private void resendOtp() {
        countDownTimer.cancel();
        binding.edtOTP.clearComposingText();
        binding.edtOTP.setText(Constant.EMPTY_STRING);
        sendOtp();
    }
}
