package com.zen.android.brite.dbflow;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.squareup.sqlbrite.BriteDatabase;
import com.zen.android.brite.Brite;

import java.util.List;

import rx.Observable;

/**
 * @author zen
 * @version 2016/3/16
 */
class ModelExecutor<T extends BaseModel> {

    private final BriteDatabase   mDatabase;
    private final String          mTableName;
    private final ModelAdapter<T> mAdapter;

    @SuppressWarnings("unchecked")
    public ModelExecutor(T model) {
        this((Class<T>) model.getClass());
    }

    @SuppressWarnings("unchecked")
    public ModelExecutor(Class<T> clazz) {
        DbflowDataProvider.readyProvider();

        mAdapter = FlowManager.getModelAdapter(clazz);
        mTableName = getTableNameBase(mAdapter);
        String dbName = FlowManager.getDatabaseForTable(mAdapter.getModelClass()).getDatabaseName();
        mDatabase = Brite.getDatabase(dbName);
    }

    @SafeVarargs
    public final void insert(@NonNull T first, T... models) {
        if (models == null || models.length == 0) {
            insertModel(first);
            return;
        }

        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            insertModel(first);
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::insertModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void insert(@NonNull List<T> models) {
        if (models.isEmpty()) {
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::insertModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @SuppressWarnings("unchecked")
    public void update(@NonNull T first, T... models) {
        if (models == null || models.length == 0) {
            updateModel(first);
            return;
        }

        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            updateModel(first);
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::updateModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void update(@NonNull List<T> models) {
        if (models.isEmpty()) {
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::updateModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @SuppressWarnings("unchecked")
    public void save(@NonNull T first, T... models) {
        if (models == null || models.length == 0) {
            saveModel(first);
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            saveModel(first);
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::saveModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void save(@NonNull List<T> models) {
        if (models.isEmpty()) {
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::saveModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(@NonNull T first, T... models) {
        if (models == null || models.length == 0) {
            deleteModel(first);
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            deleteModel(first);
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::deleteModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void delete(@NonNull List<T> models) {
        if (models.isEmpty()) {
            return;
        }
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            Observable.from(models)
                    .filter(model -> model != null)
                    .subscribe(this::deleteModel);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void deleteAll() {
        mDatabase.delete(mTableName, null);
    }

    private void deleteModel(@NonNull T model) {
        mDatabase.delete(mTableName, mAdapter.getPrimaryConditionClause(model).getQuery());
        mAdapter.updateAutoIncrement(model, 0);
    }

    private void saveModel(@NonNull T model) {
        if (model.exists()) {
            updateModel(model);
        } else {
            insertModel(model);
        }
    }

    private void updateModel(@NonNull T model) {
        mDatabase.update(mTableName, DbflowUtils.createValues(model),
                mAdapter.getPrimaryConditionClause(model).getQuery());
    }

    private void insertModel(@NonNull T model) {
        long id = mDatabase.insert(mTableName, DbflowUtils.createValues(model));
        mAdapter.updateAutoIncrement(model, id);
    }

    private static String getTableNameBase(ModelAdapter adapter) {
        String tableName = adapter.getTableName();
        if (tableName.startsWith("`")) {
            return tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

}
