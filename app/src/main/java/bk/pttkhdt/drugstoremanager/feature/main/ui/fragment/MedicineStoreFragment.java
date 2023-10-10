package bk.pttkhdt.drugstoremanager.feature.main.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import bk.pttkhdt.drugstoremanager.core.base.BaseFragment;
import bk.pttkhdt.drugstoremanager.databinding.FragementMedicineStoreBinding;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.AutoCompleteTvAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.ui.adapter.MedicineAdapter;
import bk.pttkhdt.drugstoremanager.feature.main.viewmodel.MainViewModel;

public class MedicineStoreFragment extends BaseFragment<FragementMedicineStoreBinding, MainViewModel> {

    private MedicineAdapter medicineAdapter = new MedicineAdapter();

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

    private AutoCompleteTvAdapter autoCompleteTvAdapter;

    List<String> listMedicineName = new ArrayList<>();


    @Override
    public void onCommonViewLoaded() {
        float dpValue = 5f;
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dpValue * scale + 0.5f);
        viewBinding.rcvMedicine.setAdapter(medicineAdapter);
        viewBinding.rcvMedicine.setLayoutManager(linearLayoutManager);
        viewBinding.edtSearchMedicine.setAdapter(autoCompleteTvAdapter);
        viewBinding.edtSearchMedicine.setThreshold(1);
        viewBinding.edtSearchMedicine.setFilterTouchesWhenObscured(true);
        viewBinding.edtSearchMedicine.setDropDownVerticalOffset(pixels);
        viewBinding.edtSearchMedicine.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        medicineAdapter.setOnItemClickListener(medicine -> {

        });

    }

    @Override
    public void addViewListener() {

        viewBinding.tvCancel.setOnClickListener(v -> {
            hideKeyboard();
            viewBinding.edtSearchMedicine.setText("");
            viewBinding.tvCancel.setVisibility(View.GONE);
            viewBinding.rcvMedicine.setVisibility(View.VISIBLE);
        });
        viewBinding.edtSearchMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewBinding.tvCancel.setVisibility(View.VISIBLE);
                viewBinding.rcvMedicine.setVisibility(View.GONE);
                if (editable.length() == 0) {
                    autoCompleteTvAdapter.clearList();
                    autoCompleteTvAdapter.submitList(listMedicineName);
                }

            }
        });

    }

    @Override
    protected FragementMedicineStoreBinding getBinding(LayoutInflater inflater) {
        return FragementMedicineStoreBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected String getTagFragment() {
        return MedicineStoreFragment.class.getSimpleName();
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.listMedicine().observe(this, listMedicine -> {
            medicineAdapter.submitList(listMedicine);
            for (int i = 0; i < listMedicine.size(); i++) {
                listMedicineName.add(listMedicine.get(i).getName());
            }
            autoCompleteTvAdapter.submitList(listMedicineName);

        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        autoCompleteTvAdapter = new AutoCompleteTvAdapter(context);
    }
}
