package com.anujaneja.zookeeper.distributed.lock.app;

import com.anujaneja.zookeeper.distributed.lock.client.LockingClient;
import com.anujaneja.zookeeper.distributed.lock.resource.FileWriterResource;
import com.anujaneja.zookeeper.distributed.lock.services.LockingService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


public class LockingApp {

    static final Logger logger  = Logger.getLogger(LockingApp.class);

    public static void main(String[] args) throws InterruptedException {
        String profile = System.getProperty("profile");
        if(profile ==null) {
            System.setProperty("profile","dev");
        }

        final String appName = System.getProperty("appName","app1");

        logger.info("profile="+System.getProperty("profile"));
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});


        final LockingService lockingService = context.getBean("lockingService", LockingService.class);
        final LockingClient lockingClient = context.getBean("lockingClient",LockingClient.class);
        final FileWriterResource fileWriterResource = context.getBean("fileWriterResource", FileWriterResource.class);
        final Environment environment = context.getBean("environment",Environment.class);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i=0;i<10;i++) {
            final int index = i;

            Callable<Void> task =  new Callable<Void>() {
                public Void call() throws Exception {

                    Lock lock=null;

                    try{
                        lock = lockingService.getLock("FILE_WRITER_LOCK","global","file-lock1");

                        lock.lock();

                        //Call the file Writer service to write
                        String threadName =  Thread.currentThread().getName();
                        StringBuilder builder = new StringBuilder(appName).append(" - ").append(threadName);
                        builder.append(" - ").append(new Date().toString()).append(": Start Writing to File....");

                        fileWriterResource.writeToFile(builder.toString());


                        long sleepTime = Long.parseLong(environment.getProperty("thread.sleepTime"));
                        logger.info(appName+" Sleep time for Thread "+threadName+ " is: "+sleepTime);

                        Thread.sleep(sleepTime);


                        builder = new StringBuilder(appName).append(" - ").append(threadName);
                        builder.append(" - ").append(new Date().toString()).append(": End Writing to File....");

                        fileWriterResource.writeToFile(builder.toString());


                    } catch (Exception ex) {
                        logger.error("Exception in taking a lock",ex);
                    } finally {
                        if(lock!=null) {
                            lock.unlock();
                        }

                    }

                    return null;
                }
            };

            executorService.submit(task);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        logger.info("Now closing the zookeeper connection....");

        lockingClient.getLockingClient().close();


    }


}
