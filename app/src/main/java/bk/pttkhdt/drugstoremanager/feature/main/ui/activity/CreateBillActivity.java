package bk.pttkhdt.drugstoremanager.feature.main.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.data.model.Bill;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.databinding.ActivityCreateBillBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.CreateBillAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class CreateBillActivity extends BaseActivity<ActivityCreateBillBinding, MainViewModel> {

    private static final int REQUEST_CODE = 0;
    private List<Medicine> listMedicine = new ArrayList<>();

    private final CreateBillAdapter createBillAdapter = new CreateBillAdapter();

    private long totalSales = 0 ;

    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);



    @Override
    protected ActivityCreateBillBinding getViewBinding() {
        return ActivityCreateBillBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.titleToolbar.setText(getString(R.string.sell));
        binding.rcvDetail.setAdapter(createBillAdapter);
        binding.rcvDetail.setLayoutManager(linearLayoutManager);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    public void addViewListener() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.addMedicine.setOnClickListener(v -> openActivityForResult(AddMedicineActivity.class,REQUEST_CODE));
        binding.btnAddMedicine.setOnClickListener(v -> onClickCreateBill());
        binding.titleCancel.setOnClickListener(v -> {
            binding.btnAddMedicine.setVisibility(View.VISIBLE);
            binding.titleCancel.setVisibility(View.GONE);
            binding.addMedicine.setVisibility(View.VISIBLE);
            binding.btnPayment.setVisibility(View.GONE);
            binding.titleToolbar.setText(getString(R.string.sell));
        });
        binding.btnPayment.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(calendar.getTime());
            Sales sales = new Sales();
            sales.setTotal(totalSales);
            sales.setDate(formattedDate);
            viewModel.updateSales(sales);
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_INDEX).child(Constant.NODE_BILL);

            Log.e("Bello","listMedicine: " + listMedicine.size());
            for (Medicine medicine : listMedicine) {
                Bill bill = new Bill();
                bill.setMedicineName(medicine.getName());
                bill.setPrice(medicine.getPrice());
                bill.setQuantity(medicine.getInventory());
                if (bill != null) { // Đảm bảo object không null
                    String uniqueKey = databaseRef.push().getKey(); // Tạo key duy nhất cho mỗi hóa đơn
                    String lastFourChars = uniqueKey.substring(uniqueKey.length() - 4);
                    bill.setId(lastFourChars);
                    databaseRef.child(uniqueKey).setValue(bill)
                            .addOnSuccessListener(aVoid -> {
                                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Lỗi khi thêm hóa đơn: " + e.getMessage());
                            });
                }
            }
            showToastShort("Thanh toán thành công");
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Medicine medicine = (Medicine) data.getSerializableExtra(Constant.KEY_MEDICINE_EXTRA);
                    Set<Medicine> medicineSet = new HashSet<>();
                    medicineSet.add(medicine);
                    listMedicine.addAll(medicineSet); // Cập nhật lại danh sách
                    Log.e("Bello","listMedicine1: " + listMedicine.size());
                    createBillAdapter.submitList(listMedicine);
                    for (int i = 0 ; i < listMedicine.size() ; i++) {
                        totalSales = totalSales + (listMedicine.get(i).getPrice() * listMedicine.get(i).getInventory());
                    }
                    binding.totalSales.setText(formatPrice(totalSales));
                }
            }

        }
    }

    private void onClickCreateBill() {
        binding.btnAddMedicine.setVisibility(View.GONE);
        binding.btnPayment.setVisibility(View.VISIBLE);
        binding.addMedicine.setVisibility(View.GONE);
        binding.titleCancel.setVisibility(View.VISIBLE);
        binding.titleToolbar.setText(getString(R.string.payment));
    }

    private String formatPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
        String formattedPrice = decimalFormat.format(price);
        return formattedPrice;
    }
}
