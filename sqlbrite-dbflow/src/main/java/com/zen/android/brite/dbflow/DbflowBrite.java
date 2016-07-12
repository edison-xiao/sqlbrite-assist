package com.zen.android.brite.dbflow;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.squareup.sqlbrite.BriteDatabase;
import com.zen.android.brite.Brite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * DbflowBrite
 *
 * @author zen
 * @version 2016/3/1
 */
public class DbflowBrite {

    static {
        Brite.setupProviders(DbflowDataProvider.INSTANCE);
    }

    public static class Query<T extends BaseModel> extends Brite.Query<T> {

        private Class<T> mClazz;

        public static <T extends BaseModel> Query<T> from(@NonNull Class<T> clazz) {
            ModelAdapter<T> adapter = FlowManager.getModelAdapter(clazz);
            String table = ModelExecutor.getTableNameBase(adapter);
            String dbName = FlowManager.getDatabaseForTable(clazz).getDatabaseName();
            return new Query<>(dbName, table, clazz);
        }

        public static <T extends BaseModel> Query<T> from(
                @NonNull String dbName, @NonNull String table, @NonNull Class<T> clazz) {
            return new Query<>(dbName, table, clazz);
        }

        private Query(@NonNull String dbName, @NonNull String table, @NonNull Class<T> clazz) {
            super(dbName, table);
            mClazz = clazz;
        }

        @Override
        public Query<T> sql(String sql, String... args) {
            super.sql(sql, args);
            return this;
        }

        public Observable<List<T>> queryModels() {
            return query().map(query -> DbflowUtils.loadListFromCursor(mClazz, query.run(), new ArrayList<>()));
        }

        public Observable<T> querySingle() {
            return query().map(query -> DbflowUtils.loadFromCursor(mClazz, query.run()));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void insert(@NonNull Class<T> clazz, @NonNull List<T> models) {
        new ModelExecutor<>(clazz).insert(models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void insert(@NonNull T first, T... models) {
        new ModelExecutor<>(first).insert(first, models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void update(@NonNull T first, T... models) {
        new ModelExecutor<>(first).update(first, models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void update(@NonNull Class<T> clazz, @NonNull List<T> models) {
        new ModelExecutor<>(clazz).update(models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void save(@NonNull T first, T... models) {
        new ModelExecutor<>(first).save(first, models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void save(@NonNull Class<T> clazz, @NonNull List<T> models) {
        new ModelExecutor<>(clazz).save(models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void delete(@NonNull T first, T... models) {
        new ModelExecutor<>(first).delete(first, models);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> void delete(@NonNull Class<T> clazz, @NonNull List<T> models) {
        new ModelExecutor<>(clazz).delete(models);
    }

    public static <T extends BaseModel> void deleteAll(Class<T> table) {
        new ModelExecutor<>(table).deleteAll();
    }

    public static void transaction(String dbName, Runnable task) {
        BriteDatabase.Transaction transaction = Brite.getDatabase(dbName).newTransaction();
        try {
            task.run();
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

}
