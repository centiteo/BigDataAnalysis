package customer;

import java.io.IOException;

import org.apache.hadoop.ipc.ProtocolSignature;

public class ServerNode implements IServerNode {

  @Override
  public ProtocolSignature getProtocolSignature(String arg0, long arg1, int arg2)
      throws IOException {
    // TODO Auto-generated method stub
    return new ProtocolSignature(IServerNode.versionID, null);
  }

  @Override
  public long getProtocolVersion(String arg0, long arg1) throws IOException {
    // TODO Auto-generated method stub
    return IServerNode.versionID;
  }

  @Override
  public String register() {
    // TODO Auto-generated method stub

    return "ServerNode::register()";
  }

}
