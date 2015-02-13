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


start=$(date +%s.%N)
# run gzip split
java -cp $CLASSPATH com.intel.bigdata.analysis.dataload.util.GZFileUtils $@ 
end=$(date +%s.%N)
echo "----------- time of spliting gzip file---------"
getTiming $start $end

