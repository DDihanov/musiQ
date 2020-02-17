package com.dihanov.musiq.ui.main.mainfragments.nowplaying;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.scrobble.Scrobble;
import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.ui.adapters.RecentlyScrobbledAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindowManager;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.mainfragments.ViewPagerCustomizedFragment;
import com.dihanov.musiq.util.ActivityStarterWithIntentExtras;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlaying extends ViewPagerCustomizedFragment implements NowPlayingContract.View, RecentlyScrobbledAdapter.RecentlyScrobbledItemClickListener {
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
    ScrobbleRepository scrobbleRepository;

    @Inject
    NowPlayingContract.Presenter nowPlayingFragmentPresenter;

    @Inject
    AlbumDetailsPopupWindowManager albumDetailsPopupWindowManager;

    @Inject
    ActivityStarterWithIntentExtras activityStarterWithIntentExtras;

    @Inject
    UserSettingsRepository userSettingsRepository;

    private MainActivity mainActivity;

    public static NowPlaying newInstance() {
        Bundle args = new Bundle();
        NowPlaying nowPlaying = new NowPlaying();
        nowPlaying.setArguments(args);
        return nowPlaying;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = ((MainActivity) context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.now_playing_fragment, container, false);
        ButterKnife.bind(this, view);


        //very important to call this - this enables us to use the below method(onCreateOptionsMenu), and allows us
        //to receive calls from MainActivity's onCreateOptionsMenu
        setHasOptionsMenu(true);

        nowPlayingFragmentPresenter.takeView(this);

        Scrobble nowPlaying = scrobbleRepository.getNowPlaying();
        if (nowPlaying == null) {
            nowPlayingArtistImage.setVisibility(View.GONE);
            nowPlayingArtist.setVisibility(View.GONE);
            nowPlayingAlbum.setVisibility(View.GONE);
            nowPlayingTitle.setVisibility(View.GONE);
            loveTrackImage.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        } else {
            HelperMethods.setLayoutChildrenVisibility(View.VISIBLE, nowPlayingLayout);
            if (nowPlaying.getAlbumArt() != null) {
                Glide.with(nowPlayingArtistImage.getContext()).load(HelperMethods.bitmapToByte(nowPlaying.getAlbumArt())).into(nowPlayingArtistImage);
            } else {
                Glide.with(nowPlayingArtistImage.getContext())
                        .load(getResources()
                                .getIdentifier("ic_missing_image", "drawable", this.getContext().getPackageName()))
                        .into(nowPlayingArtistImage);
            }
            nowPlayingArtist.setText(nowPlaying.getArtistName());
            nowPlayingTitle.setText(nowPlaying.getTrackName());
            nowPlayingAlbum.setText(nowPlaying.getAlbumName());

            RxView.clicks(nowPlayingArtistImage)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(click -> {
                        nowPlayingFragmentPresenter.fetchEntireAlbumInfo(nowPlaying.getArtistName(), nowPlaying.getAlbumName());
                    });

            RxView.clicks(nowPlayingArtist)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(click -> {
                        nowPlayingFragmentPresenter.fetchArtist(nowPlaying.getArtistName());
                    });
        }

        if (userSettingsRepository.hasSessionKey()) {
            nowPlayingFragmentPresenter.loadRecentScrobbles();
        }

        return view;
    }


    @OnClick(R.id.love_track_full)
    void loveTrack(View view) {
        Scrobble nowPlaying = scrobbleRepository.getNowPlaying();
        if (nowPlaying == null || nowPlaying.getArtistName() == null || nowPlaying.getTrackName() == null) {
            return;
        }
        nowPlayingFragmentPresenter.loveTrack(nowPlaying.getArtistName(), nowPlaying.getTrackName());
        RecentlyScrobbledAdapter adapter = (RecentlyScrobbledAdapter) recentTracks.getAdapter();
        if (adapter == null) {
            return;
        }
        adapter.loveNowPlaying(nowPlaying.getArtistName(), nowPlaying.getTrackName());
    }

    @Override
    public void onDestroy() {
        nowPlayingFragmentPresenter.leaveView();
        super.onDestroy();
    }

    @Override
    public View getBirdIcon() {
        return null;
    }

    @Override
    public void showProgressBar() {
        mainActivity.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        mainActivity.hideProgressBar();
    }

    @Override
    public void setRecentTracks(RecentTracksWrapper recentTracksWrapper) {
        if (recentTracksWrapper == null) {
            return;
        }

        if (recentTracksWrapper.getRecenttracks() == null || recentTracksWrapper.getRecenttracks().getTrack() == null) {
            return;
        }

        List<Track> result = recentTracksWrapper.getRecenttracks().getTrack();

        RecentlyScrobbledAdapter adapter =
                new RecentlyScrobbledAdapter(result, requireContext(), this);


        recentTracks.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this.getContext(), GridLayoutManager.VERTICAL, false);
        recentTracks.setLayoutManager(layoutManager);
        recentTracks.getAdapter().notifyDataSetChanged();
    }

    @Override
    public MainActivity getMainActivity() {
        return this.mainActivity;
    }

    @Override
    public void onTrackLoved(Track track) {
        nowPlayingFragmentPresenter.loveTrack(track.getArtist().getName(), track.getName());
    }

    @Override
    public void onTrackUnloved(Track track) {
        nowPlayingFragmentPresenter.unloveTrack(track.getArtist().getName(), track.getName());
    }

    @Override
    public void showToastTrackLoved() {
        Toast.makeText(requireContext(), getString(R.string.track_loved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastTrackUnloved() {
        Toast.makeText(requireContext(), R.string.track_unloved, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startActivityWithExtras(HashMap<String, String> bundleExtra) {
        bundleExtra.put(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        activityStarterWithIntentExtras.startActivityWithExtras(bundleExtra, requireActivity(), ArtistDetails.class);
    }

    @Override
    public void showAlbumDetails(Album fullAlbum) {
        albumDetailsPopupWindowManager.showAlbumDetails(requireActivity(), fullAlbum);
    }
}
