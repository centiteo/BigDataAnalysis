# Introduction
BigDataAnalysis is a general big data analysis project over Cloudera platform, including Data Generator simulation, Data Ingestion, Data Processing, Data Analysis and Data Exporting.

------------------------
The delivery for customer:
1.Run $BIGDATAANALYSIS_HOME/build.sh, it will generated a obfuscator codes package under $BIGDATAANALYSIS_HOME/target/BigDataAnalysis-1.0.tar.gz
2.Bring the $BIGDATAANALYSIS_HOME/target/BigDataAnalysis-1.0.tar.gz to cluster side.



The latest BigDataAnalysis can be downloaded from an BigDataAnalysis Mirror [1].

The source code can be found at [1]

The BigDataAnalysis changes tracker is at [2]

1. https://sh-ssvn.sh.intel.com/ssg_repos/svn_hadoop/hadoop/hadoop/SOLUTIONS/BigDataAnalysis
2. https://sh-ssvn.sh.intel.com/ssg_repos/svn_hadoop/hadoop/hadoop/SOLUTIONS/BigDataAnalysis/CHANGES.txt

# Build
====================
Apache-ANT:
  You should have jdk and apache-ant installed
  ant -Dcdh.version=<version> package
      Currently, cdh.version only supports 5.0 and 5.2.1

Maven
  You should have jdk and maven installed in build machine.
  
  Build Steps: 
  Type mvn package to build project

# How to Run
====================
Data Generation:

To get started using BigDataAnalysis's different components, like BulkLoad, DataGen, etc., the full documentation for this release can be found under the docs/ directory that accompanies this README. 

Also, you can find some pre-built examples in examples folder to understand the usage directly.