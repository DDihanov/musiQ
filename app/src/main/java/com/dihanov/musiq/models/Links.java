
package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("link")
    @Expose
    private Link link;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

}
