/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.com;

/**
 *
 * @author ikonovalov
 */
public enum RPiCommunicationCapabilities {

    UART(UARTCommunication.class),
    I2C(RPiCommunication.class), /* not yet implemented */
    TWI(RPiCommunication.class),
    SPI(RPiCommunication.class);

    private final Class<?> communitaction;

    private RPiCommunicationCapabilities(Class comm) {
        this.communitaction = comm;
    }

}
