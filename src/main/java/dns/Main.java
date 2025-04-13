package dns;

import dns.config.LoggingConfig;
import dns.server.Server;
import dns.server.impl.ServerImpl;

public class Main {
  public static void main(String[] args) {
    LoggingConfig.initialize();

    Server dnsServer = new ServerImpl();
    dnsServer.start();
  }
}
