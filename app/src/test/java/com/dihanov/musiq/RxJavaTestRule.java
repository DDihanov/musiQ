package com.dihanov.musiq;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 3/1/2018.
 */

public class RxJavaTestRule implements TestRule {
    @Override public Statement apply(final Statement base, Description d) {
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override public Scheduler apply(Scheduler scheduler) throws Exception {
                        return Schedulers.trampoline();
                    }
                });

                RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override public Scheduler apply(Scheduler scheduler) throws Exception {
                        return Schedulers.trampoline();
                    }
                });

                RxJavaPlugins.setNewThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override public Scheduler apply(Scheduler scheduler) throws Exception {
                        return Schedulers.trampoline();
                    }
                });

                RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                        new Function<Callable<Scheduler>, Scheduler>() {
                            @Override public Scheduler apply(Callable<Scheduler> schedulerCallable)
                                    throws Exception {
                                return Schedulers.trampoline();
                            }
                        });

                base.evaluate();
                RxJavaPlugins.reset();
                RxAndroidPlugins.reset();
            }
        };
    }
}
