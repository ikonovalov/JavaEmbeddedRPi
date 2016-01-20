/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.msg.v1;

import ru.codeunited.rpi.hello.Bits;

/**
 *
 * @author ikonovalov
 */
public class GPIOHWMessageV1 extends HWMessageV1 {
    
    public enum GPIOMode {
        UNKNOWN((byte)0x00),
        OUTPUT( (byte)0x01),
        INPUT(  (byte)0x02);
        
        private final byte mode;

        private GPIOMode(byte mode) {
            this.mode = mode;
        }
         
        static GPIOMode forCode(byte code) {
            for(GPIOMode mode : GPIOMode.values()) {
                if (mode.mode == code) {
                    return mode;
                }
            }
            return UNKNOWN;
        }
    }
    
    public GPIOHWMessageV1(byte[] message) {
        super(message);
    }
    
    public int getPinNumber() {
        return getRawMessage()[2];
    }
    
    public GPIOMode getMode() {
        byte modeSection = Bits.highNibble(getRawMessage()[3]);
        return GPIOMode.forCode(modeSection);
    }
    
    public byte getModeParams() {
        return Bits.lowNibble(getRawMessage()[3]);
    }
    
}
