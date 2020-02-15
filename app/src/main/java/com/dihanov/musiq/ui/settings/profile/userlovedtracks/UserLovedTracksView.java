package com.dihanov.musiq.ui.settings.profile.userlovedtracks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.LovedTracksAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import dagger.android.support.DaggerFragment;

public class UserLovedTracksView extends DaggerFragment implements UserLovedTracksContract.View, AbstractAdapter.OnItemClickedListener<Track> {
    public static final String TITLE = "loved tracks";

    @BindView(R.id.loved_tracks_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.loved_tracks_limit_spinner)
    Spinner spinner;

    @BindView(R.id.loved_tracks_recycler_view)
    RecyclerView recyclerView;

    @Inject UserLovedTracksContract.Presenter presenter;

    public static UserLovedTracksView newInstance(){
        Bundle args = new Bundle();
        UserLovedTracksView userLovedTracksView = new UserLovedTracksView();
        userLovedTracksView.setArguments(args);
        return userLovedTracksView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loved_tracks_fragment, container, false);
        ButterKnife.bind(this, view);

        presenter.takeView(this);

        return view;
    }

    @Override
    public void loadLovedTracks(List<Track> lovedTracks) {
        recyclerView.setAdapter(new LovedTracksAdapter(lovedTracks, getContext(), this));
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this.getContext(), GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @OnItemSelected(R.id.loved_tracks_limit_spinner)
    public void onSpinnerItemSelected(Spinner spinner, int position){
        presenter.fetchLovedTracks(Integer.parseInt((String) spinner.getSelectedItem()));
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.INVISIBLE);
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
    public void onDestroy() {
        this.presenter.leaveView();
        super.onDestroy();
    }

    @Override
    public void onItemClicked(Track item) {
        presenter.unloveTrack(item.getArtist().getName(),
                item.getName());
    }
}
