package com.cloudera.bigdata.analysis.dataload.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.index.util.RowKeyUtil;

/*
 * The class is used to generate a fine tuned region splits based on an adequate sampling
 * 
 * You input a file containing record distribution information based on a sample, for example data of one 
 * out of a year
 */

public class RegionSplitKeyUtils {

  /**
   * @param args
   */
  public static void main(String[] args) {

    if (args.length != 2) {
      System.out
          .println("Usage RegionSplitKeyGen <recordCountFile> <recordsPerRegion>");
      System.exit(1);
    }

    // wap_dist.txt 500000
    String file = args[0];

    long sampleRecordsPerRegion = Long.parseLong(args[1]);

    String hbaseTargetTableSplitKeySpec = genSplitKeysFromFile(file,
        sampleRecordsPerRegion);
    System.out.println(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC + "="
        + hbaseTargetTableSplitKeySpec);

  }

  /*
   * @param path the file path generate
   */
  public static String genSplitKeysFromFile(String sampleInfoFile,
      long sampleRecordsPerRegion) {

    StringBuffer sb = new StringBuffer();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(sampleInfoFile));
      String line = reader.readLine();
      String[] ss = line.split(",");
      if (ss.length != 2) {
        System.out.println("Bad line found: " + line);
      }
      long n = Long.parseLong(ss[ss.length - 1].trim());
      long records = n;

      long max = Long.MIN_VALUE;
      long min = Long.MAX_VALUE;

      while ((line = reader.readLine()) != null) {
        ss = line.split(",");
        if (ss.length != 2) {
          System.out.println("Bad line found: " + line);
        }
        n = Long.parseLong(ss[ss.length - 1].trim());
        if (records + n > sampleRecordsPerRegion) {
          sb.append(ss[0]).append(",");

          if (records > max)
            max = records;
          if (records < min)
            min = records;
          records = 0;
        } else {
          records += n;
        }
      }

      System.out.println("# Max rows per region: " + max);
      System.out.println("# Min rows per region: " + min);

      reader.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    String result = sb.toString();
    if (result.endsWith(","))
      result = result.substring(0, result.length() - 1);

    return result;
  }

  /**
   * region splits must balance! it's not good enough to just divide region
   * count. for example: 11 prefixes, 3 regions, each region has 3 prefixes, but
   * last region will take 5 prefixes, it's not balance, we should re-balance
   * the rest 2 prefixes.
   */
  public static byte[][] calcSplitKeys(int regionQuantity) {
    int avgPrefixCount = Constants.ROWKEY_PREFIX_MAX_VALUE / regionQuantity;
    int residualPrefixCount = Constants.ROWKEY_PREFIX_MAX_VALUE
        % regionQuantity;
    byte[][] splitKeys = new byte[regionQuantity - 1][];
    for (int i = 1; i <= regionQuantity - 1; i++) {
      if (i <= residualPrefixCount) {
        splitKeys[i - 1] = Bytes.toBytes(RowKeyUtil.formatToRowKeyPrefix(i
            * avgPrefixCount + i));
      } else {
        splitKeys[i - 1] = Bytes.toBytes(RowKeyUtil.formatToRowKeyPrefix(i
            * avgPrefixCount + residualPrefixCount));
      }
    }
    return splitKeys;
  }

}
