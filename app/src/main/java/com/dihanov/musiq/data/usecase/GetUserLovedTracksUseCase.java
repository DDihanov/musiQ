package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.UserLovedTracks;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserLovedTracksUseCase extends BaseUseCase<Integer, UserLovedTracks> {
    @Inject
    public GetUserLovedTracksUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<UserLovedTracks> getObservable(Integer limit) {
        return lastFmApiClient.getLastFmApiService()
                .getUserLovedTracks(userSettingsRepository.getUsername(), limit);
    }
}
