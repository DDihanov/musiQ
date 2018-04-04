package com.dihanov.musiq.ui.settings.profile.user_bio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dihanov.musiq.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class ProfileUserInfo extends DaggerFragment implements ProfileUserInfoContract.View {
    public static final String TITLE = "bio";

    @Inject
    ProfileUserInfoContract.Presenter presenter;

    @BindView(R.id.profile_real_name)
    TextView realNameTextView;

    @BindView(R.id.profile_url)
    TextView profileUrlTextView;

    @BindView(R.id.profile_country)
    TextView countryTextView;

    @BindView(R.id.profile_age)
    TextView ageTextView;

    @BindView(R.id.profile_playcount)
    TextView playcountTextView;

    @BindView(R.id.profile_date)
    TextView dateTextView;

    @BindView(R.id.profile_age_label)
    TextView ageLabel;

    @BindView(R.id.profile_info_progressbar)
    ProgressBar progressBar;

    public static ProfileUserInfo newInstance() {
        Bundle args = new Bundle();
        ProfileUserInfo fragment = new ProfileUserInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_info_layout, container, false);
        ButterKnife.bind(this, view);

        presenter.takeView(this);
        presenter.fetchUserInfo();

        return view;
    }

    @Override
    public void loadUserBio(String realName, String profileUrl, String country, String age, String playcount, String unixRegistrationDate) {
        this.realNameTextView.setText(realName);
        this.profileUrlTextView.setText(profileUrl);
        this.countryTextView.setText(country);
        if(Integer.parseInt(age) <= 0){
            this.ageTextView.setVisibility(View.GONE);
            this.ageLabel.setVisibility(View.GONE);
        } else {
            this.ageTextView.setText(age);
        }
        this.playcountTextView.setText(playcount);
        this.dateTextView.setText(DateUtils.formatDateTime(getContext(), Long.parseLong(unixRegistrationDate) * 1000L, 0));
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.leaveView();
    }
}
