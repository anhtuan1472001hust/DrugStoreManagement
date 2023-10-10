package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

import bk.pttkhdt.drugstoremanager.core.recycleview.BaseRecycleAdapter;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseViewHolder;
import bk.pttkhdt.drugstoremanager.data.model.Bill;
import bk.pttkhdt.drugstoremanager.databinding.ItemDetailBillBinding;

public class BillAdapter extends BaseRecycleAdapter<Bill> {

    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BillViewHolder(
                ItemDetailBillBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    public class BillViewHolder extends BaseViewHolder<ItemDetailBillBinding> {

        public BillViewHolder(@NonNull ItemDetailBillBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            Bill bill = mData.get(position);
            getViewBinding().headerQuantity.setText(String.valueOf(bill.getQuantity()));
            getViewBinding().headerId.setText(String.valueOf(bill.getId()));
            getViewBinding().headerPrice.setText(formatPrice(bill.getPrice()));
            getViewBinding().headerName.setText(bill.getMedicineName());
        }
    }

    private String formatPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(price);
        return formattedPrice;
    }
}
