package com.dihanov.musiq;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 3/1/2018.
 */

public class RxJavaTestRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description d) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());

                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

                RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                        schedulerCallable -> Schedulers.trampoline());

                base.evaluate();
                RxJavaPlugins.reset();
                RxAndroidPlugins.reset();
            }
        };
    }

}
