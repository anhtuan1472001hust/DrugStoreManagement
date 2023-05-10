package bk.pttkhdt.drugstoremanager.utils.extensions;

import android.os.Build;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.function.Function;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NetWorkExtensions {

    private final CompositeDisposable compositeDisposable;

    public NetWorkExtensions() {
        compositeDisposable = new CompositeDisposable();
    }

    public void checkInternetConnection(Function<Boolean,Void> function) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            compositeDisposable.add(
                    ReactiveNetwork.checkInternetConnectivity()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    function::apply, Throwable::printStackTrace
                            )
            );
        }
    }

    public void disposeCheckInternet() {
        compositeDisposable.clear();
    }

}
