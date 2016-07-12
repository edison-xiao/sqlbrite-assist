package com.zen.android.brite;

import com.squareup.sqlbrite.BriteDatabase;
import com.zen.android.brite.db.BriteDataProviderImpl;
import com.zen.android.brite.db.SqliteOpenHelperImpl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author zen
 * @version 2016/4/28
 */
public class BriteHelperTest {

    @Before
    public void setUp() throws Exception {
        BriteHelper.INSTANCE.addDataProvider(new BriteDataProviderImpl());
    }

    @Test
    public void testGetDatabase() throws Exception {
        BriteDatabase db = BriteHelper.INSTANCE.getBriteDatabase("");
        Assert.assertNull(db);

        db = BriteHelper.INSTANCE.getBriteDatabase(SqliteOpenHelperImpl.DATABASE_NAME);
        Assert.assertNotNull(db);
    }
}
