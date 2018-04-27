package com.anujaneja.zookeeper.distributed.lock.services;

import java.util.concurrent.locks.Lock;

public interface LockingService {

    public Lock getLock(String namespace,String path, String section);

    public Lock getLock(String namespace,String path);
}
