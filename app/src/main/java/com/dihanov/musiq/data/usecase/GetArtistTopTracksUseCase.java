package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetArtistTopTracksUseCase extends BaseUseCase<Artist, ArtistTopTracks> {
    private static final int LIMIT = 10;

    @Inject
    public GetArtistTopTracksUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<ArtistTopTracks> getObservable(Artist artist) {
        return lastFmApiClient.getLastFmApiService()
                .getArtistTopTracks(artist.getName(), LIMIT);
    }
}
