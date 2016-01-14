/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.util.ArrayList;
import java.util.List;
import ru.codeunited.msg.HWMessage;

/**
 *
 * @author ikonovalov
 */
public class MessageBusImpl implements MessageBus {        
    
    private List<MessageSubscriber> subscribers = new ArrayList<>();

    @Override
    public synchronized void publish(HWMessage hwMessage) {
        for (MessageSubscriber sub : subscribers) {
            sub.onMessage(hwMessage);
        }
    }
    
    @Override
    public synchronized void subscribe(MessageSubscriber messageSubscriber) {
        subscribers.add(messageSubscriber);        
    }
    
}
