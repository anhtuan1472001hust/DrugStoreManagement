package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityRegisterBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, AuthViewModel> {

    private FirebaseAuth auth;
    @Override
    protected ActivityRegisterBinding getViewBinding() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
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
            if (viewModel.isValidGmail(phoneNumber)) {
                viewModel.checkExistedAccount(phoneNumber);
            } else {
                showToastShort(getString(R.string.activity_add_member_Invalid_format_Phone_number));
            }
        });
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isAccountExisted.observe(this, isExisted -> {
            if (isExisted) {
                showToastShort(getString(R.string.error_account_existed));
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF, binding.edtUsername.getText().toString()).apply();
                Intent intent = new Intent(this,ConfigureAccountActivity.class);
                intent.putExtra(Constant.KEY_PHONE_NUMBER, binding.edtUsername.getText().toString());
                startActivity(intent);
            }
        });

    }
}
