package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
    public static final String TITLE = "biography";

    @BindView(R.id.artist_details_biography)
    TextView biographyTextView;

    private String biography;

    private ArtistDetailsActivityContract.View artistDetailsActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if(savedInstanceState != null){
            this.biography = savedInstanceState.getString(TITLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE, this.biography);
    }

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
        if(this.biography == null){
            this.biography = this.artistDetailsActivity.getArtistBiography();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_details_biography_fragment, container, false);
        ButterKnife.bind(this, view);

        String modifiedBio = formatText(this.biography);
        this.biographyTextView.setText(Html.fromHtml(modifiedBio));
        this.biographyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    private String formatText(String biography) {
        String result = biography;
        result = result.replaceAll("<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.", "\n<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.");
        result = result.replaceAll("\\n", "<br>");
        return result;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

}
