package com.dihanov.musiq.ui.main;


import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.service.LastFmApiClient;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private static final long DELAY_IN_MILLIS = 500;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    MainActivityContract.View mainActivityView;

    private Disposable disposable;

    private Context context;

    @Inject
    public MainActivityPresenter(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        if(this.disposable != null){
            disposable.dispose();
        }
        this.mainActivityView = null;
    }

    @Override
    public void addOnAutoCompleteTextViewTextChangedObserver(final AutoCompleteTextView autoCompleteTextView) {
        Observable<ArtistSearchResults> autocompleteResponseObservable =
                RxTextView.textChangeEvents(autoCompleteTextView)
                        .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                        .map(new Function<TextViewTextChangeEvent, String>() {
                            @Override
                            public String apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                                return textViewTextChangeEvent.text().toString();
                            }
                        })
                        .filter(s -> s.length() >= 2)
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<String, Observable<ArtistSearchResults>>() {
                            @Override
                            public Observable<ArtistSearchResults> apply(String s) throws Exception {
                                return lastFmApiClient.getLastFmApiService()
                                        .searchForArtist(s, 10);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry();

        autocompleteResponseObservable
                .subscribe(new Observer<ArtistSearchResults>() {
                    private static final String TAG = "ArtistResult";

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArtistSearchResults artistSearchResults) {
                        mainActivityView.showProgressBar();
                        Log.i(TAG, artistSearchResults.toString());

                        List<Artist> result = new ArrayList();
                        for (Artist artist : artistSearchResults.getResults().getArtistmatches().getArtist()) {
                            result.add(artist);
                        }

                        ArrayAdapter<Artist> itemsAdapter = new ArrayAdapter<>
                                (context, android.R.layout.simple_dropdown_item_1line, result);
                        autoCompleteTextView.setAdapter(itemsAdapter);
                        String enteredText = autoCompleteTextView.getText().toString();
                        if (result.size() >= 1 && enteredText.equals(result.get(0).getName())) {
                            autoCompleteTextView.dismissDropDown();
                        } else {
                            autoCompleteTextView.showDropDown();
                        }
                        mainActivityView.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgressBar();
                        disposable.dispose();
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        mainActivityView.hideProgressBar();
                        Log.e(TAG, "onError", e);
                    }
                });
    }

    public void addOnAutoCompleteTextViewItemClickedSubscriber(final AutoCompleteTextView autoCompleteTextView) {
//        Observable<PlaceDetailsResult> adapterViewItemClickEventObservable =
//                RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView)
//
//                        .map(new Func1<AdapterViewItemClickEvent, String>() {
//                            @Override
//                            public String call(AdapterViewItemClickEvent adapterViewItemClickEvent) {
//                                NameAndPlaceId item = (NameAndPlaceId) autoCompleteTextView.getAdapter()
//                                        .getItem(adapterViewItemClickEvent.position());
//                                return item.placeId;
//                            }
//                        })
//                        .observeOn(Schedulers.io())
//                        .flatMap(new Func1<String, Observable<PlaceDetailsResult>>() {
//                            @Override
//                            public Observable<PlaceDetailsResult> call(String placeId) {
//                                return RestClient.INSTANCE.getGooglePlacesClient().details(placeId);
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .retry();
//
//        compositeSubscription.add(adapterViewItemClickEventObservable
//                .subscribe(new Observer<PlaceDetailsResult>() {
//
//                    private static final String TAG = "PlaceDetailsResult";
//
//                    @Override
//                    public void onCompleted() {
//                        Log.i(TAG, "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError", e);
//                    }
//
//                    @Override
//                    public void onNext(PlaceDetailsResult placeDetailsResponse) {
//                        Log.i(TAG, placeDetailsResponse.toString());
//                        updateMap(placeDetailsResponse);
//                    }
//                }));
    }
}
