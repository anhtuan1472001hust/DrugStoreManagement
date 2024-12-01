package bk.pttkhdt.drugstoremanager.feature.main.ui.activity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.databinding.ActivityMainBinding;
import bk.pttkhdt.drugstoremanager.feature.auth.ui.LoginActivity;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.MainViewPagerAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;
import bk.pttkhdt.drugstoremanager.utils.view.DialogView;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isUpdateBillSuccess.observe(this, isUpdateBillSuccess -> {
            if (isUpdateBillSuccess) {
                Log.e("Bello","update bill success");
                viewModel.getListBill();
            }
        });

    }

    @Override
    public void onCommonViewLoaded() {
        viewModel.getListMedicine();
        viewModel.getListIndex();
        viewModel.getListSales();
        viewModel.getListBill();
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
        viewModel.queryUserInfo(phoneNumber);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        onClickNavigationDrawer();

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        binding.mainVp2.setAdapter(mainViewPagerAdapter);
        binding.mainVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_store));
                        binding.bottomNavigation.getMenu().findItem(R.id.menu_store).setChecked(true);
                        break;
                    case 2:
                        binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_account));
                        binding.bottomNavigation.getMenu().findItem(R.id.menu_account).setChecked(true);
                        break;
                    case 0:
                    default:
                        binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_home));
                        binding.bottomNavigation.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_home));
                    binding.mainVp2.setCurrentItem(0);
                    break;
                case R.id.menu_store:
                    binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_store));
                    binding.mainVp2.setCurrentItem(1);
                    break;
                case R.id.menu_account:
                    binding.toolbarTitle.setText(getString(R.string.drawer_menu_label_account));
                    binding.mainVp2.setCurrentItem(2);
                    break;
            }
            return true;
        });
        binding.navHeaderMain.tvUserPhone.setText(phoneNumber);
    }

    @Override
    public void addViewListener() {
        binding.navHeaderMain.btnLogout.setOnClickListener(v -> {
            Runnable listenerPositive = () -> {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF, Constant.EMPTY_STRING).apply();
                openActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK,Intent.FLAG_ACTIVITY_NEW_TASK,Intent.FLAG_ACTIVITY_CLEAR_TASK);
            };
            DialogView.showDialogDescriptionByHtml(
                    this,
                    getString(R.string.drawer_menu_label_logout),
                    getString(R.string.confirm_logout_title),
                    listenerPositive
            );
        });

        binding.btnDrawer.setOnClickListener(v -> {
            hideKeyboard();
            if (binding.dlLayout.isDrawerOpen(GravityCompat.START)) {
                binding.dlLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.dlLayout.openDrawer(GravityCompat.START);
                binding.navHeaderMain.navHeaderMain.bringToFront();
                binding.navHeaderMain.navHeaderMain.requestFocus();
            }
        });

        binding.navHeaderMain.btnHome.setOnClickListener(v -> {
            binding.mainVp2.setCurrentItem(1);
            binding.dlLayout.closeDrawer(GravityCompat.START);
        });



    }

    private void onClickNavigationDrawer() {
        binding.dlLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                float angle = slideOffset * 180;
                binding.btnDrawer.setRotation(angle);
                binding.navHeaderMain.navHeaderMain.bringToFront();
                binding.navHeaderMain.navHeaderMain.requestFocus();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                binding.btnDrawer.setRotation(90f);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                binding.btnDrawer.setRotation(0f);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

}