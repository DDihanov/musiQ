package com.dihanov.musiq.data.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

@Table(database = ScrobbleDB.class)
public class CachedScrobble extends BaseModel{
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String artistName;

    @Column
    public String trackName;

    @Column
    public long timestamp;
}
