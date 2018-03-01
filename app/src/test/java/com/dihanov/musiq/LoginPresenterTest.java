package com.dihanov.musiq;

import android.content.Context;
import android.view.ViewGroup;

import com.dihanov.musiq.models.User;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.LastFmApiService;
import com.dihanov.musiq.ui.login.LoginContract;
import com.dihanov.musiq.ui.login.LoginPresenter;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.HelperMethods;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reactivex.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by dimitar.dihanov on 2/28/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PrepareForTest({
        Connectivity.class,
        HelperMethods.class
})
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class LoginPresenterTest extends RxJavaTestRule{
    @Mock
    LoginContract.View view;

    LoginPresenter presenter;

    @Mock
    LastFmApiClient lastFmApiClient;

    @Mock
    LastFmApiService lastFmApiService;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Connectivity.class);
        PowerMockito.mockStatic(HelperMethods.class);

        presenter = Mockito.spy(new LoginPresenter(lastFmApiClient));
        presenter.takeView(view);
    }

    @Test
    public void authenticateUserTest() throws Exception {
        User user = new User();

        when(lastFmApiClient.getLastFmApiService()).thenReturn(lastFmApiService);
        when(Connectivity.isConnected(any(Context.class))).thenReturn(true);
        PowerMockito.doNothing().when(HelperMethods.class, "setLayoutChildrenEnabled", anyBoolean(), any(ViewGroup.class));
        when(lastFmApiClient.getLastFmApiService().getMobileSessionToken(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(Observable.just(user));
        presenter.authenticateUser("dasd", "131", view, true);


        verify(view).showProgressBar();
    }
}
