package com.dihanov.musiq.ui.settings.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserInfo extends Fragment {
    public static final String TITLE = "bio";

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

    private static User userInfo;
    private String realName;
    private String profileUrl;
    private String country;
    private String age;
    private String playcount;
    private String unixRegistrationDate;

    public static ProfileUserInfo newInstance(User user) {
        Bundle args = new Bundle();
        ProfileUserInfo fragment = new ProfileUserInfo();
        fragment.setArguments(args);
        userInfo = user;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.realName = userInfo.getRealname();
        this.profileUrl = userInfo.getUrl();
        this.country = userInfo.getCountry();
        this.age = userInfo.getAge();
        this.playcount = userInfo.getPlaycount();
        this.unixRegistrationDate = userInfo.getRegistered().getUnixtime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_info_layout, container, false);
        ButterKnife.bind(this, view);

        this.realNameTextView.setText(realName);
        this.profileUrlTextView.setText(profileUrl);
        this.countryTextView.setText(country);
        this.ageTextView.setText(age);
        this.playcountTextView.setText(playcount);
        this.dateTextView.setText(DateUtils.formatDateTime(getContext(), Long.parseLong(unixRegistrationDate), 0));

        return view;
    }
}
