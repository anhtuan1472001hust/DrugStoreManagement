package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import bk.pttkhdt.drugstoremanager.core.recycleview.BaseRecycleAdapter;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseViewHolder;
import bk.pttkhdt.drugstoremanager.data.model.Medicine;
import bk.pttkhdt.drugstoremanager.databinding.ItemMedicineBinding;

public class MedicineAdapter extends BaseRecycleAdapter<Medicine> {

    private OnItemClickListener onItemClickListener;

    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new MedicineViewHolder(
                ItemMedicineBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ));
    }


    public class MedicineViewHolder extends BaseViewHolder<ItemMedicineBinding> {

        public MedicineViewHolder(@NonNull ItemMedicineBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            Medicine medicine = mData.get(position);
            long price = medicine.getPrice();
            long inventory = medicine.getInventory();
            getViewBinding().tvMedicine.setText(medicine.getName());
            getViewBinding().tvUses.setText(formatInventory(inventory));
            getViewBinding().tvPrice.setText(formatPrice(price));
            Glide.with(itemView.getContext())
                    .load(medicine.getImageUrl())
                    .into(getViewBinding().imgMedicine);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(medicine);
                }
            });

        }
    }

    private String formatPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        String formattedPrice = decimalFormat.format(price);
        StringBuilder sb = new StringBuilder();
        sb.append("Giá tiền: ").append(formattedPrice);
        return sb.toString();
    }

    private String formatInventory(long inventory) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tồn kho: ").append(inventory).append(" ").append("hộp");
        return sb.toString();
    }


    public interface OnItemClickListener {
        void onItemClick(Medicine medicine);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
