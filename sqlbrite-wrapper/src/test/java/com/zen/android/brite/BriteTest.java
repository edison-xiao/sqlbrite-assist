package com.zen.android.brite;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import com.zen.android.brite.db.BriteDataProviderImpl;
import com.zen.android.brite.db.SqliteOpenHelperImpl;
import com.zen.android.brite.util.TestRunner;
import com.zen.android.brite.util.RxTestUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zen
 * @version 2016/4/28
 */
@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = IConfig.SDK_LEVEL)
public class BriteTest {

    private String mDatabaseName;
    private String mTableName;
    private static final AtomicInteger ID_GENERATE = new AtomicInteger(1);

    @Before
    public void setUp() throws Exception {
        Brite.setupProviders(new BriteDataProviderImpl());
        mTableName = SqliteOpenHelperImpl.TABLE_NAME;
        mDatabaseName = SqliteOpenHelperImpl.DATABASE_NAME;
    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

    private void cleanUp() {
//        BriteDatabase db = Brite.getDatabase(mDatabaseName);
//        db.delete(mTableName, null, null);
    }

    @Test
    public void testGetDatabase() throws Exception {
        BriteDatabase db = Brite.getDatabase(mDatabaseName);
        Assert.assertNotNull(db);
        db.close();
    }

    @Test
    public void testQuery() throws Exception {
        BriteDatabase db = Brite.getDatabase(mDatabaseName);

        QueryObservable query = Brite.Query.from(mDatabaseName, mTableName).query();
        Assert.assertNotNull(query);

        List<SqlBrite.Query> result = RxTestUtils.toResults(query, () -> {
            ContentValues cv = new ContentValues();
            cv.put("id", ID_GENERATE.getAndAdd(1));
            cv.put("title", "test1");
            db.insert(mTableName, cv);
        });

        Cursor cursor = result.get(0).run();
        Assert.assertEquals(cursor.getCount(), 1);
        cursor.close();

        db.close();
    }
}
