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
public final class CommunicationFactory {

    public static <T extends ArduinoCommunication> T create(ArduinoCommunicationCapabilities capabilities) {
        switch (capabilities) {
            case UART:
                return (T) new UARTCommunication();
            default:
                throw new RuntimeException("Communication not supported");

        }
    }

    private CommunicationFactory() {
        
    }

}
