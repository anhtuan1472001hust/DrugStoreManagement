package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import bk.pttkhdt.drugstoremanager.feature.main.ui.MainActivity;
import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityLoginBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, AuthViewModel> {

    boolean isPasswordVisible = false;

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
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
        binding.tvRegister.setOnClickListener(view -> openActivity(RegisterActivity.class));
        binding.btnLogin.setOnClickListener(v -> onClickBtnLogin());
        binding.eyeImage.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.edtPassword.setTransformationMethod(null);
                binding.eyeImage.setSelected(true);
            } else {
                binding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.eyeImage.setSelected(false);
            }
            binding.edtPassword.setSelection(binding.edtPassword.length());
        });
        binding.tvForgotPassword.setOnClickListener(v -> openActivity(ForgetPassActivity.class));
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.userResponse().observe(this,user -> {
            String password = binding.edtPassword.getText().toString();
            if (password.equals(user.getPassword())) {
                showToastShort(getString(R.string.success_login));
                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                showToastShort(getString(R.string.error_login));
            }
        });
    }

    private void onClickBtnLogin() {
        String phoneNumber = binding.edtPhone.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (viewModel.checkValidPhoneNumber(phoneNumber) && viewModel.checkValidPassword(password)) {
            viewModel.loginRequest(phoneNumber, password);
        }
    }


}
