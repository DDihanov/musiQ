package com.dihanov.musiq.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class LovedTracksAdapter extends RecyclerView.Adapter<LovedTracksAdapter.ViewHolder> {
    private List<Track> scrobbles;
    private Context context;
    private AbstractAdapter.OnItemClickedListener<Track> onItemClickedListener;

    public LovedTracksAdapter(List<Track> scrobbles, Context context, AbstractAdapter.OnItemClickedListener<Track> onItemClickedListener) {
        this.scrobbles = scrobbles;
        this.context = context;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loved_tracks_viewholder, parent, false);

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
        Glide.with(context).load(track.getImage().get(Constants.IMAGE_LARGE).getText())
                .apply(RequestOptions.circleCropTransform().placeholder(context.getResources()
                        .getIdentifier("ic_music_note_black_24dp", "drawable", context.getPackageName())))
                .transition(withCrossFade(500))
                .into(holder.thumbnail);
        holder.loved.setVisibility(View.VISIBLE);
        holder.scrobble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(context.getString(R.string.note))
                        .setMessage(context.getString(R.string.unlove_track_message))
                        .setNeutralButton(context.getString(R.string.canc), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton(context.getString(R.string.unlove_track_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onItemClickedListener.onItemClicked(track);
                                removeTrack(position);
                            }
                        }).create().show();

                return false;
            }
        });
    }

    private void removeTrack(int position){
        this.scrobbles.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return scrobbles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loved_track_adapter_text)
        TextView scrobble;

        @BindView(R.id.loved_track_time)
        TextView time;

        @BindView(R.id.loved_track_love)
        ImageView loved;

        @BindView(R.id.loved_track_thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
