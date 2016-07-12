package com.zen.android.brite;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * BriteDataProvider
 *
 * @author zen
 * @version 2016/3/1
 */
public interface BriteDataProvider {

    @Nullable
    SQLiteOpenHelper findDatabaseByName(@NonNull String dbName);

    @Nullable
    ContentResolver findContentProviderByName(@NonNull String dbName);

}