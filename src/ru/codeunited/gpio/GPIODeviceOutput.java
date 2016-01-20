/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gpio;

import java.io.IOException;
import jdk.dio.gpio.GPIOPinConfig;

/**
 *
 * @author ikonovalov
 */
public class GPIODeviceOutput extends GPIODevice {
    
    public GPIODeviceOutput(int pinNumber) throws IOException {
        super(pinNumber, GPIOPinConfig.DIR_OUTPUT_ONLY);
    }
    
    public void high() throws IOException {
        getPin().setValue(true);
    }
    
    public void low() throws IOException {
        getPin().setValue(false);
    }    
    
    public void invert() throws IOException {
        if (getPin().getValue()) {
            low();
        } else {
            high();
        }
    }
}
