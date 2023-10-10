package bk.pttkhdt.drugstoremanager.core.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.databinding.DialogLoadingViewBinding;
import bk.pttkhdt.drugstoremanager.utils.extensions.Extensions;


public abstract class BaseActivity<VB extends ViewBinding, VM extends BaseViewModel> extends AppCompatActivity implements BaseBehavior {

    protected VB binding;
    protected VM viewModel;

    private Dialog dialogLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
//        viewModel.setContext(this);
        createLoadingDialog();
        addViewListener();
        addDataObserve();
        onCommonViewLoaded();
    }

    protected abstract VB getViewBinding();

    protected abstract Class<VM> getViewModelClass();

    /**
     * Hàm này để mở 1 activity khác
     */
    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * Hàm này để mở 1 activity khác và gửi dữ liệu kèm theo
     */
    protected void openActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(this, cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }

    /**
     * Hàm này để mở 1 activity khác kèm theo cờ điều khiển
     */
    protected void openActivity(Class<?> cls,int... flags) {
        Intent intent = new Intent(this,cls);
        for (int flag : flags) {
            intent.addFlags(flag);
        }
        startActivity(intent);
    }

    protected void openActivityForResult(Class<? extends AppCompatActivity> activityClass, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    protected void openActivityWithDataForResult(Class<? extends AppCompatActivity> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


    /**
     * Hàm này để show thông báo
     */
    protected void showToastShort(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Hàm này để thêm 1 fragment vào 1 activity
     */
    protected void addFragment(BaseFragment<?,?> fragment, String tag, boolean animate) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag != null ? tag : fragment.getTagFragment());
        if (animate) {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.setCustomAnimations(0, 0);
        }
        hideKeyboard();
        fragmentTransaction.add(android.R.id.content, fragment, tag);
        fragmentTransaction.commit();
    }

    public void addFragment(BaseFragment<?,?> fragment, boolean animate) {
        addFragment(
                fragment,
                fragment.getTagFragment(),
                animate
        );
    }

    /**
     * Ẩn bàn phím điện thoại
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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


    private void onErrorMessage(String errorMessage) {
        Extensions.showToastShort(this, errorMessage);
    }

    private void onErrorMessageId(Integer errorMessageId) {
        Extensions.showToastShort(this, getString(errorMessageId));
    }

    private Dialog getDialogLoading() {
        if (dialogLoading == null) {
            dialogLoading = new Dialog(this, R.style.AppTheme_FullScreen_LightStatusBar);
            if (dialogLoading.getWindow() != null) {
                dialogLoading.getWindow().setBackgroundDrawableResource(R.color.white_50);
            }
        }
        return dialogLoading;
    }

    @Override
    protected void onDestroy() {
        viewModel.compositeDisposable.dispose();
        super.onDestroy();
    }

    private void createLoadingDialog() {
        getDialogLoading().requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogLoadingViewBinding dialogLoadingViewBinding = DialogLoadingViewBinding.inflate(getLayoutInflater());
        getDialogLoading().setContentView(dialogLoadingViewBinding.getRoot());
    }

}
