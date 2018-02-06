package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingFragment extends DaggerFragment implements NowPlayingFragmentContract.View {
    public static final String TITLE = "now playing";

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
        return view;
    }

    @Override
    public void onDestroy() {
        nowPlayingFragmentPresenter.leaveView();
        super.onDestroy();
    }
}
