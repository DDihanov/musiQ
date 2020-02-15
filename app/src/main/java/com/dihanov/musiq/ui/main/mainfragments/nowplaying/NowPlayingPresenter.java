package com.dihanov.musiq.ui.main.mainfragments.nowplaying;

import com.dihanov.musiq.data.usecase.FetchArtistWithSerializedInfoUseCase;
import com.dihanov.musiq.data.usecase.GetEntireAlbumInfoUseCase;
import com.dihanov.musiq.data.usecase.GetRecentScrobblesUseCase;
import com.dihanov.musiq.data.usecase.LoveTrackUseCase;
import com.dihanov.musiq.data.usecase.UnloveTrackUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.AlbumArtistPairModel;
import com.dihanov.musiq.models.LoveUnloveTrackModel;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.SpecificAlbum;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingPresenter implements NowPlayingContract.Presenter {
    private NowPlayingContract.View nowPlayingFragment;
    private GetRecentScrobblesUseCase getRecentScrobblesUseCase;
    private LoveTrackUseCase loveTrackUseCase;
    private UnloveTrackUseCase unloveTrackUseCase;
    private GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase;
    private FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase;

    private UseCase.ResultCallback<RecentTracksWrapper> recentTracksWrapperResultCallback =
            new UseCase.ResultCallback<RecentTracksWrapper>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(RecentTracksWrapper response) {
                    if (nowPlayingFragment != null) {
                        nowPlayingFragment.setRecentTracks(response);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }
            };

    private UseCase.ResultCallback<Response> loveTrackCallback = new UseCase.ResultCallback<Response>() {

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(Response response) {
            if(response != null){
                nowPlayingFragment.showToastTrackLoved();
            }
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    private UseCase.ResultCallback<Response> unloveTrackCallback = new UseCase.ResultCallback<Response>() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(Response response) {
            if (response != null) {
                nowPlayingFragment.showToastTrackUnloved();
            }
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private UseCase.ResultCallback<SpecificAlbum> specificAlbumResultCallback = new UseCase.ResultCallback<SpecificAlbum>() {
        @Override
        public void onStart() {
            nowPlayingFragment.showProgressBar();
        }

        @Override
        public void onSuccess(SpecificAlbum specificAlbum) {
            Album fullAlbum = specificAlbum.getAlbum();
            nowPlayingFragment.showAlbumDetails(fullAlbum);
            nowPlayingFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            nowPlayingFragment.hideProgressBar();
        }
    };

    private UseCase.ResultCallback<HashMap<String, String>> serializedArtistCallback = new UseCase.ResultCallback<HashMap<String, String>>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(HashMap<String, String> response) {
            nowPlayingFragment.hideProgressBar();
            nowPlayingFragment.startActivityWithExtras(response);
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    @Inject
    public NowPlayingPresenter(FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase, GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase, GetRecentScrobblesUseCase getRecentScrobblesUseCase, UnloveTrackUseCase unloveTrackUseCase, LoveTrackUseCase loveTrackUseCase) {
        this.fetchArtistWithSerializedInfoUseCase = fetchArtistWithSerializedInfoUseCase;
        this.getEntireAlbumInfoUseCase = getEntireAlbumInfoUseCase;
        this.unloveTrackUseCase = unloveTrackUseCase;
        this.loveTrackUseCase = loveTrackUseCase;
        this.getRecentScrobblesUseCase = getRecentScrobblesUseCase;
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
    public void loadRecentScrobbles() {
         getRecentScrobblesUseCase.invoke(recentTracksWrapperResultCallback, null);
    }

    @Override
    public void loveTrack(String artistName, String trackName) {
        loveTrackUseCase.invoke(loveTrackCallback, new LoveUnloveTrackModel(artistName, trackName));
    }

    @Override
    public void unloveTrack(String artistName, String trackName) {
        unloveTrackUseCase.invoke(unloveTrackCallback, new LoveUnloveTrackModel(artistName, trackName));
    }

    @Override
    public void fetchArtist(String artistName) {
       fetchArtistWithSerializedInfoUseCase.invoke(serializedArtistCallback, artistName);
    }

    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
        getEntireAlbumInfoUseCase.invoke(specificAlbumResultCallback, new AlbumArtistPairModel(artistName, albumName));
    }
}


