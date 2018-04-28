# zookeeper-distributed-lock-curator
Steps/Commands for testing on zookeeper version 3.4.x and curator version 2.12.x

    zookeeper-distributed-lock-curator $ rm -f distributedFile.txt
    zookeeper-distributed-lock-curator $ mvn clean install
  
    open multiple terminals to run multiple jvm apps e.g.
  
  
    zookeeper-distributed-lock-curator $ mvn exec:java -D"exec.mainClass"="com.anujaneja.zookeeper.distributed.lock.app.LockingApp" -DappName=App[x]
  
  NOTE: This path in zookeeper /distributedLocks/FILE_WRITER_LOCK/global/file-lock1 (lock-key) will be not be deleted. Although, the dynamic ephermal node is deleted. In long run if we have multiple lock-keys we will be having a lot of nodes un-used.

Steps/Commands for testing on zookeeper version 3.5.x and curator version 4.x.x

    upgrade the zookeeper to 3.5.x (You might need to delete old snapshots)

    zookeeper-distributed-lock-curator $ rm -f distributedFile.txt
    
    zookeeper-distributed-lock-curator $ mvn clean install
  
    open multiple terminals to run multiple jvm apps e.g.
  
  
    zookeeper-distributed-lock-curator $ mvn exec:java -D"exec.mainClass"="com.anujaneja.zookeeper.distributed.lock.app.LockingApp" -DappName=App[x]

  NOTE: This path in zookeeper /distributedLocks/FILE_WRITER_LOCK/global/file-lock1 (lock-key) will be be deleted. But, only the child at "lock-key" level will be deleted i.e. "file-lock1" will be deleted along with dynamic ephermal node.
