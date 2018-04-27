package com.anujaneja.zookeeper.distributed.lock.resource;

import com.anujaneja.zookeeper.distributed.lock.app.LockingApp;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileWriterResourceImpl implements FileWriterResource {

    static private final Logger logger = Logger.getLogger(FileWriterResourceImpl.class);

    private Environment environment;

    public FileWriterResourceImpl(Environment environment) {
        this.environment = environment;
    }


    public void writeToFile(String content) throws IOException {
        writeToFile(content,null);

    }

    public void writeToFile(String content, String path) throws IOException {
        path = path!=null?path:environment.getProperty("file.path");

        File file = new File(path);
        if(!file.exists()) {
            boolean created = file.createNewFile();
            logger.info("File with path "+file.getAbsolutePath()+" created "+created);
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));

        bufferedWriter.write(content);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
}
