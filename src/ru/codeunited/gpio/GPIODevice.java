/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gpio;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.DeviceManager;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

/**
 *
 * @author ikonovalov
 */
public class GPIODevice implements AutoCloseable {
    
    private GPIOPin pin = null;
    
    public GPIODevice(int pinNumber, int mode) throws IOException {
        this(
                new GPIOPinConfig.Builder()
                .setControllerNumber(0)
                .setPinNumber(pinNumber)
                .setDirection(mode)
                .setDriveMode(GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN)
                .setInitValue(false)
                .build()
        );
    }
    
    public GPIODevice(GPIOPinConfig config) throws IOException {
        pin = (GPIOPin) DeviceManager.open(config);
    }
    
    protected GPIOPin getPin() {
        return pin;
    }
    
    public boolean currentState() throws IOException {
        return getPin().getValue();
    }
    
    @Override
    public void close() throws Exception {
        if (pin != null) {
            pin.close();
        }
    }

    public int getDirection() throws IOException, UnavailableDeviceException, ClosedDeviceException {
        return pin.getDirection();
    }

    public boolean isOpen() {
        return pin.isOpen();
    }
}
