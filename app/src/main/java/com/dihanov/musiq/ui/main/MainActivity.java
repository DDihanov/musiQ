package com.dihanov.musiq.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.util.KeyboardHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DaggerActivity;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerActivity implements MainActivityContract.View {
    @Inject
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.search)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainActivityPresenter.addOnAutoCompleteTextViewItemClickedSubscriber(autoCompleteTextView);
        mainActivityPresenter.addOnAutoCompleteTextViewTextChangedObserver(autoCompleteTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.leaveView();
    }


    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideKeyboard() {
        KeyboardHelper.hideKeyboard(this);
    }

    //    public void getArtistExample() {
//        Single<Artist> artistInfo = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfo("Cher");
////        Call<Artist> call = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfoCall("Cher");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        call.enqueue(new Callback<Artist>() {
////            @Override
////            public void onResponse(Call<Artist> call, Response<Artist> response) {
////                Artist artist = response.body();
////                builder.setTitle(artist.getArtist().getName());
////                builder.setMessage(artist.getArtist().getBio().getContent());
////                builder.show();
////                System.out.println();
////            }
////
////            @Override
////            public void onFailure(Call<Artist> call, Throwable t) {
////            }
////        });
//
////        artistInfo
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(artist -> {
////                            builder.setTitle(artist.getArtist().getName());
////                            builder.setMessage(artist.getArtist().getBio().getSummary());
////                            builder.show();});
////    }
}
