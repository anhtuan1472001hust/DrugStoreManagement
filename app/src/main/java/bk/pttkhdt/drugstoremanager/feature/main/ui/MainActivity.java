package bk.pttkhdt.drugstoremanager.feature.main.ui;

import android.content.Intent;

import android.view.View;



import com.google.firebase.auth.FirebaseAuth;

import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityMainBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.ui.LoginActivity;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {

    }

    @Override
    public void addViewListener() {
        binding.tvWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                openActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK,Intent.FLAG_ACTIVITY_NEW_TASK,Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });

    }
}