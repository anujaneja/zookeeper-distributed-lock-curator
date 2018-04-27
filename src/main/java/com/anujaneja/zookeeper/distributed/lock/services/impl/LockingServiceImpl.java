package com.anujaneja.zookeeper.distributed.lock.services.impl;

import com.anujaneja.zookeeper.distributed.lock.client.LockingClient;
import com.anujaneja.zookeeper.distributed.lock.locking.DistributedLock;
import com.anujaneja.zookeeper.distributed.lock.services.LockingService;

import java.util.concurrent.locks.Lock;

public class LockingServiceImpl implements LockingService {

    private LockingClient lockingClient;
    private String root;
    private static final String DEFAULT_SECTION = "";

    public LockingServiceImpl(LockingClient lockingClient,String root) {
        this.lockingClient = lockingClient;
        this.root = root;
    }

    private String buildLockingPath(String namespace, String path, String section) {
        StringBuilder builder = new StringBuilder("/").append(root).
                append("/").append(namespace).
                append("/").append(path);

        if(!"".equals(section)) {
            builder = builder.append("/").append(section);
        }

        return builder.toString();
    }
    public Lock getLock(String namespace, String path, String section) {
        Lock lock = new DistributedLock(lockingClient,buildLockingPath(namespace,path,section));
        return lock;
    }

    public Lock getLock(String namespace, String path) {
        return getLock(namespace,path,DEFAULT_SECTION);
    }
}
