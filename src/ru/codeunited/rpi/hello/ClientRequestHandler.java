/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.StreamConnection;
import ru.codeunited.msg.HWMessage;
import ru.codeunited.msg.HWMessageFactory;

/**
 *
 * @author ikonovalov
 */
public class ClientRequestHandler {

    private final StreamConnection connection;

    private MessageBus messageBus;

    private HWMessageFactory messageFactory;

    public ClientRequestHandler(StreamConnection connection) {
        super();
        Objects.requireNonNull(connection, "Connection is null");
        this.connection = connection;
    }

    public void setMessageBus(MessageBus messageBus) {
        Objects.requireNonNull(messageBus, "Message queue is null");
        this.messageBus = messageBus;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public HWMessageFactory getMessageFactory() {
        return messageFactory;
    }

    public void setMessageFactory(HWMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    void handle() {
        final int bufferSz = 8;
        final int lastBufferIndex = bufferSz - 1;
        byte[] buffer = new byte[bufferSz];
        byte[] incompleteMessage = null;
        System.out.println("Handling incoming connection... ");
        System.out.println("MF ready : " + (getMessageFactory() != null));
        System.out.println("MB ready : " + (getMessageBus() != null));

        try (OutputStream out = connection.openOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                InputStream in = connection.openDataInputStream()) {

            int readSize;
            while ((readSize = in.read(buffer)) != -1) { // keep reading stream while connected     
                System.out.println("Buffer read " + readSize);
                byte[] workMemory;
                int workMemorySz;
                // copy buffer into work memory of incomplete + buffer
                if (incompleteMessage != null) {
                    System.out.println("Incomplete message detected");
                    workMemorySz = incompleteMessage.length + bufferSz;
                    workMemory = new byte[workMemorySz];
                    System.arraycopy(incompleteMessage, 0, workMemory, 0, incompleteMessage.length);
                    System.arraycopy(buffer, 0, workMemory, incompleteMessage.length, readSize);
                    incompleteMessage = null; // cleanup incomplete
                } else {
                    workMemory = new byte[bufferSz];
                    workMemorySz = bufferSz;
                    System.arraycopy(buffer, 0, workMemory, 0, readSize);
                }
                System.out.println("WorkMem: " + workMemorySz);
                
                int msgOffset = 0;
                boolean parseMessage = true;
                
                while (parseMessage) {
                    byte msgSize = workMemory[msgOffset + 1];
                    
                    if (msgSize + msgOffset <= workMemorySz) { // msg fully contains in a buffer
                        byte[] msg = new byte[msgSize];
                        System.arraycopy(workMemory, msgOffset, msg, 0, msgSize);

                        System.out.println("MsgSize: " + msgSize);

                        HWMessage hwMessage = getMessageFactory().createFrom(msg);
                        writer.write("cls " + hwMessage.getClass().getName() + '\n');
                        getMessageBus().publish(hwMessage);
                        
                        msgOffset += msgSize;
                        System.out.println("New MsgOffset: " + msgOffset);
                    } else { // only part of message contains in a buffer
                        // oversized message
                        System.out.println("Oversized message!");
                        int incompletePartBuffer = workMemorySz - msgOffset;
                        incompleteMessage = new byte[incompletePartBuffer];
                        System.out.println("Oversized message rest: " + incompletePartBuffer);
                        System.arraycopy(workMemory, msgOffset, incompleteMessage, 0, incompletePartBuffer);
                        parseMessage = false;
                    }
                                      
                    // detect stop factors
                    if (msgOffset >= workMemorySz || workMemory[msgOffset] == 0x00) { //0x00 - we don't have 0x00 message type - so buffer not fulled.
                        parseMessage = false;
                        System.out.println("No more messages in a buffer");
                    }
                }
            }
            writer.write("End of transmission\n");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            System.err.println("Terminate connection!");
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
