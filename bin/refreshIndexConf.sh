#!/bin/sh

currentpath=$(cd "$(dirname "$0")";pwd)
jarname=$currentpath/../BigDataAnalysis-1.0.jar
dirname=asb
indexconf=index-conf
currentPro=current.properties

regionserverlist=(192.168.0.121 192.168.0.122 192.168.0.123)
masterlist=(192.168.0.121 192.168.0.122 192.168.0.123)

sudo -u hdfs hadoop fs -rmr /user/hbase/conf
hadoop fs -mkdir /user/hbase
hadoop fs -mkdir /user/hbase/conf
# put new index configuration to HDFS 
hadoop fs -put $currentpath/../conf/*${indexconf}.x* /user/hbase/conf/

# put new current load configuration to HDFS 
hadoop fs -put $currentpath/../conf/${currentPro} /user/hbase/conf/

sudo -u hdfs hadoop fs -chown -R hbase:hadoop /user/hbase/conf/
sudo -u hdfs hadoop fs -chmod -R 755 /user/hbase/conf/

echo refreshed index-conf.xml and current.properties in HDFS
