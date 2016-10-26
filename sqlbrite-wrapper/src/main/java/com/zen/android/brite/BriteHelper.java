package com.zen.android.brite;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * BriteHelper
 *
 * @author zen
 * @version 2016/3/1
 */
enum BriteHelper {

    INSTANCE;

    private final SqlBrite mSqlBrite = SqlBrite.create(new Logger());
    private final HashMap<String, BriteDatabase> mDatabaseHashMap = new HashMap<>();
    private final HashMap<String, BriteContentResolver> mResolverHashMap = new HashMap<>();
    private final Set<BriteDataProvider> mDataProviders = new HashSet<>();

    private static class Logger implements SqlBrite.Logger {

        @Override
        public void log(String message) {
            Log.d("zSqlBrite", message);
//            System.out.println("zSqlBrite " + message);
        }
    }

    BriteHelper() {
    }

    /* package accessible for unit tests */
    void reset(){
        mDatabaseHashMap.clear();
        mResolverHashMap.clear();
    }

    public void addDataProvider(BriteDataProvider provider) {
        if (!mDataProviders.contains(provider)) {
            mDataProviders.add(provider);
        }
    }

    private BriteDatabase findDatabase(String dbName) {
        for (BriteDataProvider provider : mDataProviders) {
            SQLiteOpenHelper helper = provider.findDatabaseByName(dbName);
            if (helper != null) {
                BriteDatabase bd = mSqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
                bd.setLoggingEnabled(true);
                return bd;
            }
        }
        return null;
    }

    private BriteContentResolver findContentResolver(String dbName) {
        for (BriteDataProvider provider : mDataProviders) {
            ContentResolver resolver = provider.findContentProviderByName(dbName);
            if (resolver != null) {
                return mSqlBrite.wrapContentProvider(resolver, Schedulers.io());
            }
        }
        return null;
    }

    public BriteDatabase getBriteDatabase(String dbName) {
        if (mDatabaseHashMap.containsKey(dbName)) {
            return mDatabaseHashMap.get(dbName);
        }
        BriteDatabase db = findDatabase(dbName);
        if (db != null) {
            mDatabaseHashMap.put(dbName, db);
        }
        return db;
    }

    public BriteContentResolver getContentResolver(String dbName) {
        if (mResolverHashMap.containsKey(dbName)) {
            return mResolverHashMap.get(dbName);
        }
        BriteContentResolver resolver = findContentResolver(dbName);
        if (resolver != null) {
            mResolverHashMap.put(dbName, resolver);
        }
        return resolver;
    }
}
