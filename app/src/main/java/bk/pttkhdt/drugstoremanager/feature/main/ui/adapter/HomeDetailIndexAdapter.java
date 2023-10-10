package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


import bk.pttkhdt.drugstoremanager.R;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseRecycleAdapter;
import bk.pttkhdt.drugstoremanager.core.recycleview.BaseViewHolder;
import bk.pttkhdt.drugstoremanager.databinding.ItemBillBinding;
import bk.pttkhdt.drugstoremanager.databinding.ItemSalesBinding;
import bk.pttkhdt.drugstoremanager.utils.Constant;

public class HomeDetailIndexAdapter extends BaseRecycleAdapter<String> {

    boolean stateSelected = false;
    boolean stateSelected1 = false;
    public SalesAdapter salesAdapter= new SalesAdapter();
    public BillAdapter billAdapter = new BillAdapter();

    @Override
    public int getItemViewType(int position) {
        String index = mData.get(position);
        switch (index) {
            case Constant.NODE_SALES: return Constant.VIEW_TYPE_ITEM_SALES;
            case Constant.NODE_BILL: return Constant.VIEW_TYPE_ITEM_BILL;
            default: return Constant.VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constant.VIEW_TYPE_ITEM_SALES: return new SalesViewHolder(
                    ItemSalesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
            case Constant.VIEW_TYPE_ITEM_BILL: return new BillViewHolder(
                    ItemBillBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        return  new BillViewHolder(
                ItemBillBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }


    public class SalesViewHolder extends BaseIndexViewHolder<ItemSalesBinding> {

        public SalesViewHolder(@NonNull ItemSalesBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
            getViewBinding().rcvDetail.setAdapter(salesAdapter);
            getViewBinding().rcvDetail.setLayoutManager(linearLayoutManager);


        }
    }

    public class BillViewHolder extends BaseViewHolder<ItemBillBinding> {

        public BillViewHolder(@NonNull ItemBillBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            String index = mData.get(position);
            getViewBinding().tvIndex.setText(index);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
            getViewBinding().rcvDetail.setAdapter(billAdapter);
            getViewBinding().rcvDetail.setLayoutManager(linearLayoutManager);
            getViewBinding().clState.setOnClickListener(v -> {
                stateSelected1 = !stateSelected1;
                getViewBinding().imgStateSelected.setSelected(stateSelected1);
                if (stateSelected1) {
                    getViewBinding().cvContainer.setVisibility(View.VISIBLE);
                } else {
                    getViewBinding().cvContainer.setVisibility(View.GONE);
                }
            });

        }
    }

    public class BaseIndexViewHolder<T extends ViewBinding> extends BaseViewHolder<T> {

        public BaseIndexViewHolder(@NonNull T viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            String index = mData.get(position);
            TextView tvIndex = getViewBinding().getRoot().findViewById(R.id.tv_index);
            ConstraintLayout clState = getViewBinding().getRoot().findViewById(R.id.cl_state);
            AppCompatImageView imgStateSelected = getViewBinding().getRoot().findViewById(R.id.img_state_selected);
            RecyclerView rcvDetail = getViewBinding().getRoot().findViewById(R.id.rcv_detail);
            tvIndex.setText(index);
            clState.setOnClickListener(v -> {
                stateSelected = !stateSelected;
                imgStateSelected.setSelected(stateSelected);
                if (stateSelected) {
                    rcvDetail.setVisibility(View.VISIBLE);
                } else {
                    rcvDetail.setVisibility(View.GONE);
                }
            });

        }
    }

}
