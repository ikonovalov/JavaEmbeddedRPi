/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.rpi.hello;

import java.io.IOException;
import jdk.dio.DeviceManager;
import jdk.dio.pwm.PWMChannel;
import jdk.dio.pwm.PWMChannelConfig;

/**
 *
 * @author ikonovalov
 */
public class PWMControl {

    private PWMChannel channel;

    private static int pin = 12;

    public PWMControl() throws IOException {

        channel = DeviceManager.open(
                new PWMChannelConfig.Builder()
                .setControllerNumber(0)
                .setChannelNumber(24)
                .build()
        );
        System.out.println("PWM activated");

    }

}
