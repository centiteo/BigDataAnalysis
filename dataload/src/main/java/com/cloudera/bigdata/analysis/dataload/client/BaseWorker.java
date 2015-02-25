package com.cloudera.bigdata.analysis.dataload.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.slf4j.*;

public abstract class BaseWorker implements Worker {
  private static final Logger LOG = LoggerFactory.getLogger(BaseWorker.class);

  protected String configFile;
  protected Configuration conf;
  protected FileSystem fs;
  protected static HBaseAdmin hbaseAdmin;
  //protected ConfigReader configReader;
  protected Properties props;

  public BaseWorker(String config) {
    this.configFile = config;
    //this.configReader = new ConfigReader(config);
    try {
      props = new Properties();
      props.load(new FileInputStream(config));
      conf = HBaseConfiguration.create();
      fs = FileSystem.get(conf);
      hbaseAdmin = new HBaseAdmin(conf);
    } catch (IOException ioe) {
      LOG.error("ERROR: " + ioe);
      throw new RuntimeException(ioe);
    }
  }
  
  protected Properties getProperties(){
	  return props;
  }

  protected abstract void doWork() throws Exception;
}
