/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.ard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author ikonovalov
 */
public interface ArduinoCommunication extends AutoCloseable {
    
    InputStream newInputStream();
    
    OutputStream newOutputStream();
    
    ArduinoCommunication open() throws IOException;
    
}
