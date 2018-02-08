package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class Response {
    @SerializedName("scrobbles")
    @Expose
    private Scrobbles scrobbles;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("_status")
    @Expose
    private String status;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Scrobbles getScrobbles() {
        return scrobbles;
    }

    public void setScrobbles(Scrobbles scrobbles) {
        this.scrobbles = scrobbles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
