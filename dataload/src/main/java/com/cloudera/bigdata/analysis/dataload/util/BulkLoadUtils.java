package com.cloudera.bigdata.analysis.dataload.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.mapreduce.PutSortReducer;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.exception.ETLException;
import com.cloudera.bigdata.analysis.dataload.extract.HBaseTargetFieldSpec;
import com.cloudera.bigdata.analysis.dataload.extract.HdfsSourceFieldSpec;
import com.cloudera.bigdata.analysis.dataload.io.CombineTextInputFormat;
import com.cloudera.bigdata.analysis.dataload.mapreduce.CustomizedCombineHBaseRowMapper;
import com.cloudera.bigdata.analysis.dataload.mapreduce.CustomizedHBaseRowMapper;
import com.cloudera.bigdata.analysis.dataload.mapreduce.ExtendedHBaseRowMapper;
import com.cloudera.bigdata.analysis.dataload.transform.BulkLoadProcessFieldSpec;
import com.cloudera.bigdata.analysis.dataload.transform.TargetTableSpec;
import com.cloudera.bigdata.analysis.index.util.IndexUtil;

public class BulkLoadUtils {
  private final static Logger LOGGER = LoggerFactory
      .getLogger(BulkLoadUtils.class);
  final static String EXCEPTION_LOG_TABLE_NAME = "exception_log";

  public static String addPropertyPrefix(String property){
	  return Constants.BULKLOAD_PROPERTY_PREFIX + property;
  }
  public static void setConfFromSourceDefinition(
      HdfsSourceFieldSpec hdfsSourceFieldSpec, Configuration conf) {
    conf.set(addPropertyPrefix(Constants.IDP_HBASE_MASTER_IPADDRESS),
    		hdfsSourceFieldSpec
            .getIdpHBaseMasterIpaddress());
      conf.set(addPropertyPrefix(Constants.HDFS_SOURCE_FILE_INPUT_PATH), 
    		  hdfsSourceFieldSpec
    	        .getHdfsSourceFileInputPath());
      conf.set(addPropertyPrefix(Constants.HDFS_SOURCE_FILE_ENCODING), 
    		  hdfsSourceFieldSpec
    	        .getHdfsSourceFileEncoding());
      conf.set(addPropertyPrefix(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_DELIMITER),
    		  hdfsSourceFieldSpec
    	        .getHdfsSourceFileRecordFieldsDelimiter());
      conf.set(addPropertyPrefix(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_NUMBER),
    		  hdfsSourceFieldSpec
    	        .getHdfsSourceFileRecordFieldsNumber());
    conf.set(
        addPropertyPrefix(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_TYPE_INT),
    		  hdfsSourceFieldSpec
    	        .getHdfsSourceFileRecordFieldTypeInt());
    conf.setBoolean(
        addPropertyPrefix(Constants.HDFS_SOURCE_INPUT_DIRS_RECURSIVE),
        hdfsSourceFieldSpec.getHdfsSourceInputDirsRecursive());
  }

  public static void setConfFromTargetDefinition(
      HBaseTargetFieldSpec hbaseTargetFieldSpec, Configuration conf) {
    // ETL for hbase rowkey, column families and column, all are required for
    // CustomizedHBaseRowMapper
    conf.set(addPropertyPrefix(Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH), 
        hbaseTargetFieldSpec.getHbaseGeneratedHfilesOutputPath());
    conf.set(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME), 
        hbaseTargetFieldSpec.getHbaseTargetTableName());
    conf.setBoolean(addPropertyPrefix(Constants.HBASE_TARGET_WRITE_TO_WAL_FLAG), 
        hbaseTargetFieldSpec.isHbaseTargetWriteToWAL());
    conf.set(Constants.HFILE_COMPRESSION,
        hbaseTargetFieldSpec.getHbaseTargetTableCompression());

