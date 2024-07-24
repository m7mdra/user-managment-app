package com.example.usermanagmentapp

import kotlin.Throws
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler.ExecutorWorker
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * A class that changes RxJava schedulers to Immediate and Test schedulers for immediate actions in
 * tests and waiting timeouts for testing RxJava timeout in host java tests.
 */
class RxImmediateSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { testScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { IMMEDIATE_SCHEDULER }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }

    companion object {
        /**
         * Get test scheduler for testing RxJava timeout.
         *
         * @return [TestScheduler] object for testing RxJava timeout.
         */
        val testScheduler = TestScheduler()
        private val IMMEDIATE_SCHEDULER: Scheduler = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                // Changing delay to 0 prevents StackOverflowErrors when scheduling with a delay.
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorWorker(Executors.newSingleThreadExecutor(), true, true)
            }
        }
    }
}