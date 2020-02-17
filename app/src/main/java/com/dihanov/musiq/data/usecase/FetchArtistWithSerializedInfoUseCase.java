package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FetchArtistWithSerializedInfoUseCase extends BaseUseCase<String, HashMap<String, String>> {
    @Inject
    public FetchArtistWithSerializedInfoUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<HashMap<String, String>> getObservable(String artistName) {
        Observable<SpecificArtist> specificArtistRequest = lastFmApiClient.getLastFmApiService()
                .getSpecificArtistInfo(artistName)
                .subscribeOn(Schedulers.io());
        Observable<TopArtistAlbums> topAlbumRequest = lastFmApiClient.getLastFmApiService()
                .searchForArtistTopAlbums(artistName, Constants.ALBUM_LIMIT)
                .subscribeOn(Schedulers.newThread());

        return Observable.zip(
                specificArtistRequest,
                topAlbumRequest,
                (specificArtist, topArtistAlbums) -> {
                    HashMap<String, String> result = new HashMap<>();

                    result.put(Constants.ARTIST, new Gson().toJson(specificArtist, SpecificArtist.class));
                    result.put(Constants.ALBUM, new Gson().toJson(topArtistAlbums, TopArtistAlbums.class));

                    return result;
                });
    }
}
