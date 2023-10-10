package bk.pttkhdt.drugstoremanager.feature.main.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseFragment;
import bk.pttkhdt.drugstoremanager.data.model.User;
import bk.pttkhdt.drugstoremanager.databinding.FragmentUserInfoBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.MainActivity;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class UserInfoFragment extends BaseFragment<FragmentUserInfoBinding, MainViewModel> {


    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public void onCommonViewLoaded() {
        setSystemBar(R.color.blue_gradient_end);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
        viewModel.queryUserInfo(phoneNumber);
    }

    @Override
    public void addViewListener() {
        viewBinding.btnBack.setOnClickListener(v -> {
            updateUser();
            requireActivity().onBackPressed();
            setSystemBar(R.color.light_background);
        });
        viewBinding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
            setSystemBar(R.color.light_background);
        });


    }

    @Override
    public void addDataObserve() {
        viewModel.userInfo().observe(this,user -> {
            viewBinding.edtName.setText(user.getName());
            viewBinding.tvEmailAdress.setText(user.getEmail());
            viewBinding.layoutContent.edtEmail.setText(user.getEmail());
            viewBinding.layoutContent.edtAddress.setText(user.getAddress());
            viewBinding.layoutContent.edtDob.setText(user.getBirthday());
        });

    }

    @Override
    protected FragmentUserInfoBinding getBinding(LayoutInflater inflater) {
        return FragmentUserInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected String getTagFragment() {
        return UserInfoFragment.class.getSimpleName();
    }

    private void updateUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
        User user1 = new User();
        user1.setName(viewBinding.edtName.getText().toString());
        user1.setEmail(viewBinding.layoutContent.edtEmail.getText().toString());
        user1.setAddress(viewBinding.layoutContent.edtAddress.getText().toString());
        user1.setBirthday(viewBinding.layoutContent.edtDob.getText().toString());
        viewModel.updateUserInfo(user1, phoneNumber);
    }

    private void setSystemBar(int color) {
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


}
