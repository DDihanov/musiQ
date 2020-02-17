package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserInfoUseCase extends BaseUseCase<Void, UserInfo> {
    @Inject
    public GetUserInfoUseCase(
            LastFmApiClient lastFmApiClient,
            SigGenerator sigGenerator,
            UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<UserInfo> getObservable(Void params) {
        String username = userSettingsRepository.getUsername();
        return lastFmApiClient.getLastFmApiService()
                .getUserInfo(username);
    }


    @Override
    protected void onCompleteInner(UserInfo userInfo) {
        String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
        persistProfilePicUrl(profilePicUrl);
    }

    private void persistProfilePicUrl(String profilePicUrl) {
        userSettingsRepository.persistProfilePictureUrl(profilePicUrl);
    }
}
