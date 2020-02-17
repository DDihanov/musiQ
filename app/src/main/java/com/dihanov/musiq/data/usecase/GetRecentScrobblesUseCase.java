package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRecentScrobblesUseCase extends BaseUseCase<Void, RecentTracksWrapper> {
    private static final int RECENT_SCROBBLES_LIMIT = 20;

    @Inject
    public GetRecentScrobblesUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<RecentTracksWrapper> getObservable(Void params) {
        return lastFmApiClient.getLastFmApiService()
                .getUserRecentTracks(userSettingsRepository.getUsername(),
                        RECENT_SCROBBLES_LIMIT, 1);

    }
}
