package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.util.SigGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DeserializeFavoriteAlbumsUseCase extends BaseUseCase<Set<String>, List<Album>> {

    @Inject
    public DeserializeFavoriteAlbumsUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    public void invoke(ResultCallback<List<Album>> resultCallback, Set<String> favorites) {
        List<Album> deserializedList = new ArrayList<>();

        for (String serializedAlbum : favorites) {
            String actualValue = serializedAlbum.split(" \\_\\$\\_ ")[1];
            Album album = new Gson().fromJson(actualValue, Album.class);
            deserializedList.add(album);
        }

        Single.just(deserializedList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Album>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        resultCallback.onStart();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Album> albums) {
                        resultCallback.onSuccess(albums);
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        resultCallback.onError(e);
                        compositeDisposable.clear();
                    }
                });
    }

    //no API calls in this class
    @Override
    protected Observable<List<Album>> getObservable(Set<String> params) {
        return null;
    }
}
