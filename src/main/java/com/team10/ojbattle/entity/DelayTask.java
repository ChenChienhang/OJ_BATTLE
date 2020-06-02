package com.team10.ojbattle.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/27 21:45
 * @version: 1.0
 */
public class DelayTask implements Delayed {

    private final String subject;

    private final long executeTime;

    public DelayTask(String subject, long delayTime) {
        this.subject = subject;
        this.executeTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.executeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public String getSubject() {
        return subject;
    }
}


