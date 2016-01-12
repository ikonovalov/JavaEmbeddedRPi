/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.arduino;

/**
 * byte 0: 4 bit message type, 2 bit version, 2 bit Req 00 /Response 01 /Datagram 10 
 * byte 1: message size
 * byte x: message type specific
 * @author ikonovalov
 */
public abstract class HWMessage {   
    
    public static final byte MSG_REQUEST_TYPE = 0b0000_0000;
    
    public static final byte MSG_RESPONSE_TYPE = 0b0000_0001;
    
    public static final byte MSG_DATAGRAM_TYPE = 0b0000_0010;
    
    private final byte[] message;      
    
    public HWMessage(byte[] message) {
        this.message = message;
    }
    
    public abstract int getVersion();
    
}
