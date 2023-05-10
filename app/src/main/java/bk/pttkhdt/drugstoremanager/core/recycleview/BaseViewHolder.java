package bk.pttkhdt.drugstoremanager.core.recycleview;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    private final VB viewBinding;

    public BaseViewHolder(@NonNull VB viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    public VB getViewBinding() {
        return viewBinding;
    }

    public abstract void bindData(int position);
}
