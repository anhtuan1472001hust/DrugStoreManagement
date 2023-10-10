package bk.pttkhdt.drugstoremanager.core.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.utils.extensions.Extensions;

public abstract class BaseFragment<VB extends ViewBinding,VM extends BaseViewModel> extends Fragment implements BaseBehavior {

    protected VB viewBinding;

    protected VM viewModel;

    private Dialog dialogLoading = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = getBinding(inflater);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(getViewModelClass());
        addViewListener();
        addDataObserve();
        onCommonViewLoaded();
    }

    protected abstract VB getBinding(LayoutInflater inflater);

    protected abstract Class<VM> getViewModelClass();

    protected abstract String getTagFragment();


    /**
     *  Hàm này để mở 1 activity khác
     */
    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /**
     *  Hàm này để mở 1 activity khác và gửi dữ liệu kèm theo
     */
    protected void openActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(getActivity(),cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }

    protected void openActivity(Class<?> cls,int... flags) {
        Intent intent = new Intent(getActivity(),cls);
        for (int flag : flags) {
            intent.addFlags(flag);
        }
        startActivity(intent);
    }

    @Override
    public void addDataObserve() {
        viewModel.errorState.observe(this, this::onErrorMessage);
        viewModel.errorMessage.observe(this, this::onErrorMessageId);
        viewModel.loadingState.observe(this,this::onLoading);
    }

    @Override
    public void onLoading(Boolean isLoading) {
        if (isLoading) {
            getDialogLoading().show();
        } else {
            getDialogLoading().dismiss();
        }
    }

    private Dialog getDialogLoading() {
        if (dialogLoading == null) {
            dialogLoading = new Dialog(getActivity(), R.style.AppTheme_FullScreen_LightStatusBar);
            if (dialogLoading.getWindow() != null) {
                dialogLoading.getWindow().setBackgroundDrawableResource(R.color.white_50);
            }
        }
        return dialogLoading;
    }

    private void onErrorMessage(String errorMessage) {
        Extensions.showToastShort(requireActivity(), errorMessage);
    }


    private void onErrorMessageId(Integer errorMessageId) {
        Extensions.showToastShort(requireActivity(), getString(errorMessageId));
    }

    public void hideKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
