package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.scrobble.Scrobble;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BulkScrobbleUseCase extends BaseUseCase<List<Scrobble>, Response> {
    @Inject
    public BulkScrobbleUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<Response> getObservable(List<Scrobble> cached) {
        return null;
    }

    @Override
    public void invoke(ResultCallback<Response> resultCallback, List<Scrobble> cached) {
        List<Observable<Response>> observables = new ArrayList<>();
        String apiSig = "";

        for (int i = 0; i < 50 && i < cached.size(); i++) {
            Scrobble scrobble = cached.get(i);
            String artistName = scrobble.getArtistName();
            String trackName = scrobble.getTrackName();
            String timestamp = String.valueOf(scrobble.getTimestamp());
            apiSig = sigGenerator.generateSig(Constants.ARTIST, artistName,
                    Constants.TRACK, trackName,
                    Constants.TIMESTAMP, timestamp,
                    Constants.METHOD, Constants.TRACK_SCROBBLE_METHOD);


            observables.add(lastFmApiClient.getLastFmApiService().scrobbleTrack(Constants.TRACK_SCROBBLE_METHOD,
                    artistName,
                    trackName,
                    Config.API_KEY,
                    apiSig,
                    timestamp,
                    userSettingsRepository.getUserSessionKey(),
                    Config.FORMAT)
                    .subscribeOn(Schedulers.io()));
        }

        Observable.zipIterable(observables, objects -> {
            List<Response> result = new ArrayList<>();
            for (Object object : objects) {
                result.add((Response) object);
            }
            return result;
        }, false, 10)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Response>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Response> responses) {
                        if (responses != null) {
                            for (Response respons : responses) {
                                resultCallback.onSuccess(respons);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }
}
