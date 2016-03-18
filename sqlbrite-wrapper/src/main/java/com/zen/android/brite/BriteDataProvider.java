package com.zen.android.brite;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * BriteDataProvider
 *
 * @author zen
 * @version 2016/3/1
 */
public interface BriteDataProvider {

    SQLiteOpenHelper findDatabaseByName(String dbName);

    ContentResolver findContentProviderByName(String dbName);

}