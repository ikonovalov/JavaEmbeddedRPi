/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.msg;

import ru.codeunited.msg.v1.GPIOHWMessageV1;
import ru.codeunited.msg.v1.HWMessageV1;
import ru.codeunited.msg.v1.ServoHWMessageV1;
import ru.codeunited.msg.v1.UARTHWMessage;

/**
 *
 * @author ikonovalov
 */
public class HWMessageDefaultFactoryImpl implements HWMessageFactory {

    @Override
    public HWMessage createFrom(byte[] msg) {
        byte type = (byte) (msg[0] & 0xF0);
        switch (type) {
            case HWMessage.MSG_SERVO:
                return new ServoHWMessageV1(msg);
            case HWMessage.MSG_GPIO:
                return new GPIOHWMessageV1(msg);
            case HWMessage.MSG_UART:
                return new UARTHWMessage(msg);
            default:
                return new HWMessageV1(msg);
        }
    }
    
}
