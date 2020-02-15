package com.dihanov.musiq.ui.settings.profile.userlovedtracks;

import com.dihanov.musiq.data.usecase.GetUserLovedTracksUseCase;
import com.dihanov.musiq.data.usecase.LoveTrackUseCase;
import com.dihanov.musiq.data.usecase.UnloveTrackUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.LoveUnloveTrackModel;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.UserLovedTracks;

import javax.inject.Inject;

public class UserLovedTracksPresenter implements UserLovedTracksContract.Presenter {
    private UserLovedTracksContract.View view;
    private GetUserLovedTracksUseCase getUserLovedTracksUseCase;
    private LoveTrackUseCase loveTrackUseCase;
    private UnloveTrackUseCase unloveTrackUseCase;
    private UseCase.ResultCallback<UserLovedTracks> getUserLovedTracksResultCallback = new UseCase.ResultCallback<UserLovedTracks>() {
        @Override
        public void onStart() {
            if (view != null) {
                view.showProgressBar();
            }
        }

        @Override
        public void onSuccess(UserLovedTracks userLovedTracks) {
            if (userLovedTracks == null ||
                    userLovedTracks.getLovedtracks() == null ||
                    userLovedTracks.getLovedtracks().getTrack() == null
                    || view == null) {
                return;
            }
            view.loadLovedTracks(userLovedTracks.getLovedtracks().getTrack());
            if (view != null) {
                view.hideProgressBar();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (view != null) {
                view.showProgressBar();
            }
        }
    };
    private UseCase.ResultCallback<Response> loveTrackCallback = new UseCase.ResultCallback<Response>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(Response response) {
            if (response != null) {
                view.showToastTrackLoved();
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
                view.showToastTrackUnloved();
            }
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    @Inject
    public UserLovedTracksPresenter(GetUserLovedTracksUseCase getUserLovedTracksUseCase, LoveTrackUseCase loveTrackUseCase, UnloveTrackUseCase unloveTrackUseCase) {
        this.loveTrackUseCase = loveTrackUseCase;
        this.unloveTrackUseCase = unloveTrackUseCase;
        this.getUserLovedTracksUseCase = getUserLovedTracksUseCase;
    }

    @Override
    public void fetchLovedTracks(int limit) {
        getUserLovedTracksUseCase.invoke(getUserLovedTracksResultCallback, limit);
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
    public void takeView(UserLovedTracksContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }


}
