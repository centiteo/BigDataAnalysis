package com.cloudera.bigdata.analysis.dataload.mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.exception.FormatException;
import com.cloudera.bigdata.analysis.dataload.exception.RecordValidator;
import com.cloudera.bigdata.analysis.dataload.source.ParsedLine;
import com.cloudera.bigdata.analysis.dataload.source.TextRecordSpec;
import com.cloudera.bigdata.analysis.dataload.transform.CustomizedHBaseRowConverter;
import com.cloudera.bigdata.analysis.dataload.transform.TargetTableSpec;
import com.cloudera.bigdata.analysis.dataload.util.BulkLoadUtils;

/**
 * Write table content out to files in hdfs.
 */
public class CustomizedHBaseRowMapper extends
    Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(CustomizedHBaseRowMapper.class);
  private String hbaseTargetTableName;
  private String hbaseTargetTableSplitKeySpec;
  private String hbaseTargetTableCellMapString;
  private boolean hbaseTargetWriteToWAL;
  private Put[] indexPuts;
  private boolean buildIndex;
  private Configuration conf;

  public CustomizedHBaseRowConverter converter;

  private Counter badLineCount;
  private static final int EXCEPTION_LOG_PUT_BATCH_SIZE = 500;
  private RecordValidator validator;
  private List<Put> exceptionLogPutCache;
  // Log the malformed record into a hbase table
  HTable exceptionLogTable;
  public static final SimpleDateFormat df = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss.SSS");
  private boolean createMalformedTable = false;

  // timestamp for all inserted rows
  private long ts = 0;

  public void incrementBadLineCount(int count) {
    this.badLineCount.increment(count);
  }

  /**
   * Handles initializing this class with objects specific to it (i.e., the
   * parser). Common initialization that might be leveraged by a subsclass is
   * done in <code>doSetup</code>. Hence a subclass may choose to override this
   * method and call <code>doSetup</code> as well before handling it's own
   * custom params.
   * 
   * @param context
   */
  @Override
  protected void setup(Context context) {
    doSetup(context);
  }

  /**
   * Handles common parameter initialization that a subclass might want to
   * leverage.
   * 
   * @param context
   */
  protected void doSetup(Context context) {
    badLineCount = context.getCounter("CustomizedHBaseRowMapper", "Bad Lines");

    //instantiateValidator(context);

    conf = context.getConfiguration();

    hbaseTargetTableName =
        conf.get(BulkLoadUtils
            .addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME));
    hbaseTargetTableSplitKeySpec =
        conf.get(BulkLoadUtils
            .addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC));
    hbaseTargetTableCellMapString =
        conf.get(BulkLoadUtils
            .addPropertyPrefix(Constants.HBASE_TARGET_TABLE_CELL_MAPPING));
    hbaseTargetWriteToWAL =
        conf.getBoolean(BulkLoadUtils
            .addPropertyPrefix(Constants.HBASE_TARGET_WRITE_TO_WAL_FLAG), false);
    createMalformedTable =
        conf.getBoolean(
            BulkLoadUtils.addPropertyPrefix(Constants.CREATE_MALFORMED_TABLE),
            false);
    buildIndex =
        conf.getBoolean(BulkLoadUtils.addPropertyPrefix(Constants.BUILD_INDEX),
            false);

    String encoding =
        conf.get(BulkLoadUtils
            .addPropertyPrefix(Constants.HDFS_SOURCE_FILE_ENCODING));
    String fieldDelimiter =
        conf.get(BulkLoadUtils
            .addPropertyPrefix(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_DELIMITER));
    String textRecordSpec = conf.get("textRecordSpec");

    String importDateString =
        conf.get(BulkLoadUtils.addPropertyPrefix(Constants.IMPORT_DATE));

    try {
      TextRecordSpec recordSpec = new TextRecordSpec(textRecordSpec, encoding,
          fieldDelimiter);
      TargetTableSpec tableSpec = new TargetTableSpec(hbaseTargetTableName,
          hbaseTargetTableCellMapString, hbaseTargetTableSplitKeySpec);

      // specify hbase row converter here, extended converter or customized
      // converter, give customized converter example here
      converter = new CustomizedHBaseRowConverter(recordSpec, tableSpec, conf);

      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      if (importDateString != null && !importDateString.isEmpty()) {
        Date importDate = df.parse(importDateString);
        ts = importDate.getTime();
      }

      converter.setTimeStamp(ts);

      // if there are exceptional records, commit it to excpetion_log every 500
      // records
      if (createMalformedTable) {
        exceptionLogPutCache = new ArrayList<Put>(EXCEPTION_LOG_PUT_BATCH_SIZE);
        try {
          exceptionLogTable = new HTable(context.getConfiguration(),
              Hdfs2HBaseWorker.EXCEPTION_LOG_TABLE_NAME);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Convert a line of TXT text into an HBase table row.
   */
  @Override
  public void map(LongWritable offset, Text value, Context context)
      throws IOException {
    String lineStr = new String(value.getBytes(), 0, value.getLength(),
        converter.getRecordSpec().getEncoding());
    try {
      //validator.validate(lineStr);
      ParsedLine parsed = new ParsedLine(converter.getRecordSpec(), lineStr);
      Put put = converter.convertToRow(parsed, hbaseTargetWriteToWAL,
          buildIndex);
      ImmutableBytesWritable row = new ImmutableBytesWritable();

      // write index rows if buildIndex is true
      indexPuts = converter.convertToIndex(parsed, hbaseTargetWriteToWAL,
          buildIndex);
      if (indexPuts != null) {
        for (Put p : indexPuts) {
          row = new ImmutableBytesWritable(p.getRow());
          context.write(row, p);
        }
      }

      // write row
      row = new ImmutableBytesWritable(put.getRow());
      context.write(row, put);

    } catch (FormatException badLineEx) {
      if (createMalformedTable) {
        handleBadLine(context, lineStr, offset, badLineEx);
      } else {
        System.err
            .println("Bad line at offset: " + offset.get() + ":\n"
                + " --> Malformat Line: " + lineStr + "\n"
                + badLineEx.getMessage());
        badLineEx.printStackTrace();
        incrementBadLineCount(1);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void instantiateValidator(Context context) {
    String validatorClassName = context.getConfiguration().get(
        "validatorClass",
        "com.intel.bigdata.analysis.dataload.exception.DefaultRecordValidator");
    try {
      Class<?> validatorClass = Class.forName(validatorClassName);
      LOGGER.info("Instantiating validator: " + validatorClass.getName());
      validator = (RecordValidator) ReflectionUtils.newInstance(validatorClass,
          context.getConfiguration());
    } catch (Exception e) {
      LOGGER.error("Error in setting up bulkload's validator", e);
      throw new RuntimeException(e);
    }
  }

  private void handleBadLine(Context context, String lineStr,
      LongWritable offset, FormatException ex) throws IOException {
    String errorMsg = "Malformat at split: " + context.getInputSplit()
        + " offset at:" + offset.get();
    LOGGER.warn(errorMsg);
    Put put =
        new Put(Bytes.toBytes(context.getConfiguration().get(
            BulkLoadUtils.addPropertyPrefix(Constants.HBASE_TARGET_TABLE_NAME))
        + "|" + df.format(new Date()) + "|" + UUID.randomUUID()));
    put.add(Hdfs2HBaseWorker.EXCEPTION_LOG_TABLE_COLUMN_FAMILY_BYTES,
        Hdfs2HBaseWorker.EXCEPTION_LOG_TABLE_COLUMN_QUALIFIER_RAW_LINE_BYTES,
        Bytes.toBytes(lineStr));
    put.add(Hdfs2HBaseWorker.EXCEPTION_LOG_TABLE_COLUMN_FAMILY_BYTES,
        Hdfs2HBaseWorker.EXCEPTION_LOG_TABLE_COLUMN_QUALIFIER_ERROR_INFO_BYTES,
        Bytes.toBytes(errorMsg + " MSG:" + ex.getMessage()));

    int count = exceptionLogPutCache.size();
    if (count == EXCEPTION_LOG_PUT_BATCH_SIZE) {
      exceptionLogTable.put(exceptionLogPutCache);
      incrementBadLineCount(EXCEPTION_LOG_PUT_BATCH_SIZE);
      exceptionLogPutCache.clear();
    }
    if (count < EXCEPTION_LOG_PUT_BATCH_SIZE) {
      exceptionLogPutCache.add(put);
    }
  }

  @Override
  protected void cleanup(Context context) {
    try {
      if (createMalformedTable) {
        if (!exceptionLogPutCache.isEmpty()) {
          exceptionLogTable.put(exceptionLogPutCache);
          incrementBadLineCount(exceptionLogPutCache.size());
          exceptionLogPutCache.clear();
        }
      }
    } catch (IOException ex) {
      LOGGER.warn("", ex);
      throw new RuntimeException(ex);
    }
  }
}
