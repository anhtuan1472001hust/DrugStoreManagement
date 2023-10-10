package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import bk.pttkhdt.drugstoremanager.feature.main.ui.fragment.AccountInfoFragment;
import bk.pttkhdt.drugstoremanager.feature.main.ui.fragment.HomeFragment;
import bk.pttkhdt.drugstoremanager.feature.main.ui.fragment.MedicineStoreFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MedicineStoreFragment();
            case 2:
                return new AccountInfoFragment();
            case 0:
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
