package ru.codeunited.i2c.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;
import static ru.codeunited.i2c.device.Symbols.MICRO;

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
    
    public static final byte INPUT_CHANNEL_AUTO_INC = PCF8591.AUTO_INC_CHANNEL_ENABLE;

    public static final byte AUTO_INC_CHANNEL_ENABLE = 0b0000_0100;

    public static final byte AUTO_INC_CHANNEL_DISABLE = 0b0000_0000;

    private byte controlByte = 0b0000_0000; // initial state: output disabled, auto-increment channel off, input channel 0 is current

    private byte analogValue = 0x00;

    private byte currentChannel = 0;

    private boolean autoIncrement = false;

    public PCF8591(int address) throws IOException {
        I2CDeviceConfig config = new I2CDeviceConfig.Builder() // new I2CDeviceConfig(BUS_ID, DEFAULT_ADDRESS, ADDRESS_SIZE, FREQ);
                .setControllerNumber(BUS_ID)
                .setAddress(address, ADDRESS_SIZE)
                .setClockFrequency(FREQ)
                .build();
        setDevice((I2CDevice) DeviceManager.open(config));       
    }

    public PCF8591() throws IOException {
        this(DEFAULT_ADDRESS);
    }
    
    protected byte getControlByte() {
        return controlByte;
    }

    protected void setControlByte(byte controlByte) {
        this.controlByte = controlByte;
    }          

    /**
     * AUTO_INC + ANALOG_OUTPUT enabled (oscillator should stay warm)
     *
     * @throws IOException
     */
    private void useAutoRotateChannel() throws IOException {
        write((byte) (ANALOG_OUTPUT_ENABLED | AUTO_INC_CHANNEL_ENABLE | INPUT_CHANNEL_0)); // starts with 0x00 channel
        this.currentChannel = -1;
        this.autoIncrement = true;
    }

/**
     * Switch to channel 0-3 with analog output enabled (in real for warm-up
     * internal oscillator)
     *
     * @param channel
     * @throws IOException
     */
    public final void switchChannel(byte channel) throws IOException {
        if (channel < INPUT_CHANNEL_0 || channel > INPUT_CHANNEL_3) {
            throw new IOException("Channel number out of range 0x00 - 0x03");
        }
        
        if (channel == this.currentChannel) {
            return; // we already on channel
        }

        write((byte) (ANALOG_OUTPUT_ENABLED | channel));
        this.currentChannel = channel;
        this.autoIncrement = false;
    }         
    
    /**
     * Read previous[0] and current[1] channel value. You should set channel first with "setChannel(x)" method.
     * @return ByteBuffer with a previous and current value.
     * @see setChannel
     * @throws IOException 
     */
    public final ByteBuffer readChannel() throws IOException {
        return read(2); // 2 bytes [prev and current]
    }
    
    public ByteBuffer readChannel(byte channelNumber) throws IOException {
        switchChannel(channelNumber);
        return readChannel();
    }

    /**
     * First of all it set autorotation channel mode and reads channels skipping first previous value.
     * @return ByteBuffer with a four bytes with channels values.
     * @throws IOException 
     */
    public ByteBuffer readChannels() throws IOException {
        useAutoRotateChannel();        
        ByteBuffer prefetched = read(1);
        long s = System.nanoTime();
        ByteBuffer bbuffer = read(4);
        long f = System.nanoTime();
        System.out.println("Tconv=" + (f - s) / 1000.0f + MICRO + "s");
        return bbuffer;
    }

    /**
     * If you want to override it. You should call super method.
     * @param buffer - bytes for a write operation.
     * @throws IOException 
     */
    @Override
    protected void write(byte... buffer) throws IOException {
        setControlByte(buffer[0]);                                                  // First byte in a sequence is a control byte for 8591
        this.analogValue = buffer.length > 1 ? buffer[buffer.length - 1] : 0x00;    // Set analog output value to last transmitted value except control byte
        buffer = appendAnalogOutputValue(buffer);
        super.write(buffer);                                                        // To change body of generated methods, choose Tools | Templates.
    }
    
    public void writeAnalogChannel(byte value) throws IOException {
        write((byte) (this.controlByte | ANALOG_OUTPUT_ENABLED), value);
    }

    private byte[] appendAnalogOutputValue(byte... buffer) {
        if (buffer.length == 1) {
            buffer = Arrays.copyOf(buffer, 2);
            buffer[1] = analogValue;
        }
        return buffer;
    }

    @Override
    public void close() throws Exception {
        write((byte)0x00);
        super.close(); 
    }
    
    

    @Override
    public String toString() {
        return "PCF8591{" + "controlByte=" + controlByte + ", analogValue=" + analogValue + ", currentChannel=" + currentChannel + ", autoIncrement=" + autoIncrement + '}';
    }
}
