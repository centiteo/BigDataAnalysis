package customer;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

public class TestRPC {
  private static String host = "127.0.0.1";
  private static int port = 2000;
  private static int handlers = 1;
  private static Server listener = null;

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      listener =
          new RPC.Builder(getHadoopConfiguration())
              .setProtocol(IServerNode.class).setInstance(new ServerNode())
              .setBindAddress(host).setPort(port).setNumHandlers(handlers)
              .build();
      listener.start();
      System.out
          .println("listener = new RPC.Builder(getHadoopConfiguration()).setProtocol(IServerNode.class).setInstance(new ServerNode()).setBindAddress(host).setPort(port).setNumHandlers(handlers).build();  ");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static Configuration getHadoopConfiguration() {
    Configuration hadoopCfg = new Configuration();
    hadoopCfg.addResource("classpath:core-site.xml");
    hadoopCfg.addResource("classpath:hdfs-site.xml");
    hadoopCfg.addResource("classpath:mapred-site.xml");
    hadoopCfg.addResource("classpath:yarn-site.xml");
    hadoopCfg.set("fs.hdfs.impl",
        org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
    hadoopCfg.set("fs.file.impl",
        org.apache.hadoop.fs.LocalFileSystem.class.getName());
    return hadoopCfg;
  }
}
