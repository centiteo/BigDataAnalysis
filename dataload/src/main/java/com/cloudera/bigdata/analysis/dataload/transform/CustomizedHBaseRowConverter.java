package com.cloudera.bigdata.analysis.dataload.transform;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.exception.ETLException;
import com.cloudera.bigdata.analysis.dataload.source.ParsedLine;
import com.cloudera.bigdata.analysis.dataload.source.TextRecordSpec;
import com.cloudera.bigdata.analysis.dataload.util.BulkLoadUtils;
import com.cloudera.bigdata.analysis.dataload.util.CommonUtils;
import com.cloudera.bigdata.analysis.dataload.util.ExpressionUtils;
import com.cloudera.bigdata.analysis.index.Constants;
import com.cloudera.bigdata.analysis.index.IndexEntryBuilderGroup;
import com.cloudera.bigdata.analysis.index.util.RowKeyUtil;

public class CustomizedHBaseRowConverter {
  private Configuration conf;
  private TextRecordSpec recordSpec;
  private TargetTableSpec rowSpec;
  private long timeStamp;
  private IndexEntryBuilderGroup indexEntryBuilderGroup;
  private byte[] rowKey;
  private byte[] randomRowKeyPrefix;
  private Put[] indexPuts;
  private Put put;
  private byte[] regionStartKey;

  private static final Logger LOG = LoggerFactory
      .getLogger(CustomizedHBaseRowConverter.class);

  public CustomizedHBaseRowConverter(TextRecordSpec recordSpec,
      TargetTableSpec tableSpec, Configuration conf) {
    this.recordSpec = recordSpec;
    this.rowSpec = tableSpec;
    this.conf = conf;
    //indexEntryBuilderGroup = IndexEntryBuilderGroup.getInstance(CommonUtils.convertStringToByteArray(Constants.CURRENT_TABLE));
    if (conf
        .getBoolean(
            BulkLoadUtils
                .addPropertyPrefix(com.cloudera.bigdata.analysis.dataload.Constants.BUILD_INDEX),
            false)) {
      LOG.debug("indexEntryBuilderGroup: "
          + CommonUtils.convertByteArrayToString(Constants.CURRENT_TABLE
              .getBytes()));
    }
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public TextRecordSpec getRecordSpec() {
    return recordSpec;
  }

  public Put convertToRow(ParsedLine line, boolean writeToWAL,
      boolean buildIndex) throws ETLException {
    String rowKeyValue = new String();
    if (buildIndex) {
      // user hash + UUID as rowkey
      randomRowKeyPrefix = RowKeyUtil.genRandomRowKeyPrefix();
      rowKey = RowKeyUtil.genRowKey(randomRowKeyPrefix);
      rowKeyValue = CommonUtils.convertByteArrayToString(rowKey);
    } else {
      String rowKeyValuePre = ExpressionUtils.calculate(rowSpec.getRowKeySpec()
          .getSpecString(), line.getFieldMap());
      // UUID has 10000000 kinds randomly
      rowKeyValue = rowKeyValuePre + Constants.COLUMN_ENTRY_SEPARATOR
          + UUID.randomUUID().toString();
    }

    put = new Put(Bytes.toBytes(rowKeyValue), timeStamp);
    put.setWriteToWAL(writeToWAL);
    for (Map.Entry<String, TargetTableSpec.ColumnSpec> e : rowSpec
        .getColumnMap().entrySet()) {
      String columnName = e.getKey();
      String columnSpecString = e.getValue().getSpecString();

      String[] familyQualifierPairs = StringUtils
          .splitByWholeSeparatorPreserveAllTokens(columnName,
              Constants.FAMILY_QUALIFIER_SPLIT_CHARACTER);

      String familyName = familyQualifierPairs[0];

      String qualifierName = familyQualifierPairs[1];
      String qualifierValue = ExpressionUtils.calculate(columnSpecString,
          line.getFieldMap());

      put.add(Bytes.toBytes(familyName), Bytes.toBytes(qualifierName),
          Bytes.toBytes(qualifierValue));
    }
    return put;
  }

  public Put[] convertToIndex(ParsedLine line, boolean writeToWAL,
      boolean buildIndex) throws ETLException {
    if (buildIndex) {
      LOG.debug("Intercepted Put: " + Bytes.toStringBinary(rowKey));
      // only if a record put...
      if (RowKeyUtil.isRecord(rowKey)) {
        LOG.debug("rowkey: " + CommonUtils.convertByteArrayToString(rowKey));
        regionStartKey = CommonUtils.findRegionStarKey(
                    conf.get(BulkLoadUtils
                        .addPropertyPrefix(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC)),
            CommonUtils.convertByteArrayToString(randomRowKeyPrefix));
        indexPuts = indexEntryBuilderGroup.build(regionStartKey, put);
      }
      return indexPuts;
    } else {
      return null;
    }
  }
}
