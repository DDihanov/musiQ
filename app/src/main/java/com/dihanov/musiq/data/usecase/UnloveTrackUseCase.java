package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.LoveUnloveTrackModel;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class UnloveTrackUseCase extends BaseUseCase<LoveUnloveTrackModel, Response> {
    @Inject
    public UnloveTrackUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<Response> getObservable(LoveUnloveTrackModel params) {
        String apiSig = sigGenerator.generateSig(Constants.ARTIST, params.getArtistName(),
                Constants.TRACK, params.getTrackName(),
                Constants.METHOD, Constants.LOVE_TRACK_METHOD);
        return lastFmApiClient.getLastFmApiService()
                .unloveTrack(Constants.LOVE_TRACK_METHOD,
                        params.getArtistName(),
                        params.getTrackName(),
                        Config.API_KEY,
                        apiSig,
                        userSettingsRepository.getUserSessionKey(),
                        Config.FORMAT);
    }
}
