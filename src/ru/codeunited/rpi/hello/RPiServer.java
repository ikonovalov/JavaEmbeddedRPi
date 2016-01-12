/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnection;
import ru.codeunited.arduino.HWMessage;
import ru.codeunited.arduino.HWMessageV1;
import ru.codeunited.rpi.com.PRiCommunicationFactory;
import ru.codeunited.rpi.com.RPiCommunicationCapabilities;
import ru.codeunited.rpi.com.UARTCommunication;

/**
 *
 * @author ikonovalov
 */
public class RPiServer implements Runnable {

    public static final int DEFAULT_PORT = 4550;

    private final int port;

    private volatile boolean shouldRun = true;

    private Thread serverThread;

    private ServerSocketConnection serverSocket;

    public RPiServer() {
        this(DEFAULT_PORT);
    }

    public RPiServer(int port) {
        this.port = port;
    }

    private final UARTCommunication uart = PRiCommunicationFactory.create(RPiCommunicationCapabilities.UART);

    public synchronized void up() {
        if (serverSocket == null && serverThread == null) {
            try {
                // open socket
                serverSocket = (ServerSocketConnection) Connector.open(String.format("socket://:%d", port));
                System.out.println("Server started on " + serverSocket.getLocalAddress() + ":" + serverSocket.getLocalPort());

                // start thread
                shouldRun = true;
                serverThread = new Thread(this);
                serverThread.start();
                System.out.println("Server thread started. Listerning...");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public synchronized void down() {
        shouldRun = false;
    }

    public synchronized void downForced() {
        down();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(RPiServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            serverSocket = null;
        }
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " enter into a main loop");
        while (shouldRun) {
            try (
                    StreamConnection streamConnection = serverSocket.acceptAndOpen();
                    OutputStream out = streamConnection.openOutputStream();
                    InputStream in = streamConnection.openDataInputStream()) {
                final int bufferSz = 32;
                byte[] buffer = new byte[bufferSz];
                byte[] incompleteTransmission;
                byte[] restTransmission;
                int readSize;
                while((readSize = in.read(buffer)) != -1) {              
                    System.out.println("Buffer read " + readSize);
                           
                    // message ready
                    HWMessage message = new HWMessageV1(null);
                }
                System.out.println("End of transmission");
                

            } catch (Exception ex) {
                Logger.getLogger(RPiServer.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
                downForced();
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        downForced();
    }

}
