package com.intel.bigdata.analysis.hbase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter.RowKeyRange;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class FilterBuilder {
  static final Log LOG = LogFactory.getLog(FilterBuilder.class);

  public FilterBuilder() {

  }

  public static SingleColumnValueFilter buildSCVF(String filterStr) {
    String[] array = filterStr.split("\\,");
    LOG.info("*********** 0. " + array[0]);
    byte[] family = Bytes.toBytes(array[0]);
    LOG.info("*********** 00. " + family.toString());
    LOG.info("*********** 1. " + array[1]);
    byte[] qualifier = Bytes.toBytes(array[1]);
    LOG.info("*********** 11. " + qualifier.toString());
    LOG.info("*********** 2. " + array[2]);
    CompareOp op = judgeCompareOp(array[2]);
    LOG.info("*********** 22. " + op.toString());
    LOG.info("*********** 3. " + array[3]);
    byte[] value = Bytes.toBytes(array[3]);
    LOG.info("*********** 33. not parse " + value.toString());
    return new SingleColumnValueFilter(family, qualifier, op, value);
  }

  public static MultiColumnValueFilter buildMCVF(String filterStr) {
    String[] array = filterStr.split("\\,");
    byte[] family = Bytes.toBytes(array[0]);
    byte[] qualifier = Bytes.toBytes(array[1]);
    CompareOp op = judgeCompareOp(array[2]);
    byte[] value = Bytes.toBytes(Double.parseDouble(array[3]));
    byte[] split = Bytes.toBytes(array[4]);
    return new MultiColumnValueFilter(family, qualifier, op, value, split);
  }

  public static CompareOp judgeCompareOp(String op) {
    CompareOp cop;
    if (op.equals(">=")) {
      cop = CompareOp.GREATER_OR_EQUAL;
    } else if (op.equals(">")) {
      cop = CompareOp.GREATER;
    } else if (op.equals("=")) {
      cop = CompareOp.EQUAL;
    } else if (op.equals("!=")) {
      cop = CompareOp.NOT_EQUAL;
    } else if (op.equals("<=")) {
      cop = CompareOp.LESS_OR_EQUAL;
    } else if (op.equals("<")) {
      cop = CompareOp.LESS;
    } else {
      cop = CompareOp.NO_OP;
    }
    return cop;
  }

  public static MultiRowRangeFilter buildMRRF(List<String> list, String suffix,
      String delimiter) {
    MultiRowRangeFilter filter = null;
    // String[] array = prefixList.split("\\" + split);
    // List<String> list = Arrays.asList(array);
    Collections.sort(list);
    List<RowKeyRange> rowRangeList = new ArrayList<RowKeyRange>();
    for (String id : list) {
      LOG.info(suffix);
      LOG.info(id + delimiter + suffix);
      byte[] startRowkey = Bytes.toBytes(id + delimiter + suffix);
      LOG.info(new String(startRowkey));
      int length = startRowkey.length;
      byte[] stopRowKey = new byte[length];
      System.arraycopy(startRowkey, 0, stopRowKey, 0, length);
      stopRowKey[length - 1] = (byte) (startRowkey[length - 1] + 1);
      rowRangeList.add(new RowKeyRange(startRowkey, stopRowKey));
    }
    try {
      filter = new MultiRowRangeFilter(rowRangeList);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return filter;
  }

}
