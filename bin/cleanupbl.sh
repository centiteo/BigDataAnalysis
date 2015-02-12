#!/bin/sh

currentPath=$(cd "$(dirname "$0")"; pwd)

# specify the value of following three arguments
hbaseMasterIpaddress=192.168.0.121

# for IDP 3.1, hdfs starts with "hdfs://Cluster"
hfilePath=hdfs://vt-nn:8020/user/test/bltest_hfile
hbaseTableName=bl_test


hbaseMalformedTableName=exception_log
recordCountDir=$currentPath/../tmpbl*
hiveExtTableName=$hbaseTableName
hiveTotalTableName=$hiveExtTableName\_total

# clean hive tables
hive -e "drop table $hiveExtTableName"
hive -e "drop table $hiveTotalTableName"


# clean rcord count dir
rm -rf $recordCountDir


# clean hfile output in hdfs
ssh $hbaseMasterIpaddress "sudo -u hdfs hadoop fs -rmr $hfilePath"


# clean hbase tables
ssh $hbaseMasterIpaddress "echo disable \'$hbaseTableName\' | hbase shell"
ssh $hbaseMasterIpaddress "echo drop \'$hbaseTableName\' | hbase shell"

ssh $hbaseMasterIpaddress "echo disable \'$hbaseMalformedTableName\' | hbase shell"
ssh $hbaseMasterIpaddress "echo drop \'$hbaseMalformedTableName\' | hbase shell"