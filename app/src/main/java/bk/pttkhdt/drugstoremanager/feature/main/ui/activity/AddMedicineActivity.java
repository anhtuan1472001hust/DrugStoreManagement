package bk.pttkhdt.drugstoremanager.feature.main.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.databinding.ActivityAddMedicineBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.AutoCompleteTvAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class AddMedicineActivity extends BaseActivity<ActivityAddMedicineBinding, MainViewModel> {

    private AutoCompleteTvAdapter autoCompleteTvAdapter;

    List<String> listMedicineName = new ArrayList<>();

    Medicine medicine = new Medicine();

    @Override
    protected ActivityAddMedicineBinding getViewBinding() {
        return ActivityAddMedicineBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.listMedicine().observe(this, listMedicine -> {
            for (int i = 0; i < listMedicine.size(); i++) {
                listMedicineName.add(listMedicine.get(i).getName());
            }
            autoCompleteTvAdapter.submitList(listMedicineName);
        });
    }

    @Override
    public void onCommonViewLoaded() {
        autoCompleteTvAdapter = new AutoCompleteTvAdapter(this);
        viewModel.getListMedicine();
        binding.btnAddMedicine.setEnabled(false);
        binding.btnAddMedicine.setAlpha(0.5F);
        binding.edtSearchMedicine.setAdapter(autoCompleteTvAdapter);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding.titleToolbar.setText(getString(R.string.add_medicine));
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean allEditTextFilled = binding.edtSearchMedicine.getText().toString().length() > 0
                        && binding.quantity.getText().toString().length() > 0
                        && binding.price.getText().toString().length() > 0;
                binding.btnAddMedicine.setEnabled(allEditTextFilled);
                if (allEditTextFilled) {
                    binding.btnAddMedicine.setAlpha(1F);
                } else {
                    binding.btnAddMedicine.setAlpha(0.5F);
                }
            }

        };

        binding.edtSearchMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String medicineName = binding.edtSearchMedicine.getText().toString().trim();
                medicine.setName(medicineName);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference medicinesRef = database.getReference("medicines");

                // Tìm thuốc có tên giống với medicine.getName()
                medicinesRef.orderByChild("name").equalTo(medicine.getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean found = false;

                                for (DataSnapshot child : snapshot.getChildren()) {
                                    // Lấy inventory hiện tại của thuốc từ Firebase
                                    Integer price = child.child("price").getValue(Integer.class);
                                    Log.e("Bello","price: " + price);
                                    if (price != null) {
                                        binding.price.setText(Integer.toString(price));
                                    }
                                }

                                if (!found) {
                                    System.out.println("Không tìm thấy thuốc với tên: " + medicine.getName());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                System.out.println("Lỗi khi truy cập dữ liệu: " + error.getMessage());
                            }
                        });
                if (editable.length() == 0) {
                    autoCompleteTvAdapter.clearList();
                    autoCompleteTvAdapter.submitList(listMedicineName);
                }
            }
        });
        binding.edtSearchMedicine.addTextChangedListener(textWatcher);
        binding.quantity.addTextChangedListener(textWatcher);
        binding.price.addTextChangedListener(textWatcher);


    }

    @Override
    public void addViewListener() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnAddMedicine.setOnClickListener(v -> onClickAddMedicine());

    }

    private void onClickAddMedicine() {
        String medicineName = binding.edtSearchMedicine.getText().toString().trim();
        String quantity = binding.quantity.getText().toString().trim();
        String price = binding.price.getText().toString().trim();
        medicine.setName(medicineName);
        medicine.setPrice(Long.parseLong(price));
        medicine.setInventory(Long.parseLong(quantity));
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_MEDICINE_EXTRA, medicine);
        setResult(RESULT_OK, intent);
        showToastShort(getString(R.string.add_medicine_to_sell));
        finish();
    }
}
