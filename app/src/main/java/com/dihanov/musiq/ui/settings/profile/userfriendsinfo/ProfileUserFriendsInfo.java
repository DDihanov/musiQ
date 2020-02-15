package com.dihanov.musiq.ui.settings.profile.userfriendsinfo;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.User;
import com.dihanov.musiq.ui.adapters.ProfileFriendsAdapter;
import com.dihanov.musiq.ui.settings.profile.Profile;
import com.dihanov.musiq.util.KeyboardHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import dagger.android.support.DaggerFragment;

public class ProfileUserFriendsInfo extends DaggerFragment implements ProfileUserFriendsContract.View {
    public static final String TITLE = "friends";

    @Inject
    ProfileUserFriendsContract.Presenter presenter;

    @BindView(R.id.profile_friends_recycler_view)
    RecyclerView friendRecyclerView;

    @BindView(R.id.friends_limit_spinner)
    Spinner friendsLimitSpinner;

    @BindView(R.id.profile_friends_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.profile_friends_search)
    EditText friendsSearchBar;

    public static ProfileUserFriendsInfo newInstance() {
        Bundle args = new Bundle();
        ProfileUserFriendsInfo fragment = new ProfileUserFriendsInfo();
        fragment.setArguments(args);
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

        presenter.takeView(this);
        friendRecyclerView.setAdapter(new ProfileFriendsAdapter(getContext(), new ArrayList<>()));

        return view;
    }

    @Override
    public void loadFriends(List<User> friends) {
        friendRecyclerView.setAdapter(new ProfileFriendsAdapter(getContext(), friends));
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this.getContext(), 2,GridLayoutManager.VERTICAL, false);
        friendRecyclerView.setLayoutManager(layoutManager);
    }

    @OnTextChanged(R.id.profile_friends_search)
    void searchForFriend(CharSequence charSequence){
        ((ProfileFriendsAdapter)friendRecyclerView.getAdapter()).getFilter().filter(charSequence);
    }

    @OnEditorAction(R.id.profile_friends_search)
    boolean onEditTextSearch(TextView v, int actionId, KeyEvent event){
        boolean handled = false;

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            KeyboardHelper.hideKeyboard(ProfileUserFriendsInfo.this.getActivity());
            handled = true;
        }

        return handled;
    }

    @OnFocusChange(R.id.profile_friends_search)
    void onSearchClick(View v){
        ((Profile) getActivity()).getAppBarLayout().setExpanded(false);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @OnItemSelected(R.id.friends_limit_spinner)
    void onSpinnerItemSelected(Spinner spinner, int position){
        int limit = Integer.parseInt((String) spinner.getSelectedItem());

        presenter.fetchFriends(limit);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.leaveView();
    }
}
