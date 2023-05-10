package bk.pttkhdt.drugstoremanager.core.base;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

import bk.pttkhdt.drugstoremanager.utils.livedata.SingleLiveEvent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {

    private WeakReference<Context> contextRef;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

//    protected AppPreference appPreference = new AppPreferenceImpl(getContext());
//
//    protected DataManager dataManager = new DataManagerImpl(appPreference);

    SingleLiveEvent<Integer> errorMessage = new SingleLiveEvent<>();

    SingleLiveEvent<String> errorState = new SingleLiveEvent<>();

    SingleLiveEvent<Boolean> loadingState = new SingleLiveEvent<>();


    public void setContext(Activity activity) {
        contextRef = new WeakReference<>(activity);
    }

    public Context getContext() {
        if (contextRef != null) {
            return contextRef.get();
        } else {
            return null;
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void setErrorStringId(Integer errorStringId) {
        errorMessage.setValue(errorStringId);
    }

    public void setErrorString(String errorString) {
        errorState.setValue(errorString);
    }

    public void setLoading(Boolean isLoading) {
        loadingState.setValue(isLoading);
    }
}
