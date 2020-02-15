package com.dihanov.musiq.data.usecase;

import android.util.Pair;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserTopArtistsUseCase extends BaseUseCase<Pair<String, Integer>, UserTopArtists> {
    @Inject
    public GetUserTopArtistsUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<UserTopArtists> getObservable(Pair<String, Integer> params) {
        String username = userSettingsRepository.getUsername();

        if (username.isEmpty() || username.equals("")) return null;

        return lastFmApiClient.getLastFmApiService()
                .getUserTopArtists(username, params.second, params.first);
    }
}
