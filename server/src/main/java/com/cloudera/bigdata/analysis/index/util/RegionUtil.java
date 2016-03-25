package com.cloudera.bigdata.analysis.index.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.hbase.regionserver.HRegion;
import org.apache.hadoop.hbase.util.Bytes;

public abstract class RegionUtil {

  public static byte[] getRegionStartKey(HRegion region) {
    return null;
    // byte[] regionStartKey = region.getStartKey();
    //
    // if (ArrayUtils.isEmpty(regionStartKey)) {
    // regionStartKey = Bytes.toBytes(RowKeyUtil.formatToRowKeyPrefix(0));
    // }
    // return regionStartKey;
  }
}
