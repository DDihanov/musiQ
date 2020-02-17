package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.AuthenticateUserModel;
import com.dihanov.musiq.models.User;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class AuthenticateUserUseCase extends BaseUseCase<AuthenticateUserModel, User>{
    @Inject
    public AuthenticateUserUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<User> getObservable(AuthenticateUserModel params) {
        return lastFmApiClient.getLastFmApiService()
                .getMobileSessionToken(
                        Constants.AUTH_MOBILE_SESSION_METHOD,
                        params.getUsername(),
                        params.getPassword(),
                        Config.API_KEY,
                        sigGenerator.generateAuthSig(params.getUsername(), params.getPassword()),
                        Config.FORMAT);
    }
}
