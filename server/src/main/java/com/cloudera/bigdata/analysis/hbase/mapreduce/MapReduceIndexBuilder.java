package com.cloudera.bigdata.analysis.hbase.mapreduce;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapReduceIndexBuilder {
  private static final Logger LOG = LoggerFactory
      .getLogger(MapReduceIndexBuilder.class);
  public static final String JOB_NAME = "Build-Secondary-Index";
  public static final String TEMP_INDEX_PATH = "/tmp/hbase-secondary-index";

  public static final String FAM_QUA_SEP = ":";
  public static final String TBL_NAME_SEP = "_";
  public static final byte[] DUMMY_CF = Bytes.toBytes("f");
  public static final byte[] DUMMY_QUA = Bytes.toBytes("q");
  public static final byte[] DUMMY_VAL = Bytes.toBytes("");

  public static final String TABLE_NAME = "builder.tableName";
  public static final String ENABLE_WAL = "builder.enableWAL";
  public static final String INDEX_FIELDS = "builder.fields";

  private Configuration config;

  private String sourceTable;
  private boolean enableWAL;
  private String[] fields;

  public static void main(String[] args) {
    if (args.length < 3) {
      System.err
          .println("You must invoke the indexer as: "
              + "com.cloudera.bigdata.analysis.hbase.mapreduce.MapReduceIndexBuilder"
              + " <tableName>" + " <wal=[true|false]>"
              + " <family:qualifier> [<faimily:qualifier>]");
    }

    MapReduceIndexBuilder indexBuilder = new MapReduceIndexBuilder(args);

    indexBuilder.start();
  }

  public MapReduceIndexBuilder(String[] args) {
    config = HBaseConfiguration.create();

    sourceTable = args[0];

    if (args[1].contains("true")) {
      enableWAL = true;
    } else {
      enableWAL = false;
    }

    fields = new String[args.length - 2];
    for (int i = 2; i < args.length; i++) {
      fields[i - 2] = args[i];
    }

  }

  public void start() {
    try {
      config.set(TABLE_NAME, sourceTable);
      config.setBoolean(MultiTableOutputFormat.WAL_PROPERTY, enableWAL);
      config.setStrings(INDEX_FIELDS, fields);

      Job job = Job.getInstance(config, JOB_NAME);
      job.setJarByClass(MapReduceIndexBuilder.class);
      Scan scan = new Scan();
      scan.setCaching(1000);

      TableMapReduceUtil.initTableMapperJob(sourceTable, scan,
          IndexMapper.class, null, null, job, true);
      job.setNumReduceTasks(0);
      job.setOutputFormatClass(MultiTableOutputFormat.class);
      job.waitForCompletion(true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class IndexMapper extends
      TableMapper<ImmutableBytesWritable, Put> {

    private TreeMap<Pair, ImmutableBytesWritable> indexes;

    protected void map(ImmutableBytesWritable key, Result result,
        Context context)
        throws IOException, InterruptedException {
      
      for (java.util.Map.Entry<Pair, ImmutableBytesWritable> entry : indexes
          .entrySet()) {
        Pair pair = entry.getKey();
        byte[] value = result.getValue(pair.first, pair.second);
        LOG.info(new String(value));

        Put put =
            new Put(Bytes.add(value, Bytes.toBytes(255), result.getRow()));
        put.add(DUMMY_CF, DUMMY_QUA, DUMMY_VAL);
        context.write(entry.getValue(), put);
      }

    }

    protected void setup(Context context) {
      Configuration config = context.getConfiguration();

      String tableName = config.get(TABLE_NAME);
      String[] fields = config.getStrings(INDEX_FIELDS);

      indexes = new TreeMap<Pair, ImmutableBytesWritable>();
      for(String field:fields){
        String[] parts = field.split(FAM_QUA_SEP);
        indexes.put(
            new Pair(Bytes.toBytes(parts[0]), Bytes.toBytes(parts[1])),
            new ImmutableBytesWritable(Bytes.toBytes(tableName + TBL_NAME_SEP
                + parts[0] + TBL_NAME_SEP + parts[1])));
      }
    }
  }

  public static class Pair implements Comparable<Pair> {
    // column family name and qualifier name
    public byte[] first;
    public byte[] second;

    public Pair(byte[] first, byte[] second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public int compareTo(Pair o1) {
      // TODO Auto-generated method stub
      int res1 = Bytes.compareTo(this.first, o1.first);
      if (res1 == 0)
        return res1;
      else
        return Bytes.compareTo(this.second, o1.second);
    }
  }

}
