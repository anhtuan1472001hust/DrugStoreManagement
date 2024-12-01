package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityForgetPassBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.MainActivity;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class ForgetPassActivity extends BaseActivity<ActivityForgetPassBinding, AuthViewModel> {

    private FirebaseAuth auth;

    @Override
    protected ActivityForgetPassBinding getViewBinding() {
        return ActivityForgetPassBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isAccountExisted.observe(this,isExisted -> {
            String phoneNumber = binding.edtUsername.getText().toString();
            if (isExisted) {
                Intent intent = new Intent(ForgetPassActivity.this, ConfigureAccountActivity.class);
                intent.putExtra(Constant.KEY_PHONE_NUMBER, phoneNumber);
                startActivity(intent);
            } else {
                showToastShort(getString(R.string.error_authentication));
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
        auth = FirebaseAuth.getInstance();
        binding.getRoot().setPadding(0, 0, 0, 0);
    }

    @Override
    public void addViewListener() {
        binding.btnNext.setOnClickListener(v -> {
            String phoneNumber = binding.edtUsername.getText().toString();
            viewModel.checkExistedAccount(phoneNumber);
        });
        binding.toolbar.setNavigationOnClickListener(view -> {
            openActivity(LoginActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        });

    }
}
