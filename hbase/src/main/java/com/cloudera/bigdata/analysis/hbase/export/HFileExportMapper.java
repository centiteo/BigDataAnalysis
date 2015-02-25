package com.cloudera.bigdata.analysis.hbase.export;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.io.hfile.CacheConfig;
import org.apache.hadoop.hbase.io.hfile.HFile;
import org.apache.hadoop.hbase.io.hfile.HFileScanner;
import org.apache.hadoop.hbase.regionserver.metrics.SchemaMetrics;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.cloudera.bigdata.analysis.hbase.Constants;

public class HFileExportMapper extends
    Mapper<LongWritable, Text, NullWritable, Text> {
  static final Log LOG = LogFactory.getLog(HFileExportMapper.class);
  private Configuration conf;
  private CacheConfig cacheConf;

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    String dir = value.toString();
    Path cfdir = new Path(dir.substring(1));
    export(cfdir, context);
  }

  @Override
  protected void setup(Context context) throws IOException,
      InterruptedException {
    conf = context.getConfiguration();
    cacheConf = new CacheConfig(conf);
    SchemaMetrics.configureGlobally(conf);
  }

  public void export(Path hfile, Context context) {
    try {
      FileSystem fs = FileSystem.get(conf);
      HFile.Reader reader = null;
      HFileScanner scanner = null;

      reader = HFile.createReader(fs, hfile, cacheConf);
      reader.loadFileInfo();
      scanner = reader.getScanner(false, false);
      scanner.seekTo();

      // /FSDataOutputStream os = fs.create(inputPath,true,1024*4);
      while (scanner.next()) {
        // get the row key, format like "rowkey fa    ", "rowkey fb    "
        // Text key1 = new Text(Bytes.toBytes(scanner.getKey()));
        // LOG.debug("******#####  in export scanner.getKey()" +
        // key1.toString());

        byte[] value = Bytes.toBytes(scanner.getValue());
        Text value1 = new Text(value);
        // LOG.debug("******#####  in export scanner.getValue()"
        // + value1.toString());

        // judge the value to filter, only output the full line
        if (StringUtils.splitByWholeSeparatorPreserveAllTokens(
            value1.toString(), "|").length > 3) {
          context.write(NullWritable.get(), value1);
          context.getCounter(Constants.Counters.RECORDCOUNT).increment(1);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      LOG.info(e.getCause());
    } catch (InterruptedException e) {
      e.printStackTrace();
      LOG.info(e.getCause());
    }
  }
}
