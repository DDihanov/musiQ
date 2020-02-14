package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dihanov.musiq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */


//this needs no presenter, because it is a simple fragment that only displays text
public class ArtistSpecificsBiography extends ArtistSpecifics {
    public static final String TITLE = "biography";

    @BindView(R.id.biography_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artist_details_biography)
    TextView biographyTextView;

    private String biography;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE, this.biography);
    }

    public static ArtistSpecificsBiography newInstance(){
        Bundle args = new Bundle();
        ArtistSpecificsBiography artistDetailsBiographyFragment = new ArtistSpecificsBiography();
        artistDetailsBiographyFragment.setArguments(args);
        return artistDetailsBiographyFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(this.biography == null){
            this.biography = this.artistDetailsActivity.getArtistBiography();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_details_biography_fragment, container, false);
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        new Thread(() -> {
            String modifiedBio = formatText(biography);
            handler.post(() -> {
                progressBar.setVisibility(View.GONE);
                biographyTextView.setText(Html.fromHtml(modifiedBio));
                biographyTextView.setMovementMethod(LinkMovementMethod.getInstance());
            });
        }).run();

        this.artistDetailsFragmentPresenter.takeView(this);
        return view;
    }

    private String formatText(String biography) {
        String result = biography;
        result = result.replaceAll("<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.", "\n<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.");
        result = result.replaceAll("\\n", "<br>");
        return result;
    }
}
