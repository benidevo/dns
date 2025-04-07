package dns;

import dns.server.Server;
import dns.server.impl.ServerImpl;

public class Main {
  public static void main(String[] args) {
    Server dnsServer = new ServerImpl();
    dnsServer.start();
  }
}
