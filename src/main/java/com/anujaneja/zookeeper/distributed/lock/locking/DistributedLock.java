package com.anujaneja.zookeeper.distributed.lock.locking;

import com.anujaneja.zookeeper.distributed.lock.client.LockingClient;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class DistributedLock implements Lock {

    private final Logger logger = Logger.getLogger(DistributedLock.class);

    private final String path;
    private InterProcessMutex distributedMutex;
    private final String section;

    private long lockTakenTime;

    public DistributedLock(LockingClient lockingClient,String path) {
        this.distributedMutex = new InterProcessMutex(lockingClient.getLockingClient(),path);
        this.path= path;
        this.section = "";
    }

    public DistributedLock(LockingClient lockingClient,String path,String section) {
        this.distributedMutex = new InterProcessMutex(lockingClient.getLockingClient(),path);
        this.path= path;
        this.section = section;
    }


    public void lock() {

        try {
            long start = 0;
            logger.info("Going to take lock on path "+path+ " and section "+section);
            start = System.currentTimeMillis();
            distributedMutex.acquire();
            lockTakenTime = System.currentTimeMillis();
            logger.info("Acquired lock on path: "+path+", section:"+section+", in "+(System.currentTimeMillis() - start)+" ms" );
        } catch (Exception e) {
            logger.error("Exception in taking lock on path "+path+ ", section "+section);
            throw  new RuntimeException(e);
        }

    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("not supported with DistributedLock");
    }

    public boolean tryLock() {
        boolean acquired = false;
        try {
            long start = 0;
            logger.info("Going to take lock on path "+path+ " and section "+section);
            start = System.currentTimeMillis();
            acquired = distributedMutex.acquire(0,TimeUnit.NANOSECONDS);
            lockTakenTime = System.currentTimeMillis();
            logger.info("Acquired lock on path: "+path+", section:"+section+", in "+(System.currentTimeMillis() - start)+" ms" );

            return acquired;

        } catch (Exception e) {
            logger.error("Exception in taking lock on path"+path+ ", section "+section);
            throw  new RuntimeException(e);
        }
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean acquired = false;
        try {
            long start = 0L;

            logger.info("Going to take lock on path "+path+ " and section "+section);
            start = System.currentTimeMillis();
            acquired = distributedMutex.acquire(time,unit);
            lockTakenTime = System.currentTimeMillis();

            logger.info("Acquired lock on path: "+path+", section:"+section+", in "+(System.currentTimeMillis() - start)+" ms" );

            return acquired;

        } catch (Exception e) {
            logger.error("Exception in taking lock on path"+path+ ", section "+section);
            throw  new RuntimeException(e);
        }

    }

    public void unlock() {

        try {
            long start = 0L;

            start = System.currentTimeMillis();
            logger.info("Releasing lock on path: "+path+", section: "+section+" holdTime: "+(System.currentTimeMillis() - lockTakenTime));

            distributedMutex.release();


            logger.info("Released lock on path: "+path+", section: "+section+" in : "+(System.currentTimeMillis() - start) +" ms");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException("not supported with DistributedLock");
    }
}
