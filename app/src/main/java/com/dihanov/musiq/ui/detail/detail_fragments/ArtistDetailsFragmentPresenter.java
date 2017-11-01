package com.dihanov.musiq.ui.detail.detail_fragments;

import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.util.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistDetailsFragmentPresenter implements ArtistDetailsFragmentContract.Presenter {
    private static final long DELAY_IN_MILLIS = 500;
    private static final String FETCHING_ALBUM_TOOLTIP = "let me fetch that album for you!";

    @Inject LastFmApiClient lastFmApiClient;

    private ArtistDetailsFragment artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArtistDetailsActivity artistDetailsActivity;

    @Inject
    public ArtistDetailsFragmentPresenter() {
    }

    @Override
    public void takeView(ArtistDetailsFragmentContract.View view) {
        this.artistDetailsFragment = (ArtistDetailsFragment) view;
        this.artistDetailsActivity = artistDetailsFragment.getArtistDetailsActivity();
    }

    @Override
    public void leaveView() {
        this.artistDetailsFragment = null;
        compositeDisposable.clear();
    }

    @Override
    public void setClickListenerFetchEntireAlbumInfo(Album album, ArtistDetailsAlbumAdapter.MyViewHolder myViewHolder) {
        final Album[] loadedAlbum = new Album[1];
        RxView.clicks(myViewHolder.thumbnail)
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    lastFmApiClient.getLastFmApiService()
                            .searchForSpecificAlbum(((Artist)album.getArtist()).getName(), album.getName())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(specificAlbum -> specificAlbum)
                            .subscribe(new Observer<SpecificAlbum>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    artistDetailsActivity.showProgressBar();
                                    Constants.showTooltip(artistDetailsActivity, artistDetailsActivity.getBirdIcon(), FETCHING_ALBUM_TOOLTIP);
                                }

                                @Override
                                public void onNext(SpecificAlbum specificAlbum) {
                                    Album fullAlbum = specificAlbum.getAlbum();
                                    loadedAlbum[0] = fullAlbum;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(this.getClass().toString(), e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    artistDetailsActivity.hideProgressbar();
                                    showAlbumDetails(loadedAlbum[0]);
                                }
                            });

                });
    }

    private void showAlbumDetails(Album album) {
        CoordinatorLayout mainLayout = (CoordinatorLayout) artistDetailsActivity.findViewById(R.id.detail_content);

        LayoutInflater inflater = LayoutInflater.from(artistDetailsActivity);
        View albumDetails = inflater.inflate(R.layout.album_popup_info, null);
        TextView tracks = albumDetails.findViewById(R.id.album_popup_tracks);
        TextView title = albumDetails.findViewById(R.id.album_popup_title);
        ImageView cover = albumDetails.findViewById(R.id.album_popup_thumbnail);
        StringBuilder sb = new StringBuilder();
        for (Track track : album.getTracks().getTrack()) {
            long millis = Long.parseLong(track.getDuration());
            String duration = String.format("%d:%d",
                    TimeUnit.SECONDS.toMinutes(millis),
                    TimeUnit.SECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis)));
            sb.append(String.format("%s - %s\n", track.getName(), duration));
        }

        tracks.setText(sb.toString());
        title.setText(album.getName());
        Glide.with(artistDetailsActivity)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(cover);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(albumDetails, width, height, focusable);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        albumDetails.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


}
