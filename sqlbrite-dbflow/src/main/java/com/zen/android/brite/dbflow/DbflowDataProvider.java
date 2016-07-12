package com.zen.android.brite.dbflow;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import com.zen.android.brite.BriteDataProvider;

/**
 * DbflowDataProvider
 *
 * @author zen
 * @version 2016/3/1
 */
enum DbflowDataProvider implements BriteDataProvider {

    INSTANCE;

    @Override
    public SQLiteOpenHelper findDatabaseByName(String dbName) {
        return getSQLiteOpenHelper(FlowManager.getDatabase(dbName));
    }

    @Override
    public ContentResolver findContentProviderByName(String dbName) {
        return null;
    }

    private SQLiteOpenHelper getSQLiteOpenHelper(DatabaseDefinition definition) {
        SQLiteOpenHelper helper = null;
        OpenHelper openHelper = definition.getHelper();
        if (openHelper instanceof SQLiteOpenHelper) {
            helper = (SQLiteOpenHelper) openHelper;
        }
        return helper;
    }
}
