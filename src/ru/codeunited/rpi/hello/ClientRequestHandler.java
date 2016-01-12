/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import javax.microedition.io.StreamConnection;

/**
 *
 * @author ikonovalov
 */
public class ClientRequestHandler {
    
    private final StreamConnection connection;
    
    public ClientRequestHandler(StreamConnection connection) {
        this.connection = connection;
    }
}
