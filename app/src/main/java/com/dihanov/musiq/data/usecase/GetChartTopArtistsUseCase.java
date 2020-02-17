package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.util.SigGenerator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetChartTopArtistsUseCase extends BaseUseCase<Integer, List<Artist>> {
    @Inject
    public GetChartTopArtistsUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<List<Artist>> getObservable(Integer limit) {
        return lastFmApiClient.getLastFmApiService().chartTopArtists(limit)
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches());
    }
}
