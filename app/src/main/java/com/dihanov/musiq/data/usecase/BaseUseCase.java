package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.util.SigGenerator;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @param <P> - input params
 * @param <R> - result type
 */
public abstract class BaseUseCase<P, R> implements UseCase<P, R> {
    protected LastFmApiClient lastFmApiClient;
    protected SigGenerator sigGenerator;
    protected UserSettingsRepository userSettingsRepository;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        this.lastFmApiClient = lastFmApiClient;
        this.sigGenerator = sigGenerator;
        this.userSettingsRepository = userSettingsRepository;
    }

    public void invoke(ResultCallback<R> resultCallback, P params) {
        getObservable(params).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseUseCaseObserver<>(resultCallback));
    }

    //convenience method to override in child classes in case there are some operations to perform on the result
    protected void onCompleteInner(R result) { }

    protected abstract Observable<R> getObservable(P params);

    private class BaseUseCaseObserver<T> implements Observer<T> {
        private ResultCallback<T> resultCallback;

        public BaseUseCaseObserver(ResultCallback<T> resultCallback) {
            this.resultCallback = resultCallback;
        }

        @Override
        public void onSubscribe(Disposable d) {
            resultCallback.onStart();
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(T t) {
            onCompleteInner((R) t);
            resultCallback.onSuccess(t);
        }

        @Override
        public void onError(Throwable e) {
            resultCallback.onError(e);
        }

        @Override
        public void onComplete() {
            compositeDisposable.clear();
        }
    }
}
