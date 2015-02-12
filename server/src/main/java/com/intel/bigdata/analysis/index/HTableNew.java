package com.intel.bigdata.analysis.index;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;

public class HTableNew extends HTable {

  public HTableNew(Configuration conf, String tableName) throws IOException {
    super(conf, tableName);
  }

}
