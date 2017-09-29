package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivityContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */


//this needs no presenter, because it is a simple fragment that only displays text
public class ArtistDetailsBiographyFragment extends DaggerFragment{
    public static final String TITLE = "Biography";

    @BindView(R.id.artist_details_biography)
    TextView biographyText;

    private String biography;

    private ArtistDetailsActivityContract.View artistDetailsActivity;

    public static ArtistDetailsBiographyFragment newInstance(){
        Bundle args = new Bundle();
        ArtistDetailsBiographyFragment artistDetailsBiographyFragment = new ArtistDetailsBiographyFragment();
        artistDetailsBiographyFragment.setArguments(args);
        return artistDetailsBiographyFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetailsActivity) context;
        this.biography = this.artistDetailsActivity.getArtistBiography();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_details_biography_fragment, container, false);
        ButterKnife.bind(this, view);

        this.biographyText.setText(this.biography);
        return view;
    }
}
