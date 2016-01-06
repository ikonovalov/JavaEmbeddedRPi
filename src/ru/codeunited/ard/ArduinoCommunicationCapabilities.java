/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.ard;

/**
 *
 * @author ikonovalov
 */
public enum ArduinoCommunicationCapabilities {

    /**
     *
     */
    UART(UARTCommunication.class);

    private final Class<?> communitaction;

    private ArduinoCommunicationCapabilities(Class comm) {
        this.communitaction = comm;
    }

}
