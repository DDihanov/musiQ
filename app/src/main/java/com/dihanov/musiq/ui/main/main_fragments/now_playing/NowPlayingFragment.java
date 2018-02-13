package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.service.scrobble.Scrobbler;
import com.dihanov.musiq.util.HelperMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingFragment extends DaggerFragment implements NowPlayingFragmentContract.View {
    public static final String TITLE = "now playing";

    @BindView(R.id.now_playing_fragment_layout)
    RelativeLayout nowPlayingLayout;

    @BindView(R.id.now_playing_art)
    ImageView nowPlayingArtistImage;

    @BindView(R.id.now_playing_artist_cardview)
    CardView cardView;

    @BindView(R.id.now_playing_album)
    TextView nowPlayingAlbum;

    @BindView(R.id.now_playing_title)
    TextView nowPlayingTitle;

    @BindView(R.id.now_playing_artist)
    TextView nowPlayingArtist;

    @BindView(R.id.love_track_full)
    ImageView loveTrackImage;

    @BindView(R.id.recently_scrobbled_recyclerview)
    RecyclerView recentTracks;

    @Inject
    Scrobbler scrobbler;

    @Inject
    NowPlayingFragmentPresenter nowPlayingFragmentPresenter;

    public static NowPlayingFragment newInstance() {
        Bundle args = new Bundle();
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        nowPlayingFragment.setArguments(args);
        return nowPlayingFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.now_playing_fragment, container, false);
        ButterKnife.bind(this, view);

        nowPlayingFragmentPresenter.takeView(this);

        Scrobble nowPlaying = scrobbler.getNowPlaying();
        if(nowPlaying == null){
            nowPlayingArtistImage.setVisibility(View.GONE);
            nowPlayingArtist.setVisibility(View.GONE);
            nowPlayingAlbum.setVisibility(View.GONE);
            nowPlayingTitle.setVisibility(View.GONE);
            loveTrackImage.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        } else {
            HelperMethods.setLayoutChildrenVisibility(View.VISIBLE, nowPlayingLayout);
            if(nowPlaying.getAlbumArt() != null){
                Glide.with(nowPlayingArtistImage.getContext()).load(HelperMethods.bitmapToByte(nowPlaying.getAlbumArt())).asBitmap().into(nowPlayingArtistImage);
            } else {
                Glide.with(nowPlayingArtistImage.getContext())
                        .load(getResources()
                                .getIdentifier("ic_missing_image", "drawable", this.getContext().getPackageName()))
                        .into(nowPlayingArtistImage);
            }
            nowPlayingArtist.setText(nowPlaying.getArtistName());
            nowPlayingTitle.setText(nowPlaying.getTrackName());
            nowPlayingAlbum.setText(nowPlaying.getAlbumName());
        }

        nowPlayingFragmentPresenter.loadRecentScrobbles(recentTracks);

        return view;
    }

    @OnClick(R.id.love_track_full)
    void loveTrack(View view){
        nowPlayingFragmentPresenter.loveTrack(scrobbler.getNowPlaying());
    }

    @Override
    public void onDestroy() {
        nowPlayingFragmentPresenter.leaveView();
        super.onDestroy();
    }
}
