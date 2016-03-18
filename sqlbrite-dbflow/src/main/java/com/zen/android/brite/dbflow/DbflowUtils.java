package com.zen.android.brite.dbflow;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * DbflowUtils
 *
 * @author zen
 * @version 2016/3/1
 */
public class DbflowUtils {

    @SuppressWarnings("unchecked")
    @CheckResult
    @NonNull
    public static <T extends BaseModel> ContentValues createValues(T model) {
        ContentValues values = new ContentValues();
        model.getModelAdapter().bindToContentValues(values, model);
        return values;
    }

    @CheckResult
    public static <T extends BaseModel> List<T> loadListFromCursor(
            Class<T> clazz, Cursor cursor, List<T> result) {
        return loadListFromCursor(clazz, cursor, result, true);
    }

    @CheckResult
    public static <T extends BaseModel> List<T> loadListFromCursor(
            Class<T> clazz, Cursor cursor, List<T> result, boolean closeCursor) {
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    ModelAdapter<T> adapter = FlowManager.getModelAdapter(clazz);
                    do {
                        result.add(adapter.loadFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            } finally {
                if (closeCursor) {
                    cursor.close();
                }
            }
        }
        return result;
    }

}
