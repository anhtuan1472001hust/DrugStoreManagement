package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Intent;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityForgetPassBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class ForgetPassActivity extends BaseActivity<ActivityForgetPassBinding, AuthViewModel> {


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
                Intent intent = new Intent(this, ValidateOtpActivity.class);
                intent.putExtra(Constant.KEY_PHONE_NUMBER,phoneNumber);
                startActivity(intent);
            } else {
                showToastShort(getString(R.string.error_authentication));
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
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
