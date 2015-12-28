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
public class ADCChannel {
    
    private int value           = 0x0000; 
    
    private int previousValue   = 0x0000;
    
    private final int channel;

    public ADCChannel(int channel) {
        this.channel = channel;
    }       

    public int getChannel() {
        return channel;
    }       

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        setPreviousValue(this.value);
        this.value = value;
    }        

    public int getPreviousValue() {
        return previousValue;
    }

    private void setPreviousValue(int previousValue) {
        this.previousValue = previousValue;
    }       

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.channel;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ADCChannel other = (ADCChannel) obj;
        if (this.channel != other.channel) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ADCChannel{channel=" + channel + ", value=" + value + ", previousValue=" + previousValue + '}';
    }
          
}
