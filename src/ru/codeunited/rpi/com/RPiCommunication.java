/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author ikonovalov
 */
public interface RPiCommunication extends AutoCloseable {
    
    InputStream newInputStream();
    
    OutputStream newOutputStream();
    
    RPiCommunication open() throws IOException;
    
}
