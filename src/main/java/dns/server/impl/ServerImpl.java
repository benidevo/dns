package dns.server.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import dns.utils.Constants;
import dns.protocol.Message;
import dns.server.Server;

public class ServerImpl implements Server {
    private final DatagramSocket socket;

    public ServerImpl() {
        this(Constants.DEFAULT_SERVER_PORT);
    }

    public ServerImpl(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create socket on port " + port, e);
        }
    }

    @Override
    public void start() {
        try {
            while (true) {
                final byte[] buf = new byte[Constants.MAX_PACKET_SIZE];
                final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println("Received data");

                Message response = Message.createResponseMessage();
                final byte[] bufResponse = response.serialize();
                final DatagramPacket packetResponse = new DatagramPacket(bufResponse, bufResponse.length,
                        packet.getSocketAddress());
                socket.send(packetResponse);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    public void restart() {
    }

}
