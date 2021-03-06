package com.dihanov.musiq.ui.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.db.ScrobbleDB;
import com.dihanov.musiq.data.repository.scrobble.Scrobble;
import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;
import com.dihanov.musiq.util.Connectivity;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dihanov on 17/07/2018
 */
public class ScrobbleReviewAdapter extends ArrayAdapter<Scrobble> {
    private List<Scrobble> tracks;
    private Context context;
    private ScrobbleRepository scrobbleRepository;
    private ScrobbleDB scrobbleDB;

    public ScrobbleReviewAdapter(@NonNull Context context, int resource, @NonNull List<Scrobble> objects, ScrobbleRepository scrobbleRepository, ScrobbleDB scrobbleDB) {
        super(context, resource, objects);
        this.scrobbleDB = scrobbleDB;
        this.scrobbleRepository = scrobbleRepository;
        this.tracks = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Scrobble scrobble = getItem(position);
        String artist = scrobble.getArtistName();
        String trackName = scrobble.getTrackName();
        final View result;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        result = layoutInflater.inflate(R.layout.scrobble_review_layout, null);

        ScrobbleReviewViewHolder viewHolder = new ScrobbleReviewViewHolder(result);


        viewHolder.track.setText(trackName);
        viewHolder.artist.setText(artist);

        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Connectivity.isConnected(context)) {
                    Toast.makeText(context, context.getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                scrobble.setArtistName(viewHolder.artist.getText().toString());
                scrobble.setTrackName(viewHolder.track.getText().toString());

                remove(scrobble);
                scrobbleRepository.scrobble(scrobble);
                scrobbleDB.removeScrobble(scrobble);
                Toast.makeText(context, context.getString(R.string.track_scrobbled), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.artist.setText(artist);
                viewHolder.track.setText(trackName);
                scrobble.setTrackName(trackName);
                scrobble.setArtistName(artist);
            }
        });

        viewHolder.date.setText(DateUtils.formatSameDayTime(
                scrobble.getTimestamp() * 1000L,
                System.currentTimeMillis(),
                DateFormat.SHORT,
                DateFormat.SHORT));

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrobbleDB.removeScrobble(scrobble);
                remove(scrobble);
            }
        });

        return result;
    }

    protected static class ScrobbleReviewViewHolder {
        @BindView(R.id.scrobble_review_artist)
        EditText artist;
        @BindView(R.id.scrobble_review_track)
        EditText track;
        @BindView(R.id.scrobble_review_confirm)
        ImageView confirm;
        @BindView(R.id.scrobble_review_revert)
        ImageView revert;
        @BindView(R.id.scrobble_review_delete)
        ImageView delete;
        @BindView(R.id.scrobble_review_date)
        TextView date;

        public ScrobbleReviewViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return this.tracks.size();
    }

    @Override
    public void remove(@Nullable Scrobble object) {
        for (Iterator<Scrobble> iterator = tracks.iterator(); iterator.hasNext(); ) {
            Scrobble scrobble = iterator.next();
            if (scrobble.getTrackName().equals(object.getTrackName()) &&
                    scrobble.getArtistName().equals(object.getArtistName()) &&
                    scrobble.getTimestamp() == object.getTimestamp()) {
                iterator.remove();
                notifyDataSetChanged();
                break;
            }
        }
    }
}
