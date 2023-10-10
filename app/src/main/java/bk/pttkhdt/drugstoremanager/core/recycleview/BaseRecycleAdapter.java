package bk.pttkhdt.drugstoremanager.core.recycleview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<?>> {

    public List<T> mData = new ArrayList<>();


    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return createViewHolder(layoutInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<T> data) {
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList() {
        mData.clear();
        notifyDataSetChanged();
    }

    public abstract BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);
}
