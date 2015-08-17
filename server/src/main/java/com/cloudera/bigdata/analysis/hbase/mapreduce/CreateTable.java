package com.cloudera.bigdata.analysis.hbase.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

import com.cloudera.bigdata.analysis.dataload.util.Util;

/**
 * Assume the leading RowKey only contains Alphabet and Digit
 * 
 * @author centiteo
 *
 */
public class CreateTable {

  /**
   * <tableName> <splitSize> [<splitPrefix>]
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    if (args.length < 2) {
      System.out.println("Usage: "
          + "com.cloudera.bigdata.analysis.hbase.mapreduce.CreateTable"
          + " <tableName>" + " <splitSize>"
          + " [<splitPrefix>]   #comma separated string list");
    }

    String tableName = args[0];
    int splitSize = Integer.parseInt(args[1]);
    String splitPrefix = "";
    if (args.length > 2) {
      splitPrefix = args[2];
    }

    createTableInner(tableName, splitSize, splitPrefix);
  }

  public static void createTableInner(String tableName, int splitSize,
      String splitPrefix) throws Exception {
    Configuration conf = HBaseConfiguration.create();
    HBaseAdmin hAdmin = new HBaseAdmin(conf);

    HColumnDescriptor columnDescriptor =
        new HColumnDescriptor(Bytes.toBytes("f"));
    columnDescriptor.setCompressionType(Algorithm.SNAPPY);
    columnDescriptor.setMaxVersions(3);

    HTableDescriptor descriptor =
        new HTableDescriptor(TableName.valueOf(tableName));
    descriptor.addFamily(columnDescriptor);
    hAdmin.createTable(descriptor,
        Util.genSplitKeysAlphaDig(splitPrefix, splitSize));

    hAdmin.close();
  }

}
