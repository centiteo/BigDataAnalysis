package com.intel.bigdata.analysis.hbase.delete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.HConstants.OperationStatusCode;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.HRegion;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.OperationStatus;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

public class BatchEndpoint extends BaseEndpointCoprocessor implements
    BatchProtocol {
  static final Log LOG = LogFactory.getLog(BatchEndpoint.class);
  static final int PUT_SIZE = 1000;

  @Override
  public <T, S> long batch_update(String tableName, List<KeyValue> kvs,
      String batchSize, Scan scan) throws IOException {
    RegionCoprocessorEnvironment env = (RegionCoprocessorEnvironment) this
        .getEnvironment();
    HRegion region = env.getRegion();
    InternalScanner scanner = (InternalScanner) region.getScanner(scan);

    long totalRowsUpdated = 0L;

    boolean hasMore = true;
    int size = Integer.parseInt(batchSize);
    while (hasMore) {
      List<List<KeyValue>> updateRows = new ArrayList<List<KeyValue>>(size);
      for (int i = 0; i < size; i++) {
        List<KeyValue> results = new ArrayList<KeyValue>();
        hasMore = scanner.next(results);
        if (results.size() > 0) {
          updateRows.add(results);
        }
        if (!hasMore) {
          break;
        }
      }
      if (updateRows.size() > 0) {
        //LOG.info("=====enter");
        Pair<Mutation, Integer>[] putWithLockArr = new Pair[updateRows.size()];
        int i = 0;
        for (List<KeyValue> updateRow : updateRows) {
          Put put = new Put(updateRow.get(0).getRow());
          put.setWriteToWAL(false);
          //LOG.info("====================rowkey"
           //   + Bytes.toStringBinary(updateRow.get(0).getRow()));
          for (KeyValue tmp : kvs) {
            put.add(tmp.getFamily(), tmp.getQualifier(), tmp.getValue());
          }
          put.setWriteToWAL(false);
          putWithLockArr[i++] = new Pair<Mutation, Integer>(put, null);
        }
        OperationStatus[] opStatus = region.batchMutate(putWithLockArr);
        for (i = 0; i < opStatus.length; i++) {
          if (opStatus[i].getOperationStatusCode() != OperationStatusCode.SUCCESS) {
            break;
          }
          totalRowsUpdated++;
        }
      }
    }
    // LOG.info("============totalRowsUpdated"+totalRowsUpdated);
    scanner.close();
    return totalRowsUpdated;
  }

  @Override
  public <T, S> long batch_delete(String tableName, String batchSize, Scan scan)
      throws IOException {
    RegionCoprocessorEnvironment env = (RegionCoprocessorEnvironment) this
        .getEnvironment();
    HRegion region = env.getRegion();
    scan.setStartRow(region.getStartKey());
    scan.setStopRow(region.getEndKey());
    
    InternalScanner scanner = (InternalScanner) region.getScanner(scan);

    long totalRowsDeleted = 0L;

    int size = Integer.parseInt(batchSize);
   // LOG.info("===== batchSize " + batchSize);
    
    List<KeyValue> results = new ArrayList<KeyValue>();
    
    while (scanner.next(results)) {
      int length = results.size();
      int i=0;
      Pair<Mutation, Integer>[] deleteWithLockArr = new Pair[length];
      for(KeyValue deleteRow : results) {
      //  LOG.info("=====enter");
    	  Delete delete = new Delete(deleteRow.getRow());
    	  delete.setWriteToWAL(false);
      //  LOG.info("====================rowkey"
    	  //       + Bytes.toStringBinary(deleteRow.getRow()));
        deleteWithLockArr[i++] = new Pair<Mutation, Integer>(delete, null);
      }
      
      OperationStatus[] opStatus = region.batchMutate(deleteWithLockArr);
      for (i = 0; i < opStatus.length; i++) {
        if (opStatus[i].getOperationStatusCode() != OperationStatusCode.SUCCESS) {
          break;
        }
        totalRowsDeleted++;
      }
      i=0;
      results.clear();
    }

    scanner.close();
    //LOG.info("============totalRowsUpdated" + totalRowsDeleted);
    return totalRowsDeleted;
  }
}

