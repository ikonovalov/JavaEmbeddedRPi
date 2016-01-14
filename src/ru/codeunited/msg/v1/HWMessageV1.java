/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.msg.v1;

import ru.codeunited.msg.HWMessage;

/**
 *
 * @author ikonovalov
 */
public class HWMessageV1 extends HWMessage {
    
    public static final byte V1_BYTE = 0b0000_01_00;
    
    public static final byte MSG_GPIO_V1 = MSG_GPIO | V1_BYTE;
    
    public static final byte MSG_UART_V1 = MSG_UART | V1_BYTE;
    
    public static final byte MSG_SERVO_V1 = MSG_SERVO | V1_BYTE;
    
    public static final byte MSG_SERVO_V1_REQ = MSG_SERVO_V1    | MSG_REQUEST_TYPE;    
    public static final byte MSG_SERVO_V1_RESP = MSG_SERVO_V1   | MSG_RESPONSE_TYPE;
    public static final byte MSG_SERVO_V1_DAT = MSG_SERVO_V1    | MSG_DATAGRAM_TYPE;
    
    private final static int VERSION = 1;
    
    
    public HWMessageV1(byte[] message) {
        super(message);
        // check version
        if (super.getVersion() != VERSION) {
            throw new RuntimeException("This is a wrong message version. Expected V1, but have " + super.getVersion());
        }
    }

    @Override
    public int getVersion() {
        return VERSION;
    }
    
}
