package com.dihanov.musiq.data.usecase;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

public interface UseCase<P, R> {
    void invoke(ResultCallback<R> resultCallback, P params);

    interface ResultCallback<R> {
        @UiThread
        void onStart();

        @WorkerThread
        void onSuccess(R response);

        @UiThread
        void onError(Throwable e);
    }
}
