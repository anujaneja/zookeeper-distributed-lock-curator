package com.anujaneja.zookeeper.distributed.lock.resource;

import java.io.IOException;


public interface FileWriterResource {
    public void writeToFile(String content) throws IOException;

    public void writeToFile(String content,String path) throws IOException;
}
