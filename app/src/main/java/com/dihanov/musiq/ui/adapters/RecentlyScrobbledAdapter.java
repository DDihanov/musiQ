package com.dihanov.musiq.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Track;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 2/13/2018.
 */

public class RecentlyScrobbledAdapter extends RecyclerView.Adapter<RecentlyScrobbledAdapter.ViewHolder> {
    private List<Track> scrobbles;

    public RecentlyScrobbledAdapter(List<Track> scrobbles) {
        this.scrobbles = scrobbles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recently_scrobbled_viewholder, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track track = scrobbles.get(position);
        if (track == null) {
            return;
        }
        if (track.getArtist().getName() == null || track.getName() == null || track.getDate() == null) {
            return;
        }
        holder.scrobble.setText(track.getArtist().getName() + " - " + track.getName());
        holder.time.setText(DateUtils.formatSameDayTime(
                Long.parseLong(track.getDate().getUts()) * 1000L,
                System.currentTimeMillis(),
                DateFormat.SHORT,
                DateFormat.SHORT));
        if (!track.getLoved().equals("0")) {
            holder.loved.setVisibility(View.VISIBLE);
        } else {
            holder.loved.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return scrobbles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recently_scrobbled_adapter_text)
        TextView scrobble;

        @BindView(R.id.recently_scrobbled_time)
        TextView time;

        @BindView(R.id.recently_scrobbled_love)
        ImageView loved;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
