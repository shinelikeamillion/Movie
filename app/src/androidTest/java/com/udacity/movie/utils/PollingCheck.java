package com.udacity.movie.utils;

import junit.framework.Assert;

import java.util.concurrent.Callable;

public abstract class PollingCheck {

    private static long TIME_SLICE = 50;
    private long mTimeOut = 3000;

    public  PollingCheck () {}

    public PollingCheck (long timeout) {
        mTimeOut = timeout;
    }

    protected abstract boolean check();

    public void run () {
        if (check()) {
            return;
        }

        long timeout = mTimeOut;
        while (timeout > 0) {
            try {
                Thread.sleep(TIME_SLICE);
            } catch (InterruptedException e) {
                Assert.fail("线程中断异常");
            }

            if (check()) {
                return;
            }

            timeout -= TIME_SLICE;
        }
        Assert.fail("超时错误");
    }

    public static void check (CharSequence message, long timeout, Callable<Boolean> condition) throws Exception {
        while (timeout > 0) {
            if (condition.call()) {
                return;
            }
            Thread.sleep(TIME_SLICE);
            timeout -= TIME_SLICE;
        }
        Assert.fail(message.toString());
    }
}
