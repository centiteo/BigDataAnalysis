====================
Start Kafka Cluster:
====================
  nohup /root/zhangliping/kafka_2.8.0-0.8.0/bin/kafka-server-start.sh config/server.properties &

====================
Build Project:
====================
  往ASB/conf下拷入相应集群的配置文件，往ASB/lib/lib_2_5_1或ASB/lib/lib_3_1拷入相应的集群依赖包
  执行：ant -Didp.version=<version> package, <version>为2.5.1或3.1

====================
Steaming Generation:
====================
在Cygwin中：
	cd ./examples/ASB/datagen
	`cat datagen_local.cygwin`
在Linux中：
	cd ./examples/ASB/datagen
	sh datagen_local.sh
	
【注意】执行之前要配置正确的标注参数
java -cp .;../../../target/BigDataAnalysis-1.0.jar;../../../lib/*;../../../lib/lib_2_5_1/* com.intel.bigdata.analysis.datagen.GeneratorDriver --instanceDoc=mobile.xml --totalNum=6000000   --mode=local  --outputDir=hdfs://192.168.0.21:8020/ASB_jianzhong --replicaNum=1 --parallel=1 --minNum=6000000 --maxNum=6000000 --neverStop=true

默认的topic是FileMessage，默认的partition个数是16；如果consumer线程比较多，可以适当增加（num.partitions)

====================
Streaming DataLoad:
====================
在Cygwin中：
	cd ./examples/ASB/dataload
	`cat dataload_local.cygwin`
在Linux中：
	cd ./examples/ASB/dataload
	sh dataload_local.sh

【注意】执行之前要在load_asb.properties中配置正确的标注参数
dataload.source.streaming.fetch=true
可以通过fetchParallel和threadsPerMapper来控制并发度
hbase.target.table.name来设置HBase的表名

java -cp .;../../../target/BigDataAnalysis-1.0.jar;../../../lib/*;../../../lib/lib_2_5_1/*  com.intel.bigdata.analysis.dataload.DataLoad load_asb.properties 
