package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificArtist;
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

public class DeserializeFavoriteArtistsUseCase extends BaseUseCase<Set<String>, List<Artist>> {

    @Inject
    public DeserializeFavoriteArtistsUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    public void invoke(ResultCallback<List<Artist>> resultCallback, Set<String> favorites) {
        List<Artist> deserializedList = new ArrayList<>();

        for (String serializedArtist : favorites) {
            String actualValue = serializedArtist.split(" \\_\\$\\_ ")[1];
            Artist artist;
            try {
                artist = new Gson().fromJson(actualValue, Artist.class);
            } catch (IllegalStateException e) {
                SpecificArtist specificArtist = new Gson().fromJson(actualValue, SpecificArtist.class);
                artist = specificArtist.getArtist();
            }

            deserializedList.add(artist);
        }

        Single.just(deserializedList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        resultCallback.onStart();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Artist> artists) {
                        resultCallback.onSuccess(artists);
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
    protected Observable<List<Artist>> getObservable(Set<String> params) {
        return null;
    }
}
