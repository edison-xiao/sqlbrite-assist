package com.zen.android.brite.dbflow.util;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * RxJavaTestRunner
 *
 * @author yangz
 * @version 2016/7/12
 */
public class RxJavaTestRunner extends RobolectricGradleTestRunner{
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public RxJavaTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

//        RxJavaTestPlugins.resetPlugins();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
