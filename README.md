# zookeeper-distributed-lock-curator
Commands for testing on zookeeper version 3.4.x and curator version 2.12.x

  zookeeper-distributed-lock-curator $ rm -f distributedFile.txt
  zookeeper-distributed-lock-curator $ mvn clean install
  
  open multiple terminals to run multiple jvm apps e.g.
  
  
  zookeeper-distributed-lock-curator $ mvn exec:java -D"exec.mainClass"="com.anujaneja.zookeeper.distributed.lock.app.LockingApp" -DappName=App[x]
  

  

