package dns.server.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

import dns.utils.Constants;
import dns.config.LoggingConfig;
import dns.protocol.Message;
import dns.server.Server;

public class ServerImpl implements Server {
    private final DatagramSocket socket;
    private final Logger logger;

    public ServerImpl() {
        this(Constants.DEFAULT_SERVER_PORT);
    }

    public ServerImpl(int port) {
        logger = LoggingConfig.getLogger(ServerImpl.class);

        try {
            this.socket = new DatagramSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create socket on port " + port, e);
        }
    }

    @Override
    public void start() {
        logger.info("Starting server on port " + socket.getLocalPort());

        try {
            while (true) {
                final byte[] requestBuffer = new byte[Constants.MAX_PACKET_SIZE];
                final DatagramPacket packet = new DatagramPacket(requestBuffer, requestBuffer.length);
                socket.receive(packet);

                logger.info("Received data from " + packet.getSocketAddress());

                byte[] responseBuffer = handleRequest(requestBuffer);
                final DatagramPacket packetResponse = new DatagramPacket(
                        responseBuffer,
                        responseBuffer.length,
                        packet.getSocketAddress());

                socket.send(packetResponse);

                logger.info("Sent response to " + packet.getSocketAddress());
            }
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
        }
    }

    /**
     * Processes a DNS request and generates a corresponding response.
     *
     * @param requestBuffer The raw DNS request as a byte array
     * @return The serialized DNS response as a byte array
     */
    private byte[] handleRequest(byte[] requestBuffer) {
        logger.info("Handling request...");
        Message request = Message.deserialize(requestBuffer);
        Message response = Message.toDnsResponse(request);

        return response.serialize();
    }

    @Override
    public void stop() {
        if (socket != null && !socket.isClosed()) {
            logger.info("Shutting down server...");
            socket.close();
        }
    }

    @Override
    public void restart() {
    }
}
