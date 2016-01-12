/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

/**
 *
 * @author ikonovalov
 */
public final class Bits {
    
    private Bits() {
        
    }
    
    public static boolean isBitSet(int value, int position) {
        return (value & (1L << position)) != 0;
    }
    
    public static boolean isBitNotSet(int value, int position) {
        return !isBitSet(value, position);
    }

    /**
     * Get high nibble of byte
     * @param value
     * @return 
     */
    public static byte highNibble(byte value) {
        return (byte) ((value & 0xF0) >> 4);
    }

    /**
     * Get low nibble of byte
     * @param value
     * @return 
     */
    public static byte lowNibble(byte value) {
        return (byte) (value & 0x0F);
    }
    
}
