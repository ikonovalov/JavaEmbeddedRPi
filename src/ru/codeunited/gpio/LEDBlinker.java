/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.gpio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ikonovalov
 */
public class LEDBlinker implements Runnable {

    private final LEDDevice led;

    private boolean shouldStop = false;

    private final long delay;

    private int maxBlinkCount = -1;

    public LEDBlinker(LEDDevice led, long delay) {
        this.led = led;
        this.delay = delay;
    }

    public LEDBlinker(LEDDevice led) {
        this(led, 1000L);
    }

    public int getMaxBlinkCount() {
        return maxBlinkCount;
    }

    public void setMaxBlinkCount(int maxBlinkCount) {
        this.maxBlinkCount = maxBlinkCount;
    }

    public void stop() {
        shouldStop = true;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !shouldStop && getMaxBlinkCount() != 0) {
            try {
                boolean state = led.switchLed();
                if (getMaxBlinkCount() != -1) {
                    setMaxBlinkCount(getMaxBlinkCount() - 1);
                }
                System.out.println("Led state [" + (state ? "ON " : "OFF") + "] -> " + getMaxBlinkCount());
                Thread.sleep(delay);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(LEDBlinker.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        try {
            led.close();
            System.out.println("Blinker ended");
        } catch (Exception e) {

        }
    }

}
