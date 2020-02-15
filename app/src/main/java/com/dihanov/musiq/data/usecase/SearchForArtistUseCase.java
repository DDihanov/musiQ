package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SearchForArtistUseCase extends BaseUseCase<String, ArtistSearchResults> {
    private static final int LIMIT = 20;

    @Inject
    public SearchForArtistUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<ArtistSearchResults> getObservable(String params) {
        return lastFmApiClient.getLastFmApiService()
                .searchForArtist(params, LIMIT);
    }
}
