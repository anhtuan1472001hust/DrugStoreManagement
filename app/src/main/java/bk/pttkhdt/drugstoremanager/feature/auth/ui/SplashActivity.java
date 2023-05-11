package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;


import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivitySplashBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.feature.main.ui.MainActivity;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, AuthViewModel> {

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isLoginSession.observe(this, isLogin -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (isLogin) {
                    openActivity(MainActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                finish();
            }, 5000);
        });
    }

    @Override
    public void onCommonViewLoaded() {
        binding.getRoot().setPadding(0, 0, 0, 0);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(Constant.KEY_UID_PREF, Constant.EMPTY_STRING);
        viewModel.checkIsLogin(uid);
    }

    @Override
    public void addViewListener() {

    }
}
