package com.dihanov.musiq.data.repository.scrobble;

import android.media.session.PlaybackState;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class State {
    public static final int NONE = 0;

    private long lastPauseTime = System.currentTimeMillis();
    private int state;
    private long penalty;

    public State() {
    }

    public State(int status) {
        this.state = status;
    }

    public long getPenalty() {
        return penalty;
    }

    public void setPenalty(long penalty) {
        this.penalty = penalty;
    }

    public int getStatus() {
        return state;
    }

    public void setLastPauseTime(long lastPauseTime) {
        this.lastPauseTime = lastPauseTime;
    }

    public void handleStatusChanged(int state) {
        long currentTime = System.currentTimeMillis();

        switch (state) {
            case PlaybackState.STATE_PLAYING:
                this.setPenalty(currentTime - lastPauseTime);
                break;

            default:
                lastPauseTime = currentTime;
                break;
        }

        this.state = state;
    }

    public void setState(int state) {
        this.state = state;
        this.handleStatusChanged(state);
    }
}
