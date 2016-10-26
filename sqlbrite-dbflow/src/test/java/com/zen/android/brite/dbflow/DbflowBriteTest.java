package com.zen.android.brite.dbflow;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.zen.android.brite.dbflow.model.Weather;
import com.zen.android.brite.dbflow.model.Weather_Table;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
        FlowManager.getDatabase(TestDatabase.DB_NAME).reset(RuntimeEnvironment.application);
    }

    @After
    public void tearDown() throws Exception {
        DbflowBrite.deleteAll(Weather.class);
        FlowManager.destroy();
    }

    @Test
    public void testInsert() throws Exception {
        int insertSize = 10;
        int[] notifyCount = {insertSize + 3, 0};
        int totalCount = 0;

        CountDownLatch latch = new CountDownLatch(notifyCount[0]);
        DbflowBrite.Query.from(Weather.class)
                .queryModels()
                .subscribe(weathers -> {
                    notifyCount[1]++;
                    latch.countDown();
                });

        for (int i = 0; i < insertSize; i++) {
            DbflowBrite.insert(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }
        totalCount += insertSize;

        List<Weather> src = new ArrayList<>();
        for (int i = 0; i < insertSize; i++) {
            src.add(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }
        totalCount += insertSize;
        DbflowBrite.insert(Weather.class, src);

        Weather[] ws = new Weather[3];
        for (int i = 0; i < 3; i++) {
            ws[i] = new Weather(i, "ff", new Date(), Weather.Type.Cloudy);
        }
        totalCount += 3;
        DbflowBrite.insert(ws[0], ws[1], ws[2]);


        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), totalCount);

        Weather target = weathers.get(weathers.size() - 1);
        Assert.assertEquals(target.getIdx(), weathers.size());

        latch.await(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(notifyCount[0], notifyCount[1]);
    }

    @Test
    public void testSave() throws Exception {
        int saveSize = 10;
        int[] notifyCount = {saveSize + 3, 0};
        int totalCount = 0;

        CountDownLatch latch = new CountDownLatch(notifyCount[0]);
        DbflowBrite.Query.from(Weather.class)
                .queryModels()
                .subscribe(weathers -> {
                    notifyCount[1]++;
                    latch.countDown();
                });

        for (int i = 0; i < saveSize; i++) {
            DbflowBrite.save(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }
        totalCount += saveSize;

        List<Weather> src = new ArrayList<>();
        for (int i = 0; i < saveSize; i++) {
            src.add(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }
        totalCount += saveSize;
        DbflowBrite.save(Weather.class, src);

        Weather[] ws = new Weather[3];
        for (int i = 0; i < 3; i++) {
            ws[i] = new Weather(i, "ff", new Date(), Weather.Type.Cloudy);
        }
        totalCount += 3;
        DbflowBrite.save(ws[0], ws[1], ws[2]);


        List<Weather> weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), totalCount);

        Weather target = weathers.get(weathers.size() - 1);
        Assert.assertEquals(target.getIdx(), weathers.size());

        latch.await(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(notifyCount[0], notifyCount[1]);
    }

    @Test
    public void testUpdate() throws Exception {
        int saveSize = 10;
        int[] notifyCount = {saveSize * 2 + 3, 0};

        CountDownLatch latch = new CountDownLatch(notifyCount[0]);

        String sql = SQLite.select().from(Weather.class)
                .where(Weather_Table.temperature.lessThan(30))
                .toString();

        DbflowBrite.Query.from(Weather.class)
                .sql(sql)
                .queryModels()
                .subscribe(weathers -> {
                    notifyCount[1]++;
                    latch.countDown();
                });

        for (int i = 0; i < saveSize; i++) {
            DbflowBrite.save(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }

        List<Weather> weathers = getAllWeather();
        for (Weather weather : weathers) {
            weather.setCity("xiamen");
            DbflowBrite.update(weather);
        }

        for (Weather weather : weathers) {
            weather.setCity("fujian");
        }
        DbflowBrite.update(Weather.class, weathers);

        weathers.get(0).setType(Weather.Type.Cloudy);
        weathers.get(1).setType(Weather.Type.Cloudy);
        DbflowBrite.update(weathers.get(0), weathers.get(1));

        latch.await(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(notifyCount[0], notifyCount[1]);

        weathers = getAllWeather();
        for (Weather weather : weathers) {
            Assert.assertEquals(weather.getCity(), "fujian");
        }
    }

    @Test
    public void testDelete() throws Exception {
        int saveSize = 10;
        int[] notifyCount = {saveSize + 4, 0};

        CountDownLatch latch = new CountDownLatch(notifyCount[0]);
        DbflowBrite.Query.from(Weather.class)
                .queryModels()
                .subscribe(weathers -> {
                    notifyCount[1]++;
                    latch.countDown();
                });

        for (int i = 0; i < saveSize; i++) {
            DbflowBrite.save(new Weather(i, "fuzhou", new Date(), Weather.Type.Sunny));
        }

        List<Weather> weathers = getAllWeather();
        DbflowBrite.delete(weathers.get(0));
        DbflowBrite.delete(weathers.get(1), weathers.get(2));

        weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), saveSize - 3);

        DbflowBrite.delete(Weather.class, weathers);
        weathers = getAllWeather();
        Assert.assertEquals(weathers.size(), 0);

        latch.await(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(notifyCount[0], notifyCount[1]);
    }


    private List<Weather> getAllWeather() {
        return DbflowBrite.Query.from(Weather.class).queryModels()
                .toBlocking().firstOrDefault(null);
    }
}
