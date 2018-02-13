package com.dihanov.musiq.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 2/13/2018.
 */

public class RecentlyScrobbledAdapter extends RecyclerView.Adapter<RecentlyScrobbledAdapter.ViewHolder> {
    private List<String> scrobbles;

    public RecentlyScrobbledAdapter(List<String> scrobbles) {
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
        String text = scrobbles.get(position);
        holder.scrobble.setText(text);
    }

    @Override
    public int getItemCount() {
        return scrobbles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recently_scrobbled_adapter_text)
        TextView scrobble;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
