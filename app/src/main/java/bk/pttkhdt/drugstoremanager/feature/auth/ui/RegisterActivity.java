package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Intent;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityRegisterBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, AuthViewModel> {


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
        binding.getRoot().setPadding(0, 0, 0, 0);
    }

    @Override
    public void addViewListener() {
        binding.btnNext.setOnClickListener(v -> {
            String phoneNumber = binding.edtUsername.getText().toString();
            if (viewModel.checkValidPhoneNumber(phoneNumber)) {
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
                Intent intent = new Intent(this,ValidateOtpActivity.class);
                intent.putExtra(Constant.KEY_PHONE_NUMBER, binding.edtUsername.getText().toString());
                startActivity(intent);
            }
        });

    }
}
