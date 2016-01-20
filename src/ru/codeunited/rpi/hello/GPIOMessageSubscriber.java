/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

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
        }
    }

}
