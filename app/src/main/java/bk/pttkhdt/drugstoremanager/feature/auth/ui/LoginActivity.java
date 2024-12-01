package bk.pttkhdt.drugstoremanager.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bk.pttkhdt.drugstoremanager.databinding.ActivityLoginBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.MainActivity;
import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.feature.auth.viewmodel.AuthViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, AuthViewModel> {

    boolean isPasswordVisible = false;
    private FirebaseAuth auth;

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
        auth = FirebaseAuth.getInstance();
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
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.userResponse().observe(this,user -> {
            String password = binding.edtPassword.getText().toString();
            String phoneNumber = binding.edtPhone.getText().toString();
            if (password.equals(user.getPassword())) {
                showToastShort(getString(R.string.success_login));
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF,phoneNumber).apply();
                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                onLoading(false);
                showToastShort(getString(R.string.error_login));
            }
        });
    }

    private void onClickBtnLogin() {
        String phoneNumber = binding.edtPhone.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (viewModel.isValidGmail(phoneNumber) && viewModel.checkValidPassword(password)) {
            loginWithEmailPassword(phoneNumber, password);
        }
    }

    private void loginWithEmailPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
