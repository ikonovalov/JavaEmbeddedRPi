/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ru.codeunited.i2c.device.PCF8591;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import ru.codeunited.rpi.com.RPiCommunicationCapabilities;
import ru.codeunited.rpi.com.PRiCommunicationFactory;
import ru.codeunited.gen.dev.ADCChannel;
import ru.codeunited.msg.HWMessageDefaultFactoryImpl;
import ru.codeunited.rpi.com.RPiCommunication;

/**
 *
 * @author ikonovalov
 */
public class HelloRPiMIDLet extends MIDlet {

    private final RPiServer server = new RPiServer();

    @Override
    public void startApp() {        
        server.setMessageBus(new MessageBusImpl());
        server.setMessageFactory(new HWMessageDefaultFactoryImpl());
        server.up();

        try {
            System.out.println("Hello RPi " + new Date());
            System.out.println("I2C dev 0x48 reading...");

            try (final PCF8591 pcf8591 = new PCF8591()) {
                pcf8591.writeAnalogChannel((byte) 0xda);

                for (int z = 0; z < 2; z++) {
                    List<ADCChannel> channels4 = pcf8591.readChannels();
                    System.out.println(channels4);
                    Thread.sleep(1000L);
                }

            } catch (Exception ex) {
                Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Auto rotation channels read has ended.");

        } catch (Exception ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("UART communication with Arduino MEGA 2560 R3");
        try (final RPiCommunication arduinoUART = PRiCommunicationFactory.create(RPiCommunicationCapabilities.UART)) {
            System.out.println("Arduino channel ready...");
            arduinoUART.open();
            InputStream is = arduinoUART.newInputStream();
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bReader = new BufferedReader(isReader);

            for (int z = 0; z < 5; z++) {
                System.out.println(bReader.readLine());
            }
            bReader.close();
            isReader.close();
            is.close();
            System.out.println("Finalize transmission.");
        } catch (IOException ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Good bay RPi");
        server.downForced();
    }
}
