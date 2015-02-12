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

# for IDP 2.5.1
CLASSPATH=$CLASSPATH:$currentpath/../lib/lib_2_5_1/*

# for IDP 3.1
#CLASSPATH=$CLASSPATH:$currentpath/../lib/lib_3_1/*

CLASSPATH=$currentpath/../conf:$CLASSPATH
echo $CLASSPATH

hadoop fs -rmr /user/cluster.jar
hadoop fs -put ./lib/cluster.jar /user


start=$(date +%s.%N)
# run mapreduce load
java -cp $CLASSPATH com.intel.bigdata.analysis.dataload.DataLoad $@
end=$(date +%s.%N)
echo "----------- time of inmemory2hbase or hdfs2hbase loading data---------"
getTiming $start $end