    conf.set(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_CELL_MAPPING), 
    		CommonUtils.getStringFromMap(
    		hbaseTargetFieldSpec.getTargetTableCellMap(), 
    		Constants.COLUMN_ENTRY_SEPARATOR,
      Constants.COLUMN_KEY_VALUE_SEPARATOR));
    LOGGER.info("hbaseTargetTableCellMapString is "
      + conf.get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_CELL_MAPPING)));
  }

  public static void setConfFromBulkLoadStageDefinition(
      HBaseTargetFieldSpec hbaseTargetFieldSpec,
      BulkLoadProcessFieldSpec bulkLoadProcessFieldSpec, Configuration conf)
      throws Exception {
	  
    // BulkLoad Stage Definition, all are optional
    conf.setBoolean(addPropertyPrefix(Constants.BUILD_INDEX), 
        bulkLoadProcessFieldSpec.isBuildIndex());

    conf.setBoolean(addPropertyPrefix(Constants.ONLY_GENERATE_SPLITKEYSPEC), 
        bulkLoadProcessFieldSpec.isOnlyGenerateSplitKeySpec());

    boolean nativeTaskEnabled = bulkLoadProcessFieldSpec.isNativeTaskEnabled();
    conf.setBoolean(addPropertyPrefix(Constants.NATIVETASK_ENABLED), 
        bulkLoadProcessFieldSpec.isNativeTaskEnabled());
    // enable native task
    if (nativeTaskEnabled) {
      conf.set("mapreduce.map.output.collector.delegator.class",
          "org.apache.hadoop.mapred.nativetask.NativeMapOutputCollectorDelegator");
    }

    if (conf.getBoolean(addPropertyPrefix(Constants.BUILD_INDEX), false)) {
      // make sure there is a index configuration for this current bulkload
      // table
      if (!IndexUtil.isIndexConfAvailableForLoad(conf
          .get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME)))) {
        ETLException
            .handle("Failed to load data because there is index configuration error!");
      }
      
      conf.set(addPropertyPrefix(Constants.REGION_QUANTITY),
	    		bulkLoadProcessFieldSpec.getRegionQuantity());
      conf.set(addPropertyPrefix(Constants.INDEX_CONF_FILE_NAME),
          bulkLoadProcessFieldSpec.getIndexConfFileName());
      conf.set(addPropertyPrefix(Constants.HBASE_COPROCESSOR_LOCATION),
          bulkLoadProcessFieldSpec.getHBaseCoprocessorLocation());
	
	    // generate split key spec for quick index build
	    // split according to the regionQuantity
	    StringBuffer keys = new StringBuffer();
      byte[][] splitKeys =
          RegionSplitKeyUtils.calcSplitKeys(Integer.parseInt(conf
              .get(addPropertyPrefix(Constants.REGION_QUANTITY))));
	    for (int i = 0; i < splitKeys.length - 1; i++) {
	      keys.append(CommonUtils.convertByteArrayToString(splitKeys[i]));
	      keys.append(",");
	    }
	    keys.append(CommonUtils
	        .convertByteArrayToString(splitKeys[splitKeys.length - 1]));
	
	    String hbaseTargetTableSplitKeySpec = keys.toString();
      conf.set(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC),
          hbaseTargetTableSplitKeySpec);
	
	    System.out.println(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC + "="
          + hbaseTargetTableSplitKeySpec);
      
    } else {
      // if without index building
      generateHbaseTargetTableSplitKeySpec(hbaseTargetFieldSpec,
          bulkLoadProcessFieldSpec, conf);
    }

    // use ExtensibleHBaseRowConverter or not
    boolean extendedHbaseRowConverter = false;
    String extendedHBaseRowConverterClass = bulkLoadProcessFieldSpec
        .getExtendedHBaseRowConverterClass();
    if (Util.checkIsEmpty(extendedHBaseRowConverterClass)) {
      LOGGER.info("Use CustomizedHBaseRowConverter");
    } else {
      LOGGER.info("Use ExtensibleHBaseRowConverter, Class name is: "
          + extendedHBaseRowConverterClass);
      extendedHbaseRowConverter = true;
      conf.set(
          addPropertyPrefix(Constants.EXTENDEDHBASEROWCONVERTER_CLASS_KEY),
          extendedHBaseRowConverterClass);
    }
    conf.setBoolean(addPropertyPrefix(Constants.EXTENDEDHBASEROWCONVERTER),
        extendedHbaseRowConverter);

    // specify the input Split Size for combineInputFiles
    boolean combineInputFiles = false;
    String inputSplitSize = null;
    inputSplitSize = bulkLoadProcessFieldSpec.getInputSplitSize();
    if (inputSplitSize == null || inputSplitSize.isEmpty()) {
      LOGGER
          .info("Not use input files combination before mapper, specify value of \"inputSplitSize\"  if you want to use it");
    } else {
      LOGGER
          .info("Use input files combination before mapper, combination of inputSplitSize is "
              + inputSplitSize);
      combineInputFiles = true;
      // set input files combination in mapreduce
      conf.set("inputSplitSize", inputSplitSize);
      conf.set("mapreduce.input.fileinputformat.split.minsize", inputSplitSize);
      conf.set("mapreduce.input.fileinputformat.split.maxsize", inputSplitSize);
      conf.set("mapreduce.input.fileinputformat.split.minsize.per.rack",
          inputSplitSize);
      conf.set("mapreduce.input.fileinputformat.split.minsize.per.node",
          inputSplitSize);
    }
    conf.setBoolean("combineInputFiles", combineInputFiles);

    // 307M 70 mapper
    conf.set("inputSplitSize", "419439400");
    conf.set("mapreduce.input.fileinputformat.split.minsize", "419439400");
    conf.set("mapreduce.input.fileinputformat.split.maxsize", "419439400");
    conf.set("mapreduce.input.fileinputformat.split.minsize.per.rack",
        "419439400");
    conf.set("mapreduce.input.fileinputformat.split.minsize.per.node",
        "419439400");

    if (conf.getBoolean("extendedHbaseRowConverter", false)
        && (conf.getBoolean("combineInputFiles", false))) {
      LOGGER
          .warn("Use extensibleHbaseRowConverter to run bulkload, if want to use input files combination before mapper, should set keep "
              + "\"extendedHBaseRowConverterClass\" empty");
    }

    //conf.set(addPropertyPrefix(Constants.IMPORT_DATE),
    // bulkLoadProcessFieldSpec.getImportDate());
    //conf.set(addPropertyPrefix(Constants.VALIDATOR_CLASS),
    // bulkLoadProcessFieldSpec.getValidatorClass());
    conf.setBoolean(addPropertyPrefix(Constants.CREATE_MALFORMED_TABLE),
        bulkLoadProcessFieldSpec.isCreateMalformedTable());
  }

  public static void generateHbaseTargetTableSplitKeySpec(
      HBaseTargetFieldSpec hbaseTargetFieldSpec,
      BulkLoadProcessFieldSpec bulkLoadProcessFieldSpec, Configuration conf)
      throws Exception {
    boolean preCreateRegions = bulkLoadProcessFieldSpec.isPreCreateRegions();
    String rowkeyPrefix = null;
    String recordCountFile = null;
    String recordsNumPerRegion = null;
    String hbaseTargetTableSplitKeySpec = hbaseTargetFieldSpec
        .getHbaseTargetTableSplitKeySpec();
    if (preCreateRegions) {
      conf.setBoolean(addPropertyPrefix(Constants.PRE_CREATE_REGIONS),
          preCreateRegions);
      rowkeyPrefix = bulkLoadProcessFieldSpec.getRowkeyPrefix();
      recordsNumPerRegion = bulkLoadProcessFieldSpec.getRecordsNumPerRegion();

      if (!Util.checkIsEmpty(hbaseTargetTableSplitKeySpec)) {
        conf.set(
            addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC),
            hbaseTargetTableSplitKeySpec);
      } else {
        // generate splitKeySpec automatically
        if (Util.checkIsEmpty(rowkeyPrefix)) {
          ETLException.handle("Failed to run " + Constants.PRE_CREATE_REGIONS
              + ", need to specify value of \"rowkeyPrefix\"");
        }
        conf.set(addPropertyPrefix(Constants.ROWKEY_PREFIX), rowkeyPrefix);

        if (Util.checkIsEmpty(recordsNumPerRegion)) {
          // generate hive script and record count file
          recordCountFile = RecordCountHiveShellUtils.getRecordCountFile(conf);
          RecordCountHiveShellUtils.getTotalRecordNum(conf);
          // calculate records number per region
          recordsNumPerRegion = RecordCountHiveShellUtils
              .calculateRecordNumPerRegion(conf);
        } else {
          // generate hive script and record count file
          recordCountFile = RecordCountHiveShellUtils.getRecordCountFile(conf);
          RecordCountHiveShellUtils.getTotalRecordNum(conf);
          if (Long.parseLong(recordsNumPerRegion) > Long.parseLong(conf
              .get("totalRecordNum"))) {
            recordsNumPerRegion = conf.get("totalRecordNum");
          }
        }
        hbaseTargetTableSplitKeySpec = RegionSplitKeyUtils
            .genSplitKeysFromFile(recordCountFile,
                Long.parseLong(recordsNumPerRegion));
        System.out.println(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC + "="
            + hbaseTargetTableSplitKeySpec);
        conf.set(
            addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC),
            hbaseTargetTableSplitKeySpec);
      }
    } else {
      // not pre-create regions
      conf.set(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC),
          "");
    }
  }

  public static Job createHBaseTableAndGenerateHfile(FileSystem fs,
      HBaseAdmin hbaseAdmin, Configuration conf) throws Exception {
    String hbaseTableName =
        conf.get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME));
    TargetTableSpec targetTableSpec = new TargetTableSpec(
            hbaseTableName,
            conf.get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_CELL_MAPPING)),
            conf.get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC)));

    // create hbase table
    if (!CommonUtils.doesTableExist(hbaseAdmin, hbaseTableName)) {
      CommonUtils.createTable(conf, hbaseAdmin, targetTableSpec);
    } else {
      // !!!!!!Change the logic, it's totally wrong!!!!!!
      int existedRegionQuantity =
          CommonUtils.getExistedTableRegionsNum(hbaseAdmin, hbaseTableName);
      System.out.println("Table " + hbaseTableName
          + " is existed. The existed region number is "
          + existedRegionQuantity);

      if (conf.getBoolean(addPropertyPrefix(Constants.BUILD_INDEX), false)) {
        // update the existedRegionQuantity and split key spec
        conf.set(addPropertyPrefix(Constants.REGION_QUANTITY),
            String.valueOf(existedRegionQuantity));

        // generate split key spec for quick index build
        // split according to the regionQuantity
        StringBuffer keys = new StringBuffer();
        byte[][] splitKeys =
            RegionSplitKeyUtils.calcSplitKeys(existedRegionQuantity);
        for (int i = 0; i < splitKeys.length - 1; i++) {
          keys.append(CommonUtils.convertByteArrayToString(splitKeys[i]));
          keys.append(",");
        }
        keys.append(CommonUtils
            .convertByteArrayToString(splitKeys[splitKeys.length - 1]));

        String hbaseTargetTableSplitKeySpec = keys.toString();
        conf.set(
            addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC),
            hbaseTargetTableSplitKeySpec);
        System.out.println("Updated "
            + Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC + "="
            + hbaseTargetTableSplitKeySpec);
      }
    }

    // create malformed table
    if (conf.getBoolean(addPropertyPrefix(Constants.CREATE_MALFORMED_TABLE),
        false)) {
      if (!CommonUtils.doesTableExist(hbaseAdmin, EXCEPTION_LOG_TABLE_NAME)) {
        CommonUtils.createMalformedLineLogTable(hbaseAdmin,
            EXCEPTION_LOG_TABLE_NAME);
      }
    }

    // delete hfile output path
    Path outputDir =
        new Path(
            conf.get(addPropertyPrefix(Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH)));
      if (fs.exists(outputDir)) {
        fs.delete(outputDir, true);
        LOGGER.info("Delete hfile output path.");
    }

    // run mapreduce to generate hfile
    Job job = createSubmittableJob(conf, targetTableSpec);
    job.waitForCompletion(true);
    return job;
  }

  public static void loadIncrementalHFiles(Configuration conf, Job job,
      long startTime) throws Exception {
    String hbaseGeneratedHfilesOutputPath =
        conf.get(addPropertyPrefix(Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH));
    if (job.isSuccessful()) {
      if (hbaseGeneratedHfilesOutputPath != null) {
        try {
          // chmod 777 for hfile
          LOGGER.info("Executing Shell:\n" + "/bin/sh -c"
              + " sudo -u hdfs hadoop fs -chmod -R 777 "
              + hbaseGeneratedHfilesOutputPath);
          Process prs = Runtime.getRuntime().exec(
                  new String[] {"/bin/sh",
                		  "-c",
                      "sudo -u hdfs hadoop fs -chmod -R 777 "
                          + hbaseGeneratedHfilesOutputPath });
          
          String line;
          InputStream es = prs.getErrorStream();
          BufferedReader br = new BufferedReader(new InputStreamReader(es));
          while ((line = br.readLine()) != null) {
            LOGGER.info(line);
            if (line.contains("FAILED") || line.contains("ERROR")
                || line.contains("Failed") || line.contains("Error")) {
              LOGGER.error(line);
              throw new RuntimeException(line);
            }
          }

          // give hbase bulkload hfile output permission
          LOGGER.info("Executing Shell:\n" + "/bin/sh -c"
              + " sudo -u hdfs hadoop fs -chown -R hbase "
              + hbaseGeneratedHfilesOutputPath);
          prs = Runtime.getRuntime().exec(
                  new String[] {"/bin/sh",
                		  "-c",
                      "sudo -u hdfs hadoop fs -chown -R hbase "
                          + hbaseGeneratedHfilesOutputPath });
          es = prs.getErrorStream();
          br = new BufferedReader(new InputStreamReader(es));
          while ((line = br.readLine()) != null) {
            LOGGER.info(line);
            if (line.contains("FAILED") || line.contains("ERROR")
                || line.contains("Failed") || line.contains("Error")) {
              LOGGER.error(line);
              throw new RuntimeException(line);
            }
          }

          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        // LoadIncrementalHFiles to bulk load data from hfile into hbase table
        if (hbaseGeneratedHfilesOutputPath != null) {
          String[] args = { hbaseGeneratedHfilesOutputPath,
                  conf.get(addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME)) };
          int ret = ToolRunner.run(
              new LoadIncrementalHFiles(HBaseConfiguration.create()), args);
          LOGGER.info("Successfully Bulk Load data into HBase table!");
          System.out.println("The Total Time of Bulk Load is: "
              + (System.currentTimeMillis() - startTime) + " millisecond!");
          System.exit(ret);
        }
      } else {
        LOGGER.info("Successfully Direct Load data into HBase table!");
        System.out.println("The Total Time of Direct Load is: "
            + (System.currentTimeMillis() - startTime) + " millisecond!");
      }
    } else {
      throw new Exception("Failed to Load data into HBase table");
    }
  }

  private static Job createSubmittableJob(Configuration conf,
      TargetTableSpec tableSpec) throws Exception {
    String tableName = tableSpec.getTableName();
    String jobPrefixName = null;
    if (conf.getBoolean(addPropertyPrefix(Constants.EXTENDEDHBASEROWCONVERTER),
        false)) {
      jobPrefixName = "ExtendedBulkLoad";
    } else {
      if (conf.getBoolean("combineInputFiles", false)) {
        jobPrefixName = "CustomizedBulkLoadWithCombineInputFiles";
      } else {
        jobPrefixName = "CustomizedBulkLoad";
      }
    }
    Job job = new Job(conf, jobPrefixName + "_" + tableName);
    FileInputFormat.setInputPaths(job,
        conf.get(addPropertyPrefix(Constants.HDFS_SOURCE_FILE_INPUT_PATH)));
    FileInputFormat.setInputDirRecursive(job, conf.getBoolean(
        addPropertyPrefix(Constants.HDFS_SOURCE_INPUT_DIRS_RECURSIVE), false));

    if (conf.getBoolean(addPropertyPrefix(Constants.EXTENDEDHBASEROWCONVERTER),
        false)) {
      job.setJarByClass(ExtendedHBaseRowMapper.class);
      job.setMapperClass(ExtendedHBaseRowMapper.class);
      job.setInputFormatClass(TextInputFormat.class);
    } else {
      if (conf.getBoolean("combineInputFiles", false)) {
        job.setJarByClass(CustomizedCombineHBaseRowMapper.class);
        job.setMapperClass(CustomizedCombineHBaseRowMapper.class);
        job.setInputFormatClass(CombineTextInputFormat.class);
      } else {
        job.setJarByClass(CustomizedHBaseRowMapper.class);
        job.setMapperClass(CustomizedHBaseRowMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
      }
    }

    String hfileOutPath =
        conf.get(addPropertyPrefix(Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH));
    if (hfileOutPath != null) {
      HTable table = new HTable(conf, tableName);
      Path outputDir = new Path(hfileOutPath);
      FileOutputFormat.setOutputPath(job, outputDir);

      job.setReducerClass(PutSortReducer.class);
      job.setMapOutputKeyClass(ImmutableBytesWritable.class);
      job.setMapOutputValueClass(Put.class);
      HFileOutputFormat2.configureIncrementalLoad(job, table);
    } else {
      // No reducers. Just write straight to table. Call
      // initTableReducerJob
      // to set up the TableOutputFormat.
      TableMapReduceUtil.initTableReducerJob(tableName, null, job);
      job.setNumReduceTasks(0);
    }

    TableMapReduceUtil.addDependencyJars(job);
    // TableMapReduceUtil.addDependencyJars(job.getConfiguration(),
    // com.google.common.base.Function.class);
    //addDependencyValidatorClass(conf, job);
    
    return job;
  }

  private static void addDependencyValidatorClass(Configuration conf, Job job) {
    // handle validator class
    String validatorClassName =
        conf.get(addPropertyPrefix(Constants.VALIDATOR_CLASS));
    if (!Util.checkIsEmpty(validatorClassName)) {
      try {
        Class<?> validatorClass = Class.forName(validatorClassName);
        LOGGER.info("Load " + validatorClassName);
        TableMapReduceUtil.addDependencyJars(job.getConfiguration(),
            validatorClass);
        LOGGER.info("Add dependency Jar By class " + validatorClass.getName());
      } catch (Exception e) {
        LOGGER.error("Error in setting up bulkload's validator", e);
        throw new RuntimeException(e);
      }
    }
  }

}
