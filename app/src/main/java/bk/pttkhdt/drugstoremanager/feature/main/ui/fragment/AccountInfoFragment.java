package bk.pttkhdt.drugstoremanager.feature.main.ui.fragment;

import android.view.LayoutInflater;

import java.util.Objects;

import bk.pttkhdt.drugstoremanager.core.base.BaseFragment;
import bk.pttkhdt.drugstoremanager.databinding.FragmentAccountInfoBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.MainActivity;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;

public class AccountInfoFragment extends BaseFragment<FragmentAccountInfoBinding, MainViewModel> {

    @Override
    public void onCommonViewLoaded() {
        viewBinding.tvUserName.setText(Objects.requireNonNull(viewModel.userInfo().getValue()).getName());
        viewBinding.tvUserPhone.setText(Objects.requireNonNull(viewModel.userInfo().getValue()).getPhoneNumber());
    }

    @Override
    public void addViewListener() {
        viewBinding.rltPerson.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.addFragment(
                        UserInfoFragment.newInstance(),
                        true
                );
            }

        });

    }

    @Override
    protected FragmentAccountInfoBinding getBinding(LayoutInflater inflater) {
        return FragmentAccountInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected String getTagFragment() {
        return AccountInfoFragment.class.getSimpleName();
    }
}
