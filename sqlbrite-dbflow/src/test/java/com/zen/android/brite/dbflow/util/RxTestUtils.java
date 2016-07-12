package com.zen.android.brite.dbflow.util;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * RxTestUtils
 *
 * @author yangz
 * @version 2016/7/12
 */
public class RxTestUtils {

    public static <T> List<T> toResults(Observable<T> observable, Runnable doSomething) {
        TestSubscriber<T> result = new TestSubscriber<>();
        observable.subscribe(result);

        if (doSomething != null) {
            doSomething.run();
        }

        result.assertNoErrors();
        return result.getOnNextEvents();
    }

}
