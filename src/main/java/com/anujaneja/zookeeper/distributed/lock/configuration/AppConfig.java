package com.anujaneja.zookeeper.distributed.lock.configuration;

import com.anujaneja.zookeeper.distributed.lock.client.LockingClient;
import com.anujaneja.zookeeper.distributed.lock.resource.FileWriterResource;
import com.anujaneja.zookeeper.distributed.lock.resource.FileWriterResourceImpl;
import com.anujaneja.zookeeper.distributed.lock.services.LockingService;
import com.anujaneja.zookeeper.distributed.lock.services.impl.LockingServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = "com.anujaneja.zookeeper.distributed.lock")
@PropertySources({
        @PropertySource( value = { "classpath:application.properties"}),
        @PropertySource( value = { "classpath:application-${profile}.properties" }),
        @PropertySource(value= "file:${MYAPP_CONFIG_LOCATION}",ignoreResourceNotFound = true)
})
public class AppConfig {
    static final Logger log  = Logger.getLogger(AppConfig.class);

    //TODO: find out why we need to use constructor based injection.??? instead of field based??
    @Autowired
    private Environment environment;

    @Value("${zookeeper.connectionString}")
    private String zookeeperConnectionString;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Qualifier("lockingClient")
    public LockingClient lockingClient() {
        System.out.println("zookeeperConnectionString===="+zookeeperConnectionString);
        return new LockingClient(zookeeperConnectionString);
    }


    @Bean
    @Qualifier("lockingService")
    public LockingService lockingService() {
        return new LockingServiceImpl(lockingClient(),"distributedLocks");
    }

    @Bean
    public Object fileWriterResource() {
        return new FileWriterResourceImpl(environment);
    }

}
