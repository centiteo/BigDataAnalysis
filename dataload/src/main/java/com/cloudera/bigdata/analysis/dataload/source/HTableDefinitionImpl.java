package com.cloudera.bigdata.analysis.dataload.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.util.CommonUtils;
import com.cloudera.bigdata.analysis.dataload.util.RegionSplitKeyUtils;
import com.cloudera.bigdata.analysis.dataload.util.Util;
import com.cloudera.bigdata.analysis.generated.BaseColumnFamilyType;

public class HTableDefinitionImpl implements HTableDefinition {

  private static final Logger LOG = LoggerFactory
      .getLogger(HTableDefinitionImpl.class);

  final private String tableName;
  final private List<? extends BaseColumnFamilyType> cfSpecList;
  final private String splitPrefix;
  final private int splitSize;
  private int regionQuantity = 1;
  private long memStoreSize = 64 * 1024 * 1024;

  public HTableDefinitionImpl(String tableName,
      List<? extends BaseColumnFamilyType> cfSpecList, String splitPrefix,
      int splitSize) {
    this.tableName = tableName;
    this.cfSpecList = cfSpecList;
    this.splitPrefix = splitPrefix;
    this.splitSize = splitSize;
  }

  public HTableDefinitionImpl(String tableName,
      List<? extends BaseColumnFamilyType> cfSpecList, String splitPrefix,
      int splitSize, int regionQuantity) {
    this.tableName = tableName;
    this.cfSpecList = cfSpecList;
    this.splitPrefix = splitPrefix;
    this.splitSize = splitSize;
    this.regionQuantity = regionQuantity;
  }

  public HTableDefinitionImpl(String tableName,
      List<? extends BaseColumnFamilyType> cfSpecList, String splitPrefix,
      int splitSize, int regionQuantity, int memStore) {
    this(tableName, cfSpecList, splitPrefix, splitSize, regionQuantity);

  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public HColumnDescriptor[] getColumnFamilies() {
    // TODO Auto-generated method stub
    List<HColumnDescriptor> descriptors = new ArrayList<HColumnDescriptor>();
    if (LOG.isDebugEnabled()) {
      LOG.debug("cfSpecList size : " + cfSpecList.size());
    }

    for (BaseColumnFamilyType familyType : cfSpecList) {
      LOG.debug("family : " + familyType.getFamilyName());
      LOG.debug("compression type : " + familyType.getCompressionType().name());

      HColumnDescriptor descriptor = new HColumnDescriptor(
          familyType.getFamilyName());
      if (familyType.getCompressionType().name() != null)
        descriptor.setCompressionType(Algorithm.valueOf(familyType
            .getCompressionType().name()));
      descriptor.setMaxVersions(familyType.getMaxVersion());

      descriptors.add(descriptor);
    }
    return descriptors.toArray(new HColumnDescriptor[] {});
  }

  @Override
  public byte[][] getSplitKeys() throws Exception {
    if (regionQuantity < Constants.DEFAULT_DATALOAD_TABLE_REGION_QUANTITY) {
      byte[][] splitKeys = RegionSplitKeyUtils.calcSplitKeys(regionQuantity);
      if (LOG.isDebugEnabled()) {
        LOG.debug("splitKeys size: " + splitKeys.length);
        for (int i = 0; i < splitKeys.length; i++) {
          LOG.debug("splitKeys="
              + CommonUtils.convertByteArrayToString(splitKeys[i]));
        }
      }
      return splitKeys;
    } else {
      return Util.genSplitKeys(splitPrefix, splitSize);
    }
  }

  @Override
  public long getMemStoreFlushSize() {
    return memStoreSize;
  }

  @Override
  public void close() throws IOException {
  }

  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof HTableDefinitionImpl)) {
      return false;
    }

    HTableDefinitionImpl thatImpl = (HTableDefinitionImpl) obj;
    if (tableName.equals(thatImpl.tableName)) {
      return true;
    }

    return false;
  }

}
