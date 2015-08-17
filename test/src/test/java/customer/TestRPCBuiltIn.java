package customer;

import java.io.IOException;

import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.VersionedProtocol;

public class TestRPCBuiltIn {

  public interface TestProtocol extends VersionedProtocol {
    public static final long versionID = 1L;

    void ping() throws IOException;

    void sleep(long delay) throws IOException, InterruptedException;

    String echo(String value) throws IOException;

    String[] echo(String[] value) throws IOException;
  }

  public static class TestImpl implements TestProtocol {
    int fastPingCounter = 0;

    @Override
    public long getProtocolVersion(String protocol, long clientVersion) {
      return TestProtocol.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String protocol,
        long clientVersion, int hashcode) {
      return new ProtocolSignature(TestProtocol.versionID, null);
    }

    @Override
    public void ping() {
    }

    @Override
    public void sleep(long delay) throws InterruptedException {
      Thread.sleep(delay);
    }

    @Override
    public String echo(String value) throws IOException {
      return value;
    }

    @Override
    public String[] echo(String[] values) throws IOException {
      return values;
    }

  }
}
