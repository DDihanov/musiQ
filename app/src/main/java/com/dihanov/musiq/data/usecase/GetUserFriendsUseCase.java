package com.dihanov.musiq.data.usecase;

import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.UserFriends;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserFriendsUseCase extends BaseUseCase<Integer, UserFriends> {
    @Inject
    public GetUserFriendsUseCase(LastFmApiClient lastFmApiClient, SigGenerator sigGenerator, UserSettingsRepository userSettingsRepository) {
        super(lastFmApiClient, sigGenerator, userSettingsRepository);
    }

    @Override
    protected Observable<UserFriends> getObservable(Integer limit) {
        return lastFmApiClient.getLastFmApiService()
                .getUserFriends(userSettingsRepository.getUsername(), 1, limit);
    }
}
