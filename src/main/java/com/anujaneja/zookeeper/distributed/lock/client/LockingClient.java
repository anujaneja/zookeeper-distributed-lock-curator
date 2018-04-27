package com.anujaneja.zookeeper.distributed.lock.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


public class LockingClient {


    private static final Logger logger = LoggerFactory.getLogger(LockingClient.class);

    private CuratorFramework curatorFramework;

    private static final int    DEFAULT_MAX_RETRIES = 5;

    private static final int    DEFAULT_SLEEP_MS    = 10;


    public LockingClient(String destination) {
        this(destination, DEFAULT_SLEEP_MS, DEFAULT_MAX_RETRIES);
    }

    public LockingClient(String destination, int sessionTimeoutMs, int connectionTimeoutMs) {
        this(destination, sessionTimeoutMs, connectionTimeoutMs, DEFAULT_SLEEP_MS, DEFAULT_MAX_RETRIES);
    }

    public LockingClient(String destination, int sessionTimeoutMs, int connectionTimeoutMs, int sleepMs, int maxRetry) {
        logger.info("Instantiating new LockingClient with destination: {}, sessionTimeout: {}, connectionTimeout: {}, maxRetry: {}, and sleep: {}", new Object[] {
                destination,
                sessionTimeoutMs,
                connectionTimeoutMs,
                sleepMs,
                maxRetry });
        this.curatorFramework = CuratorFrameworkFactory.newClient(destination, sessionTimeoutMs, connectionTimeoutMs, new ExponentialBackoffRetry(sleepMs, maxRetry));
        this.curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                switch (newState) {
                    case LOST:
                        logger.error("Zookeeper connection lost");
                        break;
                    case SUSPENDED:
                        logger.warn("Zookeeper connection suspended");
                        break;
                    case RECONNECTED:
                        logger.info("Zookeeper connection re-established");
                        break;
                }
            }
        });
        logger.info("Starting Locking client..");
        long start = System.currentTimeMillis();
        curatorFramework.start();
        logger.info("Done starting LockingClient in {} ms", (System.currentTimeMillis() - start));
    }

    public CuratorFramework getLockingClient() {
        return this.curatorFramework;
    }
}
