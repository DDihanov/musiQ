package com.dihanov.musiq;

import com.dihanov.musiq.data.network.LastFmApiService;
import com.dihanov.musiq.models.SpecificArtist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dimitar.dihanov on 2/28/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LastFmApiTest {
    @Mock
    LastFmApiService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadSpecificArtist() throws Exception {
        SpecificArtist specificArtist = new SpecificArtist();
        when(service.getSpecificArtistInfo(anyString())).thenReturn(Observable.just(specificArtist));

        Observable<SpecificArtist> actualObservable = service.getSpecificArtistInfo(anyString());
        TestObserver<SpecificArtist> testObserver = actualObservable.test();
        testObserver.assertSubscribed();
        testObserver.assertResult(specificArtist);

        verify(service, times(1)).getSpecificArtistInfo(anyString());
    }
}
