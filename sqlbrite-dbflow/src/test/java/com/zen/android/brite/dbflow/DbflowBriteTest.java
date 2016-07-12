package com.zen.android.brite.dbflow;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.zen.android.brite.dbflow.model.Weather;
import com.zen.android.brite.dbflow.util.RxJavaTestRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.List;

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
    }

    @Test
    public void testSetup() throws Exception {
        Weather weather = new Weather(30, "fuzhou", new Date(), Weather.Type.Sunny);
        DbflowBrite.insert(weather);

        List<Weather> weathers =  DbflowBrite.Query.from(Weather.class).queryModels()
                .toBlocking().firstOrDefault(null);
        Assert.assertEquals(weathers.size(), 1);

        Weather target = weathers.get(0);
        Assert.assertFalse(target == weather);
        Assert.assertEquals(target, weather);

    }
}
