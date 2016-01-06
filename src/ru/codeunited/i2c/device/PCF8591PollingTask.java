/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.i2c.device;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.codeunited.gen.dev.ADCChannel;

/**
 *
 * @author ikonovalov
 */
public abstract class PCF8591PollingTask extends TimerTask {
    
    private final PCF8591 pcf8591;
    
    public PCF8591PollingTask(PCF8591 pcf85911) {
        this.pcf8591 = pcf85911;
    }

    @Override
    public void run() {
        try {
            onPoll(pcf8591.readChannels());
        } catch (IOException ex) {
            Logger.getLogger(PCF8591PollingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public abstract void onPoll(List<ADCChannel> channelsValue);
    
}
