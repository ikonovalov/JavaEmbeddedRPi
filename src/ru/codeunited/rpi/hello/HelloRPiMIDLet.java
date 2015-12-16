/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import ru.codeunited.gpio.LEDDevice;
import ru.codeunited.gpio.LEDBlinker;
import ru.codeunited.gpio.DFR0076Device;
import ru.codeunited.i2c.device.PCF8591;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import jdk.dio.Device;
import jdk.dio.DeviceDescriptor;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.PinEvent;
import jdk.dio.gpio.PinListener;

/**
 *
 * @author ikonovalov
 */
public class HelloRPiMIDLet extends MIDlet {

    private static final int LED1_PIN_BCM = 26;

    private static final int LED2_DEV_ID = 40;

    private static final char MICRO = '\u03bc';

    // devices
    private DFR0076Device flame = null;

    private LEDDevice led1 = null;

    // infrasturcture
    private Thread blinkerThread = null;

    private LEDBlinker blinker = null;

    @Override
    public void startApp() {
        System.out.println("Hello RPi " + new Date());

        /*try (PCF8591 pcf8591 = new PCF8591()) {

            for (int x = 0; x < 1; x++) {
                pcf8591.switchChannel(PCF8591.INPUT_CHANNEL_2);

                long s = System.nanoTime();
                ByteBuffer bbuffer = pcf8591.read(2);
                long f = System.nanoTime();
                System.out.println("Tconv=" + (f - s) / 1000.0d + MICRO + "s");
                byte[] buffer = bbuffer.array();
                for (int z = 0; z < buffer.length; z++) {
                    System.out.println("-> z(" + z + ")=" + buffer[z]);
                }
                Thread.sleep(2000L);
                System.out.println("");
            }

        } catch (Exception ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Single channel read has ended."); */
        
        try (final PCF8591 pcf8591 = new PCF8591()) {
            pcf8591.useAutoRotateChannel();
            
            System.out.println("Prefetch:");
            ByteBuffer prefetched = pcf8591.read(1);
            print(prefetched);
            System.out.println("");
           
            System.out.println("Main fetch:");
            for (int x = 0; x < 5; x++) {
                long s = System.nanoTime();
                ByteBuffer bbuffer = pcf8591.read(4);
                long f = System.nanoTime();
                System.out.println("Tconv=" + (f - s) / 1000.0d + MICRO + "s");
                print(bbuffer);
                System.out.println("");
                //Thread.sleep(1000L); // try with and without!!!
            }
        } catch (Exception ex) {
            Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Auto rotation channels read has ended.");

    }
    

    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Good bay RPi");
        if (flame != null) {
            try {
                flame.close();
            } catch (IOException ex) {
                Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void print(ByteBuffer buffer) {
        byte[] bytes = buffer.array();
        for (int z = 0; z < bytes.length; z++) {
            System.out.println("-> z(" + z + ")=" + bytes[z]);
        }
    }
    
}
