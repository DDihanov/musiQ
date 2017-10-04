package com.dihanov.musiq.ui.detail.detail_fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistDetailsAlbumAdapter extends RecyclerView.Adapter<ArtistDetailsAlbumAdapter.MyViewHolder> {
    private Disposable disposable;
    private ArtistDetailsActivity mainActivity;
    private List<Album> artistAlbumsList;
    private LastFmApiClient lastFmApiClient;

    public ArtistDetailsAlbumAdapter(ArtistDetailsActivity context, List<Album> artistAlbumsList, LastFmApiClient lastFmApiClient) {
        this.mainActivity = context;
        this.artistAlbumsList = artistAlbumsList;
        this.lastFmApiClient = lastFmApiClient;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_title)
        public TextView title;

        @BindView(R.id.album_song_count)
        public TextView count;

        @BindView(R.id.album_thumbnail)
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_artist_albums, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (artistAlbumsList.isEmpty()) {
            return;
        }
        Album album = artistAlbumsList.get(position);
        double playcount = (double)album.getPlaycount();

        holder.title.setText(album.getName().toLowerCase());
        holder.count.setText(String.valueOf((int)playcount) + "plays");

        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.thumbnail);

//        RxView.clicks(holder.thumbnail)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(click -> {
//                    int pos = holder.getLayoutPosition();
//                    showAlbumDetails(artistAlbumsList.get(pos).getName());
//                });
    }

    @Override
    public int getItemCount() {
        return this.artistAlbumsList.size();
    }

//    private void showAlbumDetails(String nameToFetch) {
//        Intent showArtistAlbumsIntent = new Intent(mainActivity, ArtistDetailsActivityAlbum.class);
//
//        lastFmApiClient.getLastFmApiService().getSpecificAlbumInfo(nameToFetch)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<SpecificArtist>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mainActivity.showProgressBar();
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onSuccess(SpecificAlbum specificAlbum) {
//                        mainActivity.hideProgressBar();
//                        disposable.dispose();
//                        showArtistAlbumsIntent.putExtra(Constants.ARTIST, new Gson().toJson(specificAlbum.getAlbum()));
//                        mainActivity.startActivity(showArtistAlbumsIntent);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(ArtistDetailsAlbumAdapter.class.toString(), e.getMessage());
//                    }
//                });
//    }

}
