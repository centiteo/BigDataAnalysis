#!/bin/sh

currentpath=$(cd "$(dirname "$0")";pwd)
. $currentpath/function.sh


CLASSPATH=".:$currentpath/../BigDataAnalysis-1.0.jar:$currentpath/../lib/*:$currentpath/../lib/lib_2_5_1/*:$currentpath/../conf/*"

# run parallel coprocessor queries
java -cp $CLASSPATH com.intel.bigdata.analysis.hbase.query.ParallelQueryClient $@
