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
public final class PRiCommunicationFactory {

    public static <T extends RPiCommunication> T create(RPiCommunicationCapabilities capabilities) {
        switch (capabilities) {
            case UART:
                return (T) new UARTCommunication();
            default:
                throw new RuntimeException("Communication not supported");

        }
    }

    private PRiCommunicationFactory() {
        
    }

}
