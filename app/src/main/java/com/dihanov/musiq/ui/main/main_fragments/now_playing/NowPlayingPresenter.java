package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.ui.adapters.RecentlyScrobbledAdapter;
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

public class NowPlayingPresenter implements NowPlayingContract.Presenter {
    private static final int RECENT_SCROBBLES_LIMIT = 20;
    private static final String TAG = NowPlayingPresenter.class.getSimpleName();

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NowPlayingContract.View nowPlayingFragment;

    @Inject
    public NowPlayingPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }


    @Override
    public void takeView(NowPlayingContract.View view) {
        this.nowPlayingFragment = view;
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
                        Log.d(NowPlayingPresenter.class.getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void loadRecentScrobbles(NowPlayingContract.View nowPlayingFragment) {
        RecyclerView recyclerView = nowPlayingFragment.getRecyclerView();
        lastFmApiClient.getLastFmApiService()
                .getUserRecentTracks(App.getSharedPreferences().getString(Constants.USERNAME, ""), RECENT_SCROBBLES_LIMIT, 1)
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

                        RecentlyScrobbledAdapter adapter =
                                new RecentlyScrobbledAdapter(result);

                        recyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(nowPlayingFragment.getContext(), GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.getAdapter().notifyDataSetChanged();
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
