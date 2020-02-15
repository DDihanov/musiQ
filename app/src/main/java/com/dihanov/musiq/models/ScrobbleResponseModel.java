package com.dihanov.musiq.models;

import com.dihanov.musiq.service.scrobble.Scrobble;

public class ScrobbleResponseModel {
    private Scrobble scrobble;
    private Response response;

    public ScrobbleResponseModel(Scrobble scrobble, Response response) {
        this.scrobble = scrobble;
        this.response = response;
    }

    public Scrobble getScrobble() {
        return scrobble;
    }

    public void setScrobble(Scrobble scrobble) {
        this.scrobble = scrobble;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
