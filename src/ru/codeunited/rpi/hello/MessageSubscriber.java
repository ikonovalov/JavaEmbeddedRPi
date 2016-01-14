/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import ru.codeunited.msg.HWMessage;

/**
 *
 * @author ikonovalov
 */
public interface MessageSubscriber {
    
    public void onMessage(HWMessage hWMessage);
    
}
