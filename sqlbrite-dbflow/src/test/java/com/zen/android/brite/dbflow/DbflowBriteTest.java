package com.zen.android.brite.dbflow;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.zen.android.brite.dbflow.model.Weather;
import com.zen.android.brite.dbflow.util.RxJavaTestRunner;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

/**
 * DbflowBriteTest
 *
 * @author yangz
 * @version 2016/7/12
 */
@RunWith(RxJavaTestRunner.class)
@Config(constants = com.zen.android.brite.BuildConfig.class, sdk = IConfig.SDK_LEVEL,
        manifest = IConfig.MANIFEST_PATH)
public class DbflowBriteTest {

    @Before
    public void setUp() throws Exception {
        FlowManager.init(new FlowConfig.Builder(RuntimeEnvironment.application).build());
//        FlowManager.getDatabase(TestDatabase.DB_NAME).reset(RuntimeEnvironment.application);
//        DbflowBrite.deleteAll(Weather.class);
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.getDatabase(TestDatabase.DB_NAME).reset(RuntimeEnvironment.application);
        FlowManager.destroy();
//        Thread.sleep(200);
//        Field field = FlowManager.class.getDeclaredField("mDatabaseHolder");
//        field.setAccessible(true);
//        field.set(null, null);
    }

    @Test
    public void testInsert() throws Exception {
//        Weather weather = new Weather(30, "fuzhou", new Date(), Weather.Type.Sunny);
//        DbflowBrite.insert(weather);
//
//        weather.insert();

        for (int i = 0; i < 20; i++) {
            new Weather(30, "fuzhou", new Date(), Weather.Type.Sunny).insert();
        }

//        for (int i = 0; i < 2; i++) {
//            DbflowBrite.insert(new Weather(i+20, "fuzhou", new Date(), Weather.Type.Sunny));
//        }


        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), 22);

        Weather target = weathers.get(0);
//        Assert.assertFalse(target == weather);
//        Assert.assertEquals(target, weather);

        System.out.println(target.getIdx());
    }

    @Test
    public void testSave() throws Exception {
        Weather weather = new Weather(20, "cc", new Date(), Weather.Type.Sunny);
        DbflowBrite.save(weather);

        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), 1);

        Weather target = weathers.get(0);
        Assert.assertFalse(target == weather);
        Assert.assertEquals(target, weather);
    }

    @Test
    public void testSaveModels() throws Exception {
        final int testSize = 3;
        for (int i = 0; i < testSize; i++) {
            Weather weather = new Weather(i + 1, "test_" + i, new Date(), Weather.Type.Sunny);
//            weather.insert();
            DbflowBrite.insert(weather);
            System.out.println(weather.getIdx());
        }

        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), testSize);
    }

    @Test
    public void testSaveList() throws Exception {
        DbflowBrite.Query.from(Weather.class)
                .queryModels()
                .subscribe(weathers -> {
                    System.out.println(weathers.size());
                });

        final int testSize = 10;
        List<Weather> source = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            source.add(new Weather(i + 1, "test_" + i, new Date(), Weather.Type.Sunny));
        }

        Weather[] copy = new Weather[source.size()];
        source.toArray(copy);

        DbflowBrite.save(new Weather(100, "test_100", new Date(), Weather.Type.Cloudy), copy);

        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), testSize + 1);
    }

    @Test
    public void testInsertList() throws Exception {
        final int testSize = 10;
        List<Weather> source = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            source.add(new Weather(i + 1, "test_" + i, new Date(), Weather.Type.Sunny));
        }

        DbflowBrite.insert(Weather.class, source);

        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), testSize);
    }

    private List<Weather> getAllWeather() {
        return DbflowBrite.Query.from(Weather.class).queryModels()
                .toBlocking().firstOrDefault(null);
    }
}
