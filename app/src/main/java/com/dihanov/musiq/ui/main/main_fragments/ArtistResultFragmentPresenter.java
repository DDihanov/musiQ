package com.dihanov.musiq.ui.main.main_fragments;

import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.ui.main.MainActivityContract;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultFragmentPresenter implements ArtistResultFragmentContract.Presenter {
    @Override
    public void takeView(MainActivityContract.View view) {

    }

    @Override
    public void leaveView() {

    }

    @Override
    public void addOnArtistResultClickedListener(final RecyclerView recyclerView) {
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
