package bk.pttkhdt.drugstoremanager.utils.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private boolean mPending = false;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (mPending) {
                mPending = false;
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void setValue(T value) {
        mPending = true;
        super.setValue(value);
    }

    public void call() {
        setValue(null);
    }
}
