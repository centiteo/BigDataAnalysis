import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;


public class TestKrb5Login {
  public static void main(String[] args){
    try {
      UserGroupInformation.loginUserFromKeytab(args[0],
          "/root/demo/usera.keytab");
      UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
      System.out.println("++++++" + ugi.getUserName() + "++++++");

      Configuration configuration = HBaseConfiguration.create();
      HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);

      HTableDescriptor[] htables = hBaseAdmin.listTables();
      for (HTableDescriptor descriptor : htables) {
        System.out.println(descriptor.getTableName().getNameAsString());
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
