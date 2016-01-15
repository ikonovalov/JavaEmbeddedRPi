/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.msg;

import ru.codeunited.rpi.hello.Bits;

/**
 * byte 0: 4 bit message type, 2 bit version, 2 bit Req 00 /Response 01 /Datagram 10 
 * byte 1: message size
 * byte x: message type specific
 * @author ikonovalov
 */
public abstract class HWMessage {
    
    public static final byte HEADER_LEN =           2;
    
    public static final byte MSG_UART =             0b0011_00_00;
    
    public static final byte MSG_GPIO =             0b0100_00_00;
    
    public static final byte MSG_SERVO =            0b0101_00_00;
    
    public static final byte MSG_REQUEST_TYPE =     0b0000_00_00;
    
    public static final byte MSG_RESPONSE_TYPE =    0b0000_00_01;
    
    public static final byte MSG_DATAGRAM_TYPE =    0b0000_00_10;
    
    private final byte[] message;      
    
    public HWMessage(byte[] message) {
        this.message = message;
    }
    
    public byte getType() {
        return Bits.highNibble(message[0]);
    }
    
    public int getVersion() {
        return Bits.lowNibble(message[0]) >> 2;
    }
    
}
