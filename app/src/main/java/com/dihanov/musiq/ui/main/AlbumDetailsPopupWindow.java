package com.dihanov.musiq.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.models.Tag;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding2.view.RxView;
import com.veinhorn.tagview.TagView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class AlbumDetailsPopupWindow {
    private static final long DELAY_IN_MILLIS = 500;
    private static final String LOADING_ALBUM = "just a sec, let me find this album for you";

    private LastFmApiClient lastFmApiClient;
    private BaseView view;

    public AlbumDetailsPopupWindow(LastFmApiClient lastFmApiClient, BaseView<?> view) {
        this.lastFmApiClient = lastFmApiClient;
        this.view = view;
    }

    public void showPopupWindow(MainViewFunctionable activity, AlbumViewHolder viewHolder, String artistName, String albumName, int mainWindowId) {
        //just a small proof of concept
        final Album[] loadedAlbum = new Album[1];
        RxView.clicks(viewHolder.getThumbnail())
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    lastFmApiClient.getLastFmApiService()
                            .searchForSpecificAlbum(artistName, albumName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(specificAlbum -> specificAlbum)
                            .subscribe(new Observer<SpecificAlbum>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    activity.showProgressBar();
                                    HelperMethods.showTooltip(activity, activity.getBirdIcon(), LOADING_ALBUM);
                                }

                                @Override
                                public void onNext(SpecificAlbum specificAlbum) {
                                    Album fullAlbum = specificAlbum.getAlbum();
                                    //just a small proof of concept
                                    loadedAlbum[0] = fullAlbum;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(this.getClass().toString(), e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    activity.hideProgressBar();
                                    showAlbumDetails((Activity) activity, loadedAlbum[0], mainWindowId);
                                }
                            });

                });
    }

    public void showPopupWindow(MainViewFunctionable activity, View view, String artistName, String albumName, int mainWindowId) {
        //just a small proof of concept
        final Album[] loadedAlbum = new Album[1];
        RxView.clicks(view)
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    lastFmApiClient.getLastFmApiService()
                            .searchForSpecificAlbum(artistName, albumName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(specificAlbum -> specificAlbum)
                            .subscribe(new Observer<SpecificAlbum>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    activity.showProgressBar();
                                    HelperMethods.showTooltip(activity, activity.getBirdIcon(), LOADING_ALBUM);
                                }

                                @Override
                                public void onNext(SpecificAlbum specificAlbum) {
                                    Album fullAlbum = specificAlbum.getAlbum();
                                    //just a small proof of concept
                                    loadedAlbum[0] = fullAlbum;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(this.getClass().toString(), e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    activity.hideProgressBar();
                                    showAlbumDetails((Activity) activity, loadedAlbum[0], mainWindowId);
                                }
                            });

                });
    }

    private void showAlbumDetails(Activity activity, Album album, int mainWindowId) {
        CoordinatorLayout mainLayout = (CoordinatorLayout) activity.findViewById(mainWindowId);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View albumDetails = inflater.inflate(R.layout.album_popup_info, null);
        TextView tracks = albumDetails.findViewById(R.id.album_popup_tracks);
        TextView title = albumDetails.findViewById(R.id.album_popup_title);
        TextView wiki = albumDetails.findViewById(R.id.album_popup_wiki);
        ImageView cover = albumDetails.findViewById(R.id.album_popup_thumbnail);

        StringBuilder sb = new StringBuilder();
        for (Track track : album.getTracks().getTrack()) {
            long millis = Long.parseLong(track.getDuration());
            String duration = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(millis),
                    TimeUnit.SECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis)));
            sb.append(String.format("%s - %s\n", track.getName(), duration));
        }

        initTags(album, albumDetails);
        tracks.setText(sb.toString());
        title.setText(album.getName());


        if (album.getWiki() != null) {
            String toModify = album.getWiki().getSummary();
            String fullWiki = album.getWiki().getContent();
            String modifiedBio = "";
            if (toModify == null) {
                toModify = fullWiki;
                if (toModify != null) {
                    modifiedBio = formatText(toModify);
                }
            } else {
                modifiedBio = formatText(toModify);
            }

            wiki.setText(Html.fromHtml(modifiedBio));
            wiki.setMovementMethod(LinkMovementMethod.getInstance());
        }


        Glide.with(activity)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .apply(new RequestOptions().placeholder(App.getAppContext().getResources()
                        .getIdentifier("ic_missing_image", "drawable", App.getAppContext()
                                .getPackageName())))
                .into(cover);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(albumDetails, width, height, focusable);
        //this fixes a weird bug where the popupwindow can't be closed on some screens
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
    }


    @SuppressLint("ResourceType")
    private void initTags(Album album, View rootLayout) {
        TagView firstTag = (TagView) rootLayout.findViewById(R.id.popup_first_tag);
        TagView secondTag = (TagView) rootLayout.findViewById(R.id.popup_second_tag);
        TagView thirdTag = (TagView) rootLayout.findViewById(R.id.popup_third_tag);
        TagView fourthTag = (TagView) rootLayout.findViewById(R.id.popup_fourth_tag);
        TagView fifthTag = (TagView) rootLayout.findViewById(R.id.popup_fifth_tag);

        TagView[] tags = new TagView[]{
                firstTag, secondTag, thirdTag, fourthTag, fifthTag
        };


        List<Tag> tagText = album.getTags().getTag();

        if (tagText.isEmpty()) {
            return;
        }

        List<String> firstFive = new ArrayList<String>() {
            {
                for (int i = 0; i < tagText.size(); i++) {
                    this.add(tagText.get(i).getName());
                }
            }
        };

        //sorted by tag name length
        Collections.sort(firstFive, new Comparator<String>() {

            @Override
            public int compare(String s, String t1) {
                return Integer.compare(t1.length(), s.length());
            }
        });

        for (int i = 0; i < tagText.size(); i++) {
            TagView currTag = tags[i];
            currTag.setText(firstFive.get(i));
            currTag.setTagColor(Color.parseColor(((Activity) view).getString(R.color.colorAccent)));
        }
    }

    private String formatText(String biography) {
        String result = biography;
        result = result.replaceAll("<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.", "\n<a href=\"https://www.last.fm/music/Red\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.");
        result = result.replaceAll("\\n", "<br>");
        return result;
    }
}
