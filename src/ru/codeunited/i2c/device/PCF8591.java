/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.codeunited.i2c.device;

import java.io.IOException;
import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;
import ru.codeunited.gen.dev.ADC;
import ru.codeunited.gen.dev.DAC;

/**
 *
 * @author ikonovalov
 */
public class PCF8591 extends AbstractI2CDevice {

    public static final int DEFAULT_ADDRESS = 0x48; // A0=Vss A1=Vss A2=Vss

    public static final byte ANALOG_OUTPUT_ENABLED = 0b0100_0000;

    public static final byte ANALOG_OUTPUT_DISABLED = 0b0000_0000;

    public static final byte INPUT_CHANNEL_0 = 0x00;

    public static final byte INPUT_CHANNEL_1 = 0x01;

    public static final byte INPUT_CHANNEL_2 = 0x02;

    public static final byte INPUT_CHANNEL_3 = 0x03;

    public static final byte AUTO_INC_CHANNEL_ENABLE = 0b0000_0100;

    public static final byte AUTO_INC_CHANNEL_DISABLE = 0b0000_0000;

    byte controlByte = 0b0000_0000; // output disabled, auto-increment channel off, input channel 0 is current

    byte analogValue = 0x00;

    private final ADC adc;

    private final DAC dac;

    public PCF8591(int address) throws IOException {
        I2CDeviceConfig config = new I2CDeviceConfig.Builder() // new I2CDeviceConfig(BUS_ID, DEFAULT_ADDRESS, ADDRESS_SIZE, FREQ);
                .setControllerNumber(BUS_ID)
                .setAddress(address, ADDRESS_SIZE)
                .setClockFrequency(FREQ)
                .build();
        setDevice((I2CDevice) DeviceManager.open(config));

        adc = new ADC(4);
        dac = new DAC(1);
    }

    public PCF8591() throws IOException {
        this(DEFAULT_ADDRESS);
    }

    /**
     * Switch to channel 0-3 with analog output enabled (in real for warm-up
     * internal oscillator)
     *
     * @param channel
     * @throws IOException
     */
    public final void switchChannel(byte channel) throws IOException {
        System.out.println("Switch to channel #" + channel);
        write((byte) (ANALOG_OUTPUT_ENABLED | channel));
    }

    /**
     * AUTO_INC + ANALOG_OUTPUT enabled (oscillator should stay warm)
     *
     * @throws IOException
     */
    public final void useAutoRotateChannel() throws IOException {
        System.out.println("Switch to AUTO_INC channels mode with internal oscilator enabled");
        write((byte) (ANALOG_OUTPUT_ENABLED | AUTO_INC_CHANNEL_ENABLE)); // starts with 0x00 channel
    }

}
