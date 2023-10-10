package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

import bk.pttkhdt.drugstoremanager.core.recycleview.BaseRecycleAdapter;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseViewHolder;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.databinding.ItemCreateBillBinding;

public class CreateBillAdapter extends BaseRecycleAdapter<Medicine> {
    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new CreateBillViewHolder(
                ItemCreateBillBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    public class CreateBillViewHolder extends BaseViewHolder<ItemCreateBillBinding> {

        public CreateBillViewHolder(@NonNull ItemCreateBillBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            Medicine medicine = mData.get(position);
            getViewBinding().headerName.setText(medicine.getName());
            getViewBinding().headerPrice.setText(formatPrice(medicine.getPrice() * medicine.getInventory()));
            getViewBinding().headerQuantity.setText(String.valueOf(medicine.getInventory()));
        }
    }
    private String formatPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(price);
        return formattedPrice;
    }

}
