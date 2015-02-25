package com.cloudera.bigdata.analysis.hbase.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.regionserver.metrics.SchemaMetrics;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.cloudera.bigdata.analysis.dataload.util.CommonUtils;
import com.cloudera.bigdata.analysis.hbase.Constants;
import com.cloudera.bigdata.analysis.hbase.export.HFileExportMapper;

public class DataExportClient {

  private static Log LOG = LogFactory.getLog(DataExportClient.class);

  private Properties props;
  private Configuration conf;
  private Job job;
  private JobClient jobClient;
  private InetSocketAddress inetSocketAddress;

  public DataExportClient(Properties props, Configuration conf) {
    this.props = props;
    this.conf = conf;
    String mapredServer = props.getProperty(Constants.MAPREDSERVER);
    int mapredPort = Integer.parseInt(props.getProperty(Constants.MAPREDPORT));
    inetSocketAddress = new InetSocketAddress(mapredServer, mapredPort);
    try {
      jobClient = new JobClient(inetSocketAddress, new Configuration());
      job = new Job(conf);
    } catch (IOException e) {
      e.printStackTrace();
      LOG.info(e.getMessage());
    }
  }

  public void exportData() {
    prepareInput();

  }

  public void prepareInput() {
    String name = props.getProperty(Constants.JOBNAME);
    String tableName = props.getProperty(Constants.TABLENAME);
    String inputDir = props.getProperty(Constants.INPUTDIR);
    String family = props.getProperty(Constants.FAMILY);
    String outputDir = props.getProperty(Constants.OUTPUTDIR);

    try {
      HBaseAdmin admin = new HBaseAdmin(conf);
      List<HRegionInfo> regionList = admin.getTableRegions(Bytes
          .toBytes(tableName));
      Path tableDir = new Path(inputDir);
      FileSystem fs = FileSystem.get(conf);
      List<Path> storeFiles = new ArrayList<Path>();

      for (HRegionInfo region : regionList) {
        Path regionDir = new Path(tableDir, region.getEncodedName());
        Path cfDir = new Path(regionDir, family);
        FileStatus[] files = fs.listStatus(cfDir);
        for (FileStatus file : files) {
          if (!file.isDir()) {
            storeFiles.add(file.getPath());
          }
        }
      }

      if (storeFiles.isEmpty()) {
        LOG.info("Empty folder, no files");
      }
      int fileNum = storeFiles.size();
      String array[] = new String[fileNum];
      for (int i = 0; i < fileNum; i++) {
        array[i] = storeFiles.get(i).toString();
        LOG.info("path " + storeFiles.get(i).toString());
      }

      Path inPath = new Path(CommonUtils.getTempDir(jobClient), name);
      LOG.info("temp dir " + CommonUtils.getTempDir(jobClient));
      Path outPath = new Path(outputDir);

      LOG.info(outputDir);
      FSDataOutputStream os = fs.create(inPath, true, 1024 * 4);

      for (Path p : storeFiles) {
        Text t = new Text(p.toString() + '\n');
        t.write(os);
      }

      os.close();
      LOG.info("Finished wrting mapreduce input file");
      submitJob(fileNum, tableName, inPath, outPath);
    } catch (MasterNotRunningException e) {
      e.printStackTrace();
      LOG.info(e.getMessage());
    } catch (ZooKeeperConnectionException e) {
      e.printStackTrace();
      LOG.info(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      LOG.info(e.getMessage());
    }
  }

  public void submitJob(int fileNum, String tableName, Path inPath, Path outPath) {
    try {
      SchemaMetrics.configureGlobally(conf);
      String name = props.getProperty(Constants.JOBNAME);
      String inputSize = conf.get(Constants.INPUTSIZE);

      int mapNum = Integer.parseInt(props.getProperty(Constants.MAPREDNUM));

      if (inputSize != null) {
        conf.set("mapred.max.split.size", inputSize);
        conf.set("mapred.min.split.size.per.rack", inputSize);
        conf.set("mapred.min.split.size.per.node", inputSize);
      }

      conf.set("mapred.task.timeout", "1800000");

      int filePerMap = fileNum / mapNum + 1;
      conf.setInt("mapreduce.input.lineinputformat.linespermap", filePerMap);
      LOG.info("Client-Set linespermap: " + filePerMap);
      conf.set("mapred.map.tasks.speculative.execution", "false");
      conf.set("mapred.reduce.tasks.speculative.execution", "false");
      conf.set("fs.default.name", jobClient.getFs().getUri().toString());
      conf.set("mapred.output.compress", "true");
      conf.set("mapred.output.compression.codec",
          "org.apache.hadoop.io.compress.GzipCodec");
      conf.setInt("dfs.replication", 1);

      Job job = new Job(conf, name + "_" + tableName);
      job.setJarByClass(HFileExportMapper.class);
      job.setMapperClass(HFileExportMapper.class);
      FileInputFormat.setInputPaths(job, inPath);

      job.setInputFormatClass(NLineInputFormat.class);
      job.setNumReduceTasks(0);

      FileOutputFormat.setOutputPath(job, outPath);
      job.setOutputKeyClass(NullWritable.class);
      job.setOutputValueClass(Text.class);

      job.waitForCompletion(true);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      LOG.info("MapredCommand <properties_file>");
      return;
    }

    Properties props = new Properties();
    props.load(new FileInputStream(args[0]));
    LOG.info("Before new MapredCommand.");

    Configuration conf = HBaseConfiguration.create();
    LOG.info("Get the fs.default.name after new Configuration : "
        + conf.get("fs.default.name"));
    DataExportClient client = new DataExportClient(props, conf);
    client.exportData();
  }

}
