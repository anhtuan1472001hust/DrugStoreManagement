package bk.pttkhdt.drugstoremanager.feature.main.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.base.BaseActivity;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.databinding.ActivityAddMedicineBinding;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class AddMedicineActivity extends BaseActivity<ActivityAddMedicineBinding, MainViewModel> {

    @Override
    protected ActivityAddMedicineBinding getViewBinding() {
        return ActivityAddMedicineBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.btnAddMedicine.setEnabled(false);
        binding.btnAddMedicine.setAlpha(0.5F);
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
                boolean allEditTextFilled = binding.edtMedicineName.getText().toString().length() > 0
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
        binding.edtMedicineName.addTextChangedListener(textWatcher);
        binding.quantity.addTextChangedListener(textWatcher);
        binding.price.addTextChangedListener(textWatcher);


    }

    @Override
    public void addViewListener() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnAddMedicine.setOnClickListener(v -> onClickAddMedicine());

    }

    private void onClickAddMedicine() {
        String medicineName = binding.edtMedicineName.getText().toString().trim();
        String quantity = binding.quantity.getText().toString().trim();
        String price = binding.price.getText().toString().trim();
        Medicine medicine = new Medicine();
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
