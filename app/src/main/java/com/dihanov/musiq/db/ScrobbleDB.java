package com.dihanov.musiq.db;

import com.dihanov.musiq.service.scrobble.Scrobble;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Insert;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Cache;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

@Database(name = ScrobbleDB.NAME, version = ScrobbleDB.VERSION)
public class ScrobbleDB {
    static final String NAME = "ScrobbleDB";
    static final int VERSION = 1;

    @Inject
    public ScrobbleDB(){
    }

    public List<Scrobble> getCachedScrobbles(){
        List<CachedScrobble> result =
                SQLite.select()
                .from(CachedScrobble.class)
                .orderBy(CachedScrobble_Table.timestamp, false)
                .queryList();

        return convertToScrobble(result);
    }

    public void writeScrobble(Scrobble scrobble){
        SQLite.insert(CachedScrobble.class)
                .columns(CachedScrobble_Table.artistName, CachedScrobble_Table.trackName, CachedScrobble_Table.timestamp)
                .values(scrobble.getArtistName(), scrobble.getTrackName(), scrobble.getTimestamp())
                .execute();
    }

    public void clearCache(){
        Delete.table(CachedScrobble.class);
    }

    private List<Scrobble> convertToScrobble(List<CachedScrobble> cachedScrobbles){
        List<Scrobble> result = new ArrayList<>();

        for (CachedScrobble cachedScrobble : cachedScrobbles) {
            result.add(new Scrobble(cachedScrobble.artistName, cachedScrobble.trackName, cachedScrobble.timestamp));
        }

        return result;
    }

    public void removeScrobble(Scrobble scrobble) {
        SQLite.delete(CachedScrobble.class).where(
                CachedScrobble_Table.artistName.is(scrobble.getArtistName())).and(CachedScrobble_Table.trackName.is(scrobble.getTrackName()))
                .async()
                .execute();
    }
}
