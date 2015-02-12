#!/bin/sh

currentpath=$(cd "$(dirname "$0")";pwd)
. $currentpath/function.sh


CLASSPATH=".:$currentpath/../target/BigDataAnalysis-1.0.jar:$currentpath/../lib/*:$currentpath/../lib/lib_cdh5.0/*:$currentpath/../conf/*"

# for CDH 5.0
CLASSPATH=$currentpath/../conf:$CLASSPATH:$currentpath/../lib/lib_cdh5.0/*

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

# run coprocessor query
java -cp $CLASSPATH com.intel.bigdata.analysis.index.QueryRecord $@
