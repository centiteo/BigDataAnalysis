# Introduction
BigDataAnalysis is a general big data analysis project over Cloudera platform, including Data Generator simulation, Data Ingestion, Data Processing, Data Analysis and Data Exporting.


# Build

Requirements:
    Oracle JDK
    Apache Ant
    Apache Maven
	
Build using Apache Ant:
    ant -Dcdh.version=<version> package, currently, we only support 5.0 and 5.2.1 for cdh.version, and will have 5.3 soon.

Build using Apache Maven
  You should have jdk and maven installed in build machine.
  
  Build Steps: 
  Type mvn package to build project

# How to Run

## Data Generation
Prepare an XML document specifying how the record will look like, and run the generation program. You can refer to schema/generator.xsd for how to create the XML instance document. Typicall, you will run like this::

java -cp /etc/hadoop/conf:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop/lib/*:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop/.//*:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop-hdfs/./:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop-hdfs/lib/*:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop-hdfs/.//*:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop-yarn/lib/*:/opt/cloudera/parcels/CDH-5.2.1-1.cdh5.2.1.p0.12/lib/hadoop/libexec/../../hadoop-yarn/.//*:/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/lib/*:/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/.//*:/etc/hbase/conf:/opt/cloudera/parcels/CDH/lib/hbase/*:/opt/cloudera/parcels/CDH/lib/hbase/lib/*:* com.cloudera.bigdata.analysis.datagen.GeneratorDriver &lt;args&gt;

Or you can just type com.cloudera.bigdata.analysis.datagen.GeneratorDriver for help.

Also, you can find some pre-built examples in examples folder to understand the usage directly.

## General Data Loading

## Bulk Data Loading
