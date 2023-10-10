package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bk.pttkhdt.drugstoremanager.core.recycleview.BaseRecycleAdapter;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseViewHolder;
import bk.pttkhdt.drugstoremanager.data.model.Sales;
import bk.pttkhdt.drugstoremanager.databinding.ItemSalesDetailBinding;

public class SalesAdapter extends BaseRecycleAdapter<Sales> {


    private Sales mSales;
    private String mDate;


    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SalesViewHolder(
                ItemSalesDetailBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    public class SalesViewHolder extends BaseViewHolder<ItemSalesDetailBinding> {

        public SalesViewHolder(@NonNull ItemSalesDetailBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(calendar.getTime());
            Sales sales = mData.get(position);
            getViewBinding().tvTotalSales.setText(formatPrice(sales.getTotal()));
            getViewBinding().tvYear.setText(String.valueOf(sales.getDate()));

        }
    }

        private String formatPrice(long price) {
            DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
            String formattedPrice = decimalFormat.format(price);
            StringBuilder sb = new StringBuilder();
            sb.append("Doanh số: ").append(formattedPrice);
            return sb.toString();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void changeSales(Sales sales) {
            for (int index = 0; index < mData.size(); index++) {
                Sales sales1 = mData.get(index);
                    if (sales.getDate().equals(sales1.getDate())) {
                        mData.set(index, sales);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        }


