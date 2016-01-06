/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.ard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import jdk.dio.DeviceManager;
import jdk.dio.uart.UART;
import jdk.dio.uart.UARTConfig;
import static jdk.dio.uart.UARTConfig.DATABITS_8;
import static jdk.dio.uart.UARTConfig.FLOWCONTROL_NONE;
import static jdk.dio.uart.UARTConfig.PARITY_NONE;
import static jdk.dio.uart.UARTConfig.STOPBITS_1;

/**
 *
 * @author ikonovalov
 */
public class UARTCommunication implements ArduinoCommunication {

    private UARTConfig config;

    private UART arduinoSerial;

    private static final int BUFF_INPUT_SZ = 512;

    private static final int BUFF_OUTPUT_SZ = 512;

    private int baudRate = 9600; // very slow by default

    /**
     * Connect to /devACM0 with 9600 speed.
     */
    public UARTCommunication() {
        this("ttyACM0", 9600);
    }

    public UARTCommunication(String deviceName, int baudRate) {
        config = new UARTConfig.Builder()
                .setControllerName(deviceName)
                .setBaudRate(baudRate)
                .setDataBits(DATABITS_8)
                .setStopBits(STOPBITS_1)
                .setParity(PARITY_NONE)
                .setFlowControlMode(FLOWCONTROL_NONE)
                .setInputBufferSize(BUFF_INPUT_SZ)
                .setOutputBufferSize(BUFF_OUTPUT_SZ)
                .build();
    }

    @Override
    public UARTCommunication open() throws IOException {
        arduinoSerial = DeviceManager.open(config);
        return this;
    }

    @Override
    public InputStream newInputStream() {
        InputStream is = Channels.newInputStream(arduinoSerial);
        return is;
    }
    
    @Override
    public OutputStream newOutputStream() {
        OutputStream os = Channels.newOutputStream(arduinoSerial);
        return os;
    }

    @Override
    public void close() throws Exception {
        if (arduinoSerial != null) {
            arduinoSerial.close();
        }
    }

}
