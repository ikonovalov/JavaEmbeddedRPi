/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import ru.codeunited.i2c.device.PCF8591;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author ikonovalov
 */
public class HelloRPiMIDLet extends MIDlet {

    private static final int LED1_PIN_BCM = 26;

    private static final int LED2_DEV_ID = 40;

    private static final char MICRO = '\u03bc';

    @Override
    public void startApp() {
        System.out.println("Hello RPi " + new Date());

        try (final PCF8591 pcf8591 = new PCF8591()) {
            
            for (int z = 0; z < 10; z++) {
                ByteBuffer channels4 = pcf8591.readChannels();
                print(channels4);
                pcf8591.setAnalogChannelValue((byte) (0xba + z * 0x03));
                System.out.println(pcf8591.toString());
                Thread.sleep(2000L);
            }
            
            pcf8591.switchChannel(PCF8591.INPUT_CHANNEL_1);
            for (int z = 0; z < 120; z++) {
                System.out.println("Cycle #" + z);                
                ByteBuffer channelOne = pcf8591.readChannel();
                pcf8591.setAnalogChannelValue(channelOne.get(1));
                Thread.sleep(1000L);
            }

        } catch (Exception ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Auto rotation channels read has ended.");

    }

    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Good bay RPi");

    }

    private void print(ByteBuffer buffer) {
        byte[] bytes = buffer.array();
        for (int z = 0; z < bytes.length; z++) {
            System.out.println("-> z(" + z + ")=" + bytes[z]);
        }
    }

}
