package dns.server;

/**
 * Represents a domain name server with basic lifecycle operations.
 */
public interface Server {
    /**
     * Starts the server, initializing all necessary resources and making it ready
     * to handle requests.
     */
    void start();

    /**
     * Stops the server, releasing all resources and halting its operations.
     */
    void stop();

    /**
     * Restarts the server by stopping and then starting it again, reinitializing
     * its state.
     */
    void restart();
}
