package com.dihanov.musiq.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by dimitar.dihanov on 2/13/2018.
 */

public class ProfileFriendsAdapter extends RecyclerView.Adapter<ProfileFriendsAdapter.ViewHolder> implements Filterable {
    private List<User> friends;
    private List<User> friendsOriginal = new ArrayList<>();
    private Context context;
    private Filter friendFilter;

    public ProfileFriendsAdapter(Context context, List<User> friends) {
        this.friends = friends;
        friendsOriginal.addAll(friends);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_friends_viewholder, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User friend = friends.get(position);

        holder.friendName.setText(friend.getName());
        if (friend.getCountry().trim().isEmpty() || friend.getCountry().trim().equals("")) {
            holder.friendCountry.setVisibility(View.GONE);
        } else {
            holder.friendCountry.setText(friend.getCountry());
        }
        Glide.with(context)
                .load(friend.getImage().get(3).getText())
                .apply(RequestOptions.circleCropTransform()).transition(withCrossFade(2000))
                .into(holder.friendImage);
        holder.friendPlaycount.setText(context.getString(R.string.playcount) + " " + friend.getPlaycount());
        holder.friendUrl.setText(friend.getUrl());
        if (friend.getRecenttrack() != null && friend.getRecenttrack().getArtist() != null && friend.getRecenttrack().getName() != null) {
            holder.friendLastTrack.setText(context.getString(R.string.last_track) + " " + friend.getRecenttrack().getArtist().getName() + " - " + friend.getRecenttrack().getName());
        }
    }

    @Override
    public Filter getFilter() {
        if(friendFilter == null) {
            friendFilter = new FriendFilter();
        }
        return friendFilter;
    }

    private class FriendFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String userString = constraint.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if(constraint == null || constraint.length() == 0 || constraint.toString().trim().equals("")){
                filterResults.values = friendsOriginal;
                filterResults.count = friendsOriginal.size();
            } else {
                ArrayList<User> resultList = new ArrayList<>();
                for (User friend : friends) {
                    if (friend.getName().toLowerCase().contains(userString)) {
                        resultList.add(friend);
                    }
                }

                filterResults.values = resultList;
                filterResults.count = resultList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            friends = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.friend_name)
        TextView friendName;

        @BindView(R.id.friends_country)
        TextView friendCountry;

        @BindView(R.id.friends_image)
        ImageView friendImage;

        @BindView(R.id.friends_playcount)
        TextView friendPlaycount;

        @BindView(R.id.friends_url)
        TextView friendUrl;

        @BindView(R.id.friends_lasttrack)
        TextView friendLastTrack;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
