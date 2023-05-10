package bk.pttkhdt.drugstoremanager.feature.main.ui;

import android.content.Intent;
import android.os.Handler;


import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivitySplashBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.ui.LoginActivity;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, MainViewModel> {

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.getRoot().setPadding(0, 0, 0, 0);
        delayFunction();
    }

    @Override
    public void addViewListener() {

    }

    private void delayFunction() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            openActivity(LoginActivity.class ,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }, 5000);
    }

}
