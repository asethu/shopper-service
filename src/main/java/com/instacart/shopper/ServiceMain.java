package com.instacart.shopper;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class ServiceMain {
    public static String SERVICE_HOST = "localhost";
    public static String SERVICE_PORT = "9090";
    public static String ENDPOINT_URI_PATTERN = "http://%s:%s/";

    // Base URI the Grizzly HTTP server will listen on
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String endpointURI) {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.instacart.shopper package
        final ResourceConfig rc = new ResourceConfig().packages("com.instacart.shopper");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at the requested endpointURI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(endpointURI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String endpointURI = String.format(ENDPOINT_URI_PATTERN, SERVICE_HOST, SERVICE_PORT);
        HttpServer server = null;

        try {
            server = startServer(endpointURI);
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", endpointURI));
            System.in.read();
        } finally {
            if (server != null) {
                server.shutdown();
            }
        }
    }
}

