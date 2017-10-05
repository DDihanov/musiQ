package com.dihanov.musiq.ui.main.main_fragments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTags;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private static final String LOADING_ARTIST = "hold on... i'm fetching this artist for you";
    private static final long ARTIST_LOADED_THREAD_TIMEOUT = 2000L;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private MainActivity mainActivity;
    private List<Artist> artistList;
    private LastFmApiClient lastFmApiClient;

    public ArtistAdapter(MainActivity context, List<Artist> albumList, LastFmApiClient lastFmApiClient) {
        this.mainActivity = context;
        this.artistList = albumList;
        this.lastFmApiClient = lastFmApiClient;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.count)
        public TextView count;

        @BindView(R.id.thumbnail)
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (artistList.isEmpty()) {
            return;
        }
        Artist artist = artistList.get(position);

        holder.title.setText(artist.getName().toLowerCase());
        holder.count.setText(Constants.formatNumberWithSeperator(artist.getListeners()) + " listeners");

        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(artist.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.thumbnail);

        RxView.clicks(holder.thumbnail)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    int pos = holder.getLayoutPosition();
                    showArtistDetails(artistList.get(pos).getName());
                });
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }

    private void showArtistDetails(String nameToFetch) {
        Intent showArtistDetailsIntent = new Intent(mainActivity, ArtistDetailsActivity.class);
        showArtistDetailsIntent.putExtra(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        Constants.showTooltip(mainActivity, mainActivity.getBirdIcon(), LOADING_ARTIST);
        mainActivity.showProgressBar();

        Observable<ArtistTopTags> topArtistTags = lastFmApiClient.getLastFmApiService()
                .getArtistTopTags(nameToFetch).subscribeOn(Schedulers.io());
        Observable<SpecificArtist> specificArtistRequest = lastFmApiClient.getLastFmApiService()
                .getSpecificArtistInfo(nameToFetch)
                .subscribeOn(Schedulers.io());
        Observable<TopArtistAlbums> topAlbumRequest = lastFmApiClient.getLastFmApiService()
                .searchForArtistTopAlbums(nameToFetch, Constants.ALBUM_LIMIT)
                .subscribeOn(Schedulers.newThread());

        Observable.zip(specificArtistRequest,
                topAlbumRequest,
                topArtistTags,
                new Function3<SpecificArtist, TopArtistAlbums, ArtistTopTags, HashMap<String, String>>() {
                    @Override
                    public HashMap<String, String> apply(SpecificArtist specificArtist, TopArtistAlbums topArtistAlbums, ArtistTopTags artistTopTags) throws Exception {
                        HashMap<String, String> result = new HashMap<>();

                        result.put(Constants.ARTIST, new Gson().toJson(specificArtist.getArtist()));
                        result.put(Constants.ALBUM, new Gson().toJson(topArtistAlbums.getTopalbums().getAlbum()));
                        result.put(Constants.TAGS, new Gson().toJson(artistTopTags.getToptags().getTag()));

                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(HashMap<String, String> stringStringHashMap) {
                        for (Map.Entry<String, String> kvp : stringStringHashMap.entrySet()) {
                            showArtistDetailsIntent.putExtra(kvp.getKey(), kvp.getValue());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ArtistAdapter.class.toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        //important we use .clear and not .dispose, since .dispose will not allow any further subscribing
                        disposable.clear();
                        mainActivity.hideProgressBar();
                        mainActivity.startActivity(showArtistDetailsIntent);
                    }
                });
    }
}
