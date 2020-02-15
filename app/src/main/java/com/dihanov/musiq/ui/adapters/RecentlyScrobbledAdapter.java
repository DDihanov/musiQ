package com.dihanov.musiq.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.util.Constants;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by dimitar.dihanov on 2/13/2018.
 */

public class RecentlyScrobbledAdapter extends RecyclerView.Adapter<RecentlyScrobbledAdapter.ViewHolder> {
    public interface RecentlyScrobbledItemClickListener {
        void onTrackLoved(Track track);
        void onTrackUnloved(Track track);
    }

    private List<Track> scrobbles;
    private Context context;
    private AlertDialog.Builder builder;
    private RecentlyScrobbledItemClickListener recentlyScrobbledItemClickListener;

    public RecentlyScrobbledAdapter(List<Track> scrobbles, Context context, RecentlyScrobbledItemClickListener recentlyScrobbledItemClickListener) {
        this.scrobbles = scrobbles;
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.recentlyScrobbledItemClickListener = recentlyScrobbledItemClickListener;
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
        if (track.getArtist().getName() == null || track.getName() == null) {
            return;
        }
        if (track.getDate() == null) {
            if (track.getAttr() == null || track.getAttr().getNowplaying() == null) {
                return;
            }
            holder.time.setText(R.string.last_scrobbling_text);
        } else {
            holder.time.setText(DateUtils.formatSameDayTime(
                    Long.parseLong(track.getDate().getUts()) * 1000L,
                    System.currentTimeMillis(),
                    DateFormat.SHORT,
                    DateFormat.SHORT));
        }

        holder.scrobble.setText(track.getArtist().getName() + " - " + track.getName());
        Glide.with(context).load(track.getArtist().getImage().get(Constants.IMAGE_LARGE).getText())
                .apply(RequestOptions.circleCropTransform().placeholder(context.getResources()
                        .getIdentifier("ic_music_note_black_24dp", "drawable", context.getPackageName())))
                .transition(withCrossFade(500))
                .into(holder.thumbnail);
        if (!track.getLoved().equals("0")) {
            holder.loved.setVisibility(View.VISIBLE);
        } else {
            holder.loved.setVisibility(View.INVISIBLE);
        }
        holder.scrobble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.loved.getVisibility() == View.VISIBLE) {
                    builder.setTitle(context.getString(R.string.note))
                            .setMessage(context.getString(R.string.unlove_track_message))
                            .setNeutralButton(context.getString(R.string.canc), (dialog, which) -> {
                                return;
                            })
                            .setPositiveButton(context.getString(R.string.unlove_track_button), (dialog, which) -> {
                                recentlyScrobbledItemClickListener.onTrackUnloved(track);
                                holder.loved.setVisibility(View.INVISIBLE);
                                track.setLoved("0");
                                notifyDataSetChanged();
                            }).create().show();

                } else {
                    builder.setTitle(context.getString(R.string.note))
                            .setMessage(context.getString(R.string.love_track_message))
                            .setNeutralButton(context.getString(R.string.canc), (dialog, which) -> {
                                return;
                            })
                            .setPositiveButton(context.getString(R.string.love_track_button), (dialog, which) -> {
                                recentlyScrobbledItemClickListener.onTrackLoved(track);
                                holder.loved.setVisibility(View.VISIBLE);
                                track.setLoved("1");
                                notifyDataSetChanged();
                            }).create().show();
                }

                return false;
            }
        });
    }

    public void loveNowPlaying(String nowPlayingArtist, String nowPlayingTrackName) {
        if (scrobbles.isEmpty() || scrobbles.size() == 0){
            return;
        }
        Track nowPlaying = scrobbles.get(0);
        if (nowPlaying.getArtist().getName().equals(nowPlayingArtist) && nowPlaying.getName().equals(nowPlayingTrackName)) {
            nowPlaying.setLoved("1");
            notifyDataSetChanged();
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

        @BindView(R.id.recently_scrobbled_thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
