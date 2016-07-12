package com.zen.android.brite.db;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zen.android.brite.BriteDataProvider;

import org.robolectric.RuntimeEnvironment;

/**
 * @author zen
 * @version 2016/4/28
 */
public class BriteDataProviderImpl implements BriteDataProvider {

    private static final SQLiteOpenHelper SQLITE_OPEN_HELPER = new SqliteOpenHelperImpl(
            RuntimeEnvironment.application, SqliteOpenHelperImpl.DATABASE_NAME, null, 1
    );

    @Override
    @Nullable
    public SQLiteOpenHelper findDatabaseByName(@NonNull String dbName) {
        if (dbName.equals(SqliteOpenHelperImpl.DATABASE_NAME)) {
            return SQLITE_OPEN_HELPER;
        }
        return null;
    }

    @Override
    @Nullable
    public ContentResolver findContentProviderByName(@NonNull String dbName) {
        return null;
    }
}
