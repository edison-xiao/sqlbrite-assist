package com.zen.android.brite.dbflow;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteOpenHelper;

import com.raizlabs.android.dbflow.config.BaseDatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import com.zen.android.brite.Brite;
import com.zen.android.brite.BriteDataProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DbflowDataProvider
 *
 * @author zen
 * @version 2016/3/1
 */
class DbflowDataProvider implements BriteDataProvider {

    private static boolean hasReady = false;

    /**
     * 省去调用初始化去注册的方式，在使用具体方法前需要验证是否已安装Provider
     */
    static void readyProvider() {
        if (!hasReady) {
            hasReady = true;
            Brite.setupProviders(new DbflowDataProvider());
        }
    }

    @Override
    public SQLiteOpenHelper findDatabaseByName(String dbName) {
        return getSQLiteOpenHelper(FlowManager.getDatabase(dbName));
    }

    @Override
    public ContentResolver findContentProviderByName(String dbName) {
        return null;
    }

    private SQLiteOpenHelper getSQLiteOpenHelper(BaseDatabaseDefinition definition) {
        SQLiteOpenHelper helper = null;
        try {
            Method method = BaseDatabaseDefinition.class.getDeclaredMethod("getHelper");
            method.setAccessible(true);
            OpenHelper openHelper = (OpenHelper) method.invoke(definition);
            if (openHelper instanceof SQLiteOpenHelper) {
                helper = (SQLiteOpenHelper) openHelper;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return helper;
    }
}
