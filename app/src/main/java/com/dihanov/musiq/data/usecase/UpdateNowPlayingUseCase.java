package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.scrobble.Scrobble;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class UpdateNowPlayingUseCase extends BaseUseCase<Scrobble, Response>{
    @Inject
    public UpdateNowPlayingUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<Response> getObservable(Scrobble nowPlaying) {
        String apiSig = sigGenerator.generateSig(Constants.ARTIST, nowPlaying.getArtistName(),
                Constants.TRACK, nowPlaying.getTrackName(),
                Constants.METHOD, Constants.UPDATE_NOW_PLAYING_METHOD);

        return lastFmApiClient.getLastFmApiService()
                .updateNowPlaying(Constants.UPDATE_NOW_PLAYING_METHOD,
                        nowPlaying.getArtistName(),
                        nowPlaying.getTrackName(),
                        Config.API_KEY,
                        apiSig,
                        userSettingsRepository.getUserSessionKey(),
                        Config.FORMAT);
    }
}
