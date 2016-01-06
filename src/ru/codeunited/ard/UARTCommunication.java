/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.ard;

import jdk.dio.uart.UART;

/**
 *
 * @author ikonovalov
 */
public class UARTCommunication implements ArduinoCommunication{
    
    private UART arduinoSerial = null;
    
    UARTCommunication() {
        
    }
    
}
