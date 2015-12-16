/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gen.dev;

/**
 *
 * @author ikonovalov
 */
public class ADC8Bit extends ADC {
    
    private byte[] channels = {0x00, 0x00, 0x00, 0x00};
    
    public ADC8Bit(int channelNumber) {
        super(channelNumber);
    }
    
    
    
}
