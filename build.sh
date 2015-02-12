#!/bin/sh
if [ $# != 1 ];then
    echo "Usage: build.sh <cdh_version>"
fi

. ./bin/function.sh

ant clean
ant -Dcdh.version=$1 package

#currentpath=$(cd "$(dirname "$0")";pwd)
#cd $currentpath/target/ 
#tar zcf BigDataAnalysis-1.0.tar.gz BigDataAnalysis-1.0 --exclude BigDataAnalysis-1.0/build.sh
#
#currentpath=`pwd`
#echo "The Released BigDataAnalysis is generated in $currentpath/BigDataAnalysis-1.0.tar.gz"
