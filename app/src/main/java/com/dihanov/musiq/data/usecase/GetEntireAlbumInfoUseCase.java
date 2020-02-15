package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.AlbumArtistPairModel;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetEntireAlbumInfoUseCase extends BaseUseCase<AlbumArtistPairModel, SpecificAlbum> {
    @Inject
    public GetEntireAlbumInfoUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<SpecificAlbum> getObservable(AlbumArtistPairModel params) {
        return lastFmApiClient.getLastFmApiService()
                .searchForSpecificAlbum(params.getArtistName(), params.getAlbumName());
    }
}
