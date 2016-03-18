package com.zen.android.brite;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

/**
 * Brite
 *
 * @author zen
 * @version 2016/3/1
 */
public class Brite {

    public static void setupProviders(BriteDataProvider... providers) {
        for (BriteDataProvider provider : providers) {
            BriteHelper.INSTANCE.addDataProvider(provider);
        }
    }

    public static BriteDatabase getDatabase(String dbName) {
        return BriteHelper.INSTANCE.getBriteDatabase(dbName);
    }

    public static BriteContentResolver getContentResolver(String dbName) {
        return BriteHelper.INSTANCE.getContentResolver(dbName);
    }

    public static class Query<T> {
        private final String   dbName;
        private       String   table;
        private       String   sql;
        private       String[] args;

        public static <T> Query<T> from(
                @NonNull String dbName, @NonNull String table) {
            return new Query<>(dbName, table);
        }

        public Query(@NonNull String dbName, @NonNull String table) {
            this.dbName = dbName;
            this.table = table;
        }

        public Query<T> sql(String sql, String... args) {
            this.sql = sql;
            this.args = args;
            return this;
        }

        public QueryObservable query() {
            BriteDatabase db = getDatabase(dbName);
            if (db == null) {
                throw new IllegalStateException("BriteDatabase " + dbName + " has not find");
            }
            if (TextUtils.isEmpty(sql)) {
                sql = "SELECT * FROM " + table;
            }
            return db.createQuery(table, sql, args);
        }
    }
}
