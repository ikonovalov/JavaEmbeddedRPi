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

    public synchronized void up() {
        if (serverSocket == null && serverThread == null) {
            try {
                serverSocket = (ServerSocketConnection) Connector.open(String.format("socket://:%d", port));
                System.out.println("Server started on " + serverSocket.getLocalAddress() + ":" + serverSocket.getLocalPort());
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
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                try (
                        StreamConnection streamConnection = serverSocket.acceptAndOpen(); 
                        OutputStream dos = streamConnection.openOutputStream();
                        InputStream is = streamConnection.openDataInputStream()) {
                    
                    dos.write("Hello, this is RPi2\n".getBytes());
                    dos.write("cmd>".getBytes());
                    dos.flush();
                    byte[] buffer = new byte[16];
                    int cmdSz = is.read(buffer);
                    System.out.println(new String(buffer, 0, cmdSz));
                    for (int z = 0; z < cmdSz; z++) {
                        dos.write(buffer[z]);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(RPiServer.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
                downForced();
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        downForced();
    }

}
