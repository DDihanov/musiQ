package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingFragmentPresenter implements NowPlayingFragmentContract.Presenter {
    private static final int RECENT_SCROBBLES_LIMIT = 20;
    private static final String TAG = NowPlayingFragmentPresenter.class.getSimpleName();

    @Inject
    LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NowPlayingFragment nowPlayingFragment;

    @Inject
    public NowPlayingFragmentPresenter() {
    }


    @Override
    public void takeView(NowPlayingFragmentContract.View view) {
        this.nowPlayingFragment = (NowPlayingFragment) view;
    }

    @Override
    public void leaveView() {
        this.nowPlayingFragment = null;
    }

    @Override
    public void loveTrack(Scrobble scrobble) {
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, scrobble.getArtistName(),
                Constants.TRACK, scrobble.getTrackName(),
                Constants.METHOD, Constants.LOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .loveTrack(Constants.LOVE_TRACK_METHOD,
                        scrobble.getArtistName(),
                        scrobble.getTrackName(),
                        Config.API_KEY,
                        apiSig,
                        App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                        Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response response) {
                        Toast.makeText(nowPlayingFragment.getContext(), R.string.track_loved, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(NowPlayingFragmentPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void loadRecentScrobbles(ListView listView) {
        lastFmApiClient.getLastFmApiService()
                .getUserRecentTracks(App.getSharedPreferences().getString(Constants.USERNAME, ""), RECENT_SCROBBLES_LIMIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecentTracksWrapper>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(RecentTracksWrapper recentTracksWrapper) {
                        List<Track> result = recentTracksWrapper.getRecenttracks().getTrack();

                        String[] trackNames = new String[result.size()];

                        for (int i = 0; i < result.size(); i++) {
                            trackNames[i] = result.get(i).toString();
                        }

                        ArrayAdapter<String> arrayAdapter =
                                new ArrayAdapter<>(nowPlayingFragment.getContext(), android.R.layout.simple_list_item_1, trackNames);

                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }
}
