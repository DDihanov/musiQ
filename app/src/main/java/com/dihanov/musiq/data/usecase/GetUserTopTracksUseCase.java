package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.UserTopTracks;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserTopTracksUseCase extends BaseUseCase<String, UserTopTracks> {
    private static int LIMIT = 10;

    @Inject
    public GetUserTopTracksUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<UserTopTracks> getObservable(String timeframe) {
        String username = userSettingsRepository.getUsername();

        if (username.isEmpty() || username.equals("")) return null;

        return lastFmApiClient.getLastFmApiService()
                .getUserTopTracks(username, LIMIT, timeframe);
    }
}
