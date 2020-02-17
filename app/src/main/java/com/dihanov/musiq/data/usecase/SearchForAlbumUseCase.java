package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SearchForAlbumUseCase extends BaseUseCase<String, GeneralAlbumSearch> {
    private static final int LIMIT = 10;

    @Inject
    public SearchForAlbumUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<GeneralAlbumSearch> getObservable(String params) {
        return lastFmApiClient.getLastFmApiService()
                .searchForAlbum(params, LIMIT);
    }
}
