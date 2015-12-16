/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gpio;

import java.io.IOException;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;
import jdk.dio.gpio.PinListener;

/**
 *
 * @author ikonovalov
 */
public class DFR0076Device implements AutoCloseable {

    private GPIOPin pin = null;

    public DFR0076Device(int pinGPIO) throws IOException {
        pin = (GPIOPin) DeviceManager.open(
                new GPIOPinConfig.Builder()
                .setControllerNumber(0)
                .setPinNumber(pinGPIO)
                .setDirection(GPIOPinConfig.DIR_INPUT_ONLY)
                .setDriveMode(GPIOPinConfig.MODE_INPUT_PULL_DOWN)
                .setTrigger(GPIOPinConfig.TRIGGER_FALLING_EDGE)
                .setInitValue(false)
                .build()
        );
        System.out.println("Pin name: " + pin.getDescriptor().getName());
        System.out.println(DFR0076Device.class.getName() + " initialized");
    }

    public void setListener(PinListener listener) throws IOException {
        if (pin != null) {
            pin.setInputListener(listener);
        }
    }

    @Override
    public void close() throws IOException {
        if (pin != null) {
            pin.setInputListener(null);
            pin.close();
        }
    }

}
