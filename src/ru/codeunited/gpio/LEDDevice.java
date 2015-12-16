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

/**
 *
 * @author ikonovalov
 */
public class LEDDevice implements AutoCloseable {

    private GPIOPin pin = null;

    public LEDDevice(int pinGPIO) throws IOException {
        pin = (GPIOPin) DeviceManager.open(
                new GPIOPinConfig.Builder()
                .setControllerNumber(0)
                .setPinNumber(pinGPIO)
                .setDirection(GPIOPinConfig.DIR_OUTPUT_ONLY)
                .setDriveMode(GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN)
                .setInitValue(false)
                .build()
        );

        //pin = DeviceManager.open(pinGPIO);
        System.out.println("PIN name: " + pin.getDescriptor().getName());
        
        System.out.println(LEDDevice.class.getName() + " initialized");
    }

    public boolean switchOn() throws IOException {
        pin.setValue(true);
        return pin.getValue();
    }

    public boolean switchOff() throws IOException {
        pin.setValue(false);
        return pin.getValue();
    }

    public boolean switchLed() throws IOException {
        if (pin.getValue()) {
            return switchOff();
        } else {
            return switchOn();
        }
    }

    @Override
    public void close() throws Exception {
        if (pin != null) 
            pin.close();
    }

}
