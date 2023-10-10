package bk.pttkhdt.drugstoremanager.feature.main.ui.fragment;

import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bk.pttkhdt.drugstoremanager.core.base.BaseFragment;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.databinding.FragmentHomeBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.AddMedicineActivity;
import bk.pttkhdt.drugstoremanager.feature.main.ui.activity.CreateBillActivity;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.HomeDetailIndexAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, MainViewModel> {

    private HomeDetailIndexAdapter homeDetailIndexAdapter = new HomeDetailIndexAdapter();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

    private DatabaseReference mDatabase;


    @Override
    public void onCommonViewLoaded() {
        viewBinding.rcvHome.setAdapter(homeDetailIndexAdapter);
        viewBinding.rcvHome.setLayoutManager(linearLayoutManager);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(calendar.getTime());

        mDatabase = FirebaseDatabase.getInstance().getReference(Constant.NODE_INDEX).child(Constant.NODE_SALES).child(formattedDate); // Thay "thongSo" bằng tên nút chứa dữ liệu của bạn trên Firebase Realtime Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Constant.NODE_TOTAL).getValue() != null) {
                    Log.e("Bello","change");
                    homeDetailIndexAdapter.salesAdapter.changeSales(dataSnapshot.getValue(Sales.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void addViewListener() {
        viewBinding.cvAddMedicine.setOnClickListener(v -> {
            openActivity(CreateBillActivity.class);
        });

    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.listIndex().observe(this,listIndex -> {
            homeDetailIndexAdapter.submitList(listIndex);
        });

        viewModel.listSales().observe(this, listSales -> {
            homeDetailIndexAdapter.salesAdapter.submitList(listSales);
        });

        viewModel.listBill().observe(this, listBill -> {
            homeDetailIndexAdapter.billAdapter.submitList(listBill);
        });

    }

    @Override
    protected FragmentHomeBinding getBinding(LayoutInflater inflater) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected String getTagFragment() {
        return HomeFragment.class.getSimpleName();
    }

}
