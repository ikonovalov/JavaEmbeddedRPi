/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.i2c.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.dio.i2cbus.I2CDevice;

/**
 *
 * @author ikonovalov
 */
public abstract class AbstractI2CDevice implements AutoCloseable {

    protected final int BUS_ID = 1; // PRi 2 ver1.1

    protected final int ADDRESS_SIZE = 7;

    private I2CDevice device = null;

    protected final int FREQ = 100_000; // this is a max value for PCF8591

    protected void write(byte... buffer) throws IOException {
        device.write(ByteBuffer.wrap(buffer));
    }
    
    @Override
    public void close() throws Exception {
        if (device.isOpen()) {
            device.close();
            System.out.println("Device closed");
        }
    }

    protected I2CDevice getDevice() {
        return device;
    }

    protected final void setDevice(I2CDevice device) {
        this.device = device;
    }
    
    protected ByteBuffer read(int size) throws IOException {
        byte[] buf = new byte[size];
        ByteBuffer bBuffer = ByteBuffer.wrap(buf);
        getDevice().read(bBuffer);
        return bBuffer;
    }
    
    protected int read() throws IOException {
        int newValue = getDevice().read();
        return newValue;
    }

}
