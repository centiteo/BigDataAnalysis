package customer;

import org.apache.hadoop.ipc.ProtocolInfo;
import org.apache.hadoop.ipc.VersionedProtocol;

//@ProtocolInfo(protocolName = "serverNode", protocolVersion = 1)
public interface IServerNode extends VersionedProtocol {
  public static final long versionID = 1;

  public String register();
}