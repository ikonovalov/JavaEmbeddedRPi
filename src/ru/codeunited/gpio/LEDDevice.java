/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gpio;

import java.io.IOException;

/**
 *
 * @author ikonovalov
 */
public class LEDDevice extends GPIODeviceOutput {

    public LEDDevice(int pinGPIO) throws IOException {
        super(pinGPIO);        
    }

    public void switchOn() throws IOException {
        high();
    }

    public void switchOff() throws IOException {
        low();
    }

    public boolean switchLed() throws IOException {
        invert();
        return currentState();
    }
}
