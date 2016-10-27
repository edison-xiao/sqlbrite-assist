package com.zen.android.brite.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * TestDatabase
 *
 * @author yangz
 * @version 2016/7/12
 */
@Database(name = TestDatabase.DB_NAME, version = TestDatabase.VERSION)
public class TestDatabase {

    static final String DB_NAME = "test";
    static final int VERSION = 1;

}
