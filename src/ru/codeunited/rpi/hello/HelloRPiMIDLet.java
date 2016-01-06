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
import java.io.Reader;
import ru.codeunited.i2c.device.PCF8591;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;
import jdk.dio.DeviceManager;
import jdk.dio.uart.UART;
import jdk.dio.uart.UARTConfig;
import static jdk.dio.uart.UARTConfig.DATABITS_8;
import static jdk.dio.uart.UARTConfig.DATABITS_9;
import static jdk.dio.uart.UARTConfig.FLOWCONTROL_NONE;
import static jdk.dio.uart.UARTConfig.PARITY_EVEN;
import static jdk.dio.uart.UARTConfig.PARITY_NONE;
import static jdk.dio.uart.UARTConfig.STOPBITS_1;
import static jdk.dio.uart.UARTConfig.STOPBITS_2;
import ru.codeunited.gen.dev.ADCChannel;

/**
 *
 * @author ikonovalov
 */
public class HelloRPiMIDLet extends MIDlet {

    @Override
    public void startApp() {
        try {
            System.out.println("Hello RPi " + new Date());

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
        
       

        UART arduinoSerial = null;
        try {
            UARTConfig config = new UARTConfig.Builder()
                    //.setChannelNumber(1)
                    //.setControllerNumber(0)
                    .setControllerName("ttyACM0")
                    .setBaudRate(9600)
                    .setDataBits(DATABITS_8)
                    .setStopBits(STOPBITS_1)
                    .setParity(PARITY_NONE)
                    .setFlowControlMode(FLOWCONTROL_NONE)
                    .setInputBufferSize(1024)
                    .setOutputBufferSize(1024)
                    .build();
            arduinoSerial = DeviceManager.open(config);
            InputStream is = Channels.newInputStream(arduinoSerial);
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bReader = new BufferedReader(isReader);
            
            System.out.println("Arduino channel ready...");
            char[] buffer = new char[64];
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
        } finally {
            if (arduinoSerial != null) {
                try {
                    arduinoSerial.close();
                    System.out.println("Arduino channel closed");
                            
                } catch (IOException ex) {
                    Logger.getLogger(HelloRPiMIDLet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

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
