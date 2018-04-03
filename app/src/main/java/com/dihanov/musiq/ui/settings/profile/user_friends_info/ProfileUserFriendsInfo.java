package com.dihanov.musiq.ui.settings.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Friends;
import com.dihanov.musiq.ui.adapters.ProfileFriendsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserFriendsInfo extends Fragment {
    public static final String TITLE = "friends";

    private static Friends friends;

    @BindView(R.id.profile_friends_recycler_view)
    RecyclerView friendRecyclerView;

    public static ProfileUserFriendsInfo newInstance(Friends userFriends) {
        Bundle args = new Bundle();
        ProfileUserFriendsInfo fragment = new ProfileUserFriendsInfo();
        fragment.setArguments(args);
        friends = userFriends;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_friends_layout, container, false);
        ButterKnife.bind(this, view);

        friendRecyclerView.setAdapter(new ProfileFriendsAdapter(getContext(), friends.getUser()));
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this.getContext(), 2,GridLayoutManager.VERTICAL, false);
        friendRecyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
