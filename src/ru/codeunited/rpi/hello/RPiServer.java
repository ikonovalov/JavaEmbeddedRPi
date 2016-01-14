/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnection;
import ru.codeunited.msg.HWMessageFactory;
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
    
    private MessageBus messageBus = new MessageBusImpl();       
        
    private HWMessageFactory messageFactory;
    
    private final UARTCommunication uart = PRiCommunicationFactory.create(RPiCommunicationCapabilities.UART);

    public RPiServer() {
        this(DEFAULT_PORT);
    }

    public RPiServer(int port) {
        this.port = port;
    }

    public void setMessageBus(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public void setMessageFactory(HWMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public HWMessageFactory getMessageFactory() {
        return messageFactory;
    }       
   
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

        try {
            while (shouldRun) {
                StreamConnection streamConnection = serverSocket.acceptAndOpen();
                ClientRequestHandler clientRequestHandler = new ClientRequestHandler(streamConnection);
                clientRequestHandler.setMessageBus(getMessageBus());
                clientRequestHandler.setMessageFactory(getMessageFactory());
                clientRequestHandler.handle();
            }
        } catch (IOException ex) {
            Logger.getLogger(RPiServer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());

            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            downForced();
        }
    }

}
