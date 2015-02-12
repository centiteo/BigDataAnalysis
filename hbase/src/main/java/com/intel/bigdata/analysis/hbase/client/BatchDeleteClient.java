package com.intel.bigdata.analysis.hbase.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.intel.bigdata.analysis.hbase.Constants;
import com.intel.bigdata.analysis.hbase.delete.BatchProtocol;
import com.intel.bigdata.analysis.hbase.query.FilterBuilder;

public class BatchDeleteClient {
  static final Log LOG = LogFactory.getLog(BatchDeleteClient.class);

  private Properties p = new Properties();;

  public BatchDeleteClient() {

  }

  public void call_batch_delete(String file) {
    try {
      LOG.info("*********** file is " + file);
      p.load(new FileInputStream(file));
      String tableName = p.getProperty(Constants.TABLENAME);
      String filterStr = p.getProperty(Constants.FILTERSTR);
      String batchSize = p.getProperty(Constants.BATCHSIZE);

      LOG.info("*********** table is " + tableName);
      LOG.info("*********** filterStr is " + filterStr);
      LOG.info("*********** batchSize is " + batchSize);

      FilterList filter = new FilterList();
      filter.addFilter(FilterBuilder.buildSCVF(filterStr));
      filter.addFilter(new KeyOnlyFilter());
      LOG.info("*********** build Filter ");
      Scan scan = new Scan();
      String[] a = filterStr.split("\\,");
      scan.addColumn(Bytes.toBytes(a[0]), Bytes.toBytes(a[1]));
      if (filter != null) {
        scan.setFilter(filter);
      }
      scan.setCacheBlocks(false);
      LOG.info("*********** scan setFilter ");

      scan.setMaxVersions(1);
      scan.setCaching(200);
      LOG.info("*********** call deeper call_batch_delete ");
      call_batch_delete(tableName, scan, batchSize);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void call_batch_delete(final String tableName, final Scan scan,
      final String batchSize) {
    try {
      Configuration conf = HBaseConfiguration.create();
      HTable table = new HTable(conf, tableName);
      LOG.info("*********** table is " + tableName);
      // my code
      Map<byte[], Long> results;
      results = table.coprocessorExec(BatchProtocol.class, null, null,
          new Batch.Call<BatchProtocol, Long>() {
            public Long call(BatchProtocol prtocol) throws IOException {
              LOG.info("*********** batch_delete ");
              return (Long) prtocol.batch_delete(tableName, batchSize, scan);
            }
          });
      LOG.info("*********** call coprocessor done ");

      printResult(results);
      table.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void printResult(Map<byte[], Long> results) {
    int count = 0;
    for (Map.Entry<byte[], Long> entry : results.entrySet()) {
      count += entry.getValue().longValue();
    }
    LOG.info("=======Operation finishied, " + count
        + " records are operated=======");
  }

  public static void main(String args[]) {
    BatchDeleteClient client = new BatchDeleteClient();
    LOG.info("Usage:");
    LOG.info("delete command: sh delete.sh");
    long startTime = System.currentTimeMillis();
    LOG.info("StartTime ====" + startTime);
    client.call_batch_delete(args[0]);
    long endTime = System.currentTimeMillis();
    long usedTime = endTime - startTime;
    LOG.info("EndTime ====" + endTime);
    LOG.info("Totally used time ======" + usedTime);
  }
}
