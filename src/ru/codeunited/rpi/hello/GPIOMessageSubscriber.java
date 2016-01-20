/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.util.logging.Level;
import java.util.logging.Logger;
import ru.codeunited.gpio.GPIODeviceOutput;
import ru.codeunited.msg.HWMessage;
import ru.codeunited.msg.v1.GPIOHWMessageV1;

/**
 *
 * @author ikonovalov
 */
public class GPIOMessageSubscriber implements MessageSubscriber {
    
    @Override
    public void onMessage(HWMessage hWMessage) {
        System.out.println("hWMessage.getType() == " + hWMessage.getType());
        if (hWMessage.getType() == HWMessage.MSG_GPIO) {
            GPIOHWMessageV1 gpioMessage = (GPIOHWMessageV1) hWMessage;
            System.out.println(">>> GPIO message! getVersion() = " + gpioMessage.getVersion() + ", size()= " + gpioMessage.size());
            System.out.println(">>> Pin number = " + gpioMessage.getPinNumber());
            
            GPIOHWMessageV1.GPIOMode mode = gpioMessage.getMode();
            System.out.println(">>> Pin mode = " + mode);
            switch (mode) {
                case OUTPUT:
                    setPinOut(gpioMessage);
                    break;
                default:
                    System.err.println("Not supported yet. [" + mode + "]");
            }
            
        }
    }

    private void setPinOut(GPIOHWMessageV1 gpioMessage) {
        System.out.println("Set OUT! -> " + gpioMessage.getModeParams());
        try (GPIODeviceOutput pinOut = new GPIODeviceOutput(gpioMessage.getPinNumber())) {            
            byte modeArg = gpioMessage.getModeParams();
            switch(modeArg) {
                case 0:
                    pinOut.low();
                    break;
                case 1:
                    pinOut.high();
                    break;
                case 2:
                    pinOut.invert();
                    break;
                default:
                    pinOut.low();
            }
        } catch (Exception ex) {
            Logger.getLogger(GPIOMessageSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

}
