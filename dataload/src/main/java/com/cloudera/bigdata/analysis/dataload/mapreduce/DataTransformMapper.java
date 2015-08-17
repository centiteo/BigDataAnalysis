package com.cloudera.bigdata.analysis.dataload.mapreduce;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hdfs.DFSClient.Conf;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.LoadTask;
import com.cloudera.bigdata.analysis.dataload.io.FileObject;
import com.cloudera.bigdata.analysis.dataload.io.FileObjectArrayWritable;
import com.sun.jmx.snmp.tasks.Task;

public class DataTransformMapper<KEYOUT, VALUEOUT> extends
    Mapper<LongWritable, FileObjectArrayWritable, KEYOUT, VALUEOUT> {
  private static final Logger LOG = LoggerFactory
      .getLogger(DataTransformMapper.class);

  private int workerThdNumber;
  private Configuration conf;
  
  protected void setup(Context context) throws IOException,
      InterruptedException {
    conf = context.getConfiguration();

    workerThdNumber = conf.getInt(Constants.THREADS_PER_MAPPER_KEY,
        Constants.DEFAULT_THREADS_PER_MAPPER);

    Path[] files = DistributedCache.getLocalCacheFiles(conf);
    for (Path path : files) {
      if (path.getName().endsWith(conf.get(Constants.INSTANCE_DOC_NAME_KEY))) {
        conf.set(Constants.INSTANCE_DOC_PATH_KEY, path.toString());
      } else if (path.getName().contains("keytab")) {
        conf.set("keytab.path", path.toString());
      }
    }
    System.out.println("******************");
    UserGroupInformation.loginUserFromKeytab("usera@EXAMPLE.COM", context
        .getConfiguration().get("keytab.path"));
    UserGroupInformation ugi = UserGroupInformation.getLoginUser();
    System.out.println("===========" + ugi.getUserName());
  }

  protected void map(LongWritable key, FileObjectArrayWritable value,
      Context context) throws IOException, InterruptedException {

    Writable[] writables = value.get();
    List<FileObject> fileList = new ArrayList<FileObject>();
    for (Writable writable : writables) {
      if (writable instanceof FileObject) {
        fileList.add((FileObject) writable);
      }
    }

    final LoadTask task = new LoadTask(fileList, conf, 1, workerThdNumber);

    UserGroupInformation.loginUserFromKeytab("usera@EXAMPLE.COM", context
        .getConfiguration().get("keytab.path"));
    task.submitJob();
    // UserGroupInformation.getLoginUser().doAs(new PrivilegedAction<Integer>()
    // {
    // public Integer run() {
    // try {
    // return task.submitJob();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // return new Integer(1);
    // }
    // });
  }

  public Integer showTables() {
    HBaseAdmin hBaseAdmin;
    try {
      hBaseAdmin = new HBaseAdmin(conf);
      HTableDescriptor[] htables = hBaseAdmin.listTables();
      for (HTableDescriptor descriptor : htables) {
        System.out.println(descriptor.getTableName().getNameAsString());
      }
      hBaseAdmin.close();
      return new Integer(0);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new Integer(1);
  }
}
