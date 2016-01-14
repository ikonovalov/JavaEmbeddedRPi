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
        final int bufferSz = 32;
        byte[] buffer = new byte[bufferSz];
        byte[] restTransmission;
        System.out.println("Handling incoming connection... ");
        System.out.println("MF ready : " + (getMessageFactory() != null));
        System.out.println("MB ready : " + (getMessageBus() != null));

        try (OutputStream out = connection.openOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                InputStream in = connection.openDataInputStream()) {

            int readSize;
            while ((readSize = in.read(buffer)) != -1) { // keep reading stream while connected     
                System.out.println("Buffer read " + readSize);

                int msgOffset = 0;
                boolean parseMessage = true;
                while (parseMessage) {
                    byte msgSize = buffer[msgOffset + 1];
                    byte[] msg = new byte[msgSize];
                    System.arraycopy(buffer, msgOffset, msg, 0, msgSize);
                    

                    System.out.println("MsgSize: " + msgSize);
                    
                    HWMessage hwMessage = getMessageFactory().createFrom(msg);
                    writer.write("cls " + hwMessage.getClass().getName() + '\n');
                    getMessageBus().publish(hwMessage);
                    
                    msgOffset += msgSize; // move to start of a next message
                    System.out.println("New MsgOffset: " + msgOffset);
                    if (buffer[msgOffset] == 0x00) { // buffer exhausted?
                        parseMessage = false;
                        System.out.println("Buffer exhausted");
                    }
                }
            }
            writer.write("End of transmission\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
