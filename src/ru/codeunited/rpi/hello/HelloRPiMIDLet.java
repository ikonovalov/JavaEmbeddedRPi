/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import ru.codeunited.i2c.device.PCF8591;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import ru.codeunited.gen.dev.ADCChannel;

/**
 *
 * @author ikonovalov
 */
public class HelloRPiMIDLet extends MIDlet {


    @Override
    public void startApp() {
        System.out.println("Hello RPi " + new Date());

        try (final PCF8591 pcf8591 = new PCF8591()) {
            pcf8591.writeAnalogChannel((byte) 0xda);
            
            for (int z = 0; z < 10; z++) {                
                List<ADCChannel> channels4 = pcf8591.readChannels();
                System.out.println(channels4);
                Thread.sleep(2000L);
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
