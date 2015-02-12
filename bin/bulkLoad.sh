#!/bin/sh

currentpath=$(cd "$(dirname "$0")";pwd)
. $currentpath/function.sh

# for debug with target dir
#cp $currentpath/../../BigDataAnalysis-1.0.jar $currentpath/../
#rm -rf $currentpath/../BigDataAnalysis-1.0-obfuscator.jar
#CLASSPATH=.:$currentpath/../target/BigDataAnalysis-1.0.jar

# for debug with source codes
CLASSPATH=.:$currentpath/../target/BigDataAnalysis-1.0.jar

#CLASSPATH=.:$currentpath/../BigDataAnalysis-1.0-obfuscator.jar
jars=`ls $currentpath/../lib`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/$jar"
done

# for CDH 5.0
CLASSPATH=$CLASSPATH:$currentpath/../lib/lib_cdh5.0/*

# for CDH 5.0
jars=`ls $currentpath/../lib/lib_cdh5.0/hadoop`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/lib_cdh5.0/hadoop/$jar"
done

jars=`ls $currentpath/../lib/lib_cdh5.0/hadoop-hdfs`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/lib_cdh5.0/hadoop-hdfs/$jar"
done

jars=`ls $currentpath/../lib/lib_cdh5.0/hadoop-mapreduce`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/lib_cdh5.0/hadoop-mapreduce/$jar"
done

jars=`ls $currentpath/../lib/lib_cdh5.0/hbase/lib`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/lib_cdh5.0/hbase/lib/$jar"
done

jars=`ls $currentpath/../lib/lib_cdh5.0/zookeeper`
for jar in $jars
do
    CLASSPATH="$CLASSPATH:$currentpath/../lib/lib_cdh5.0/zookeeper/$jar"
done

CLASSPATH=$currentpath/../conf:$CLASSPATH
echo $CLASSPATH

# run bulk load
java -cp $CLASSPATH com.intel.bigdata.analysis.dataload.mapreduce.Hdfs2HBaseWorker $@