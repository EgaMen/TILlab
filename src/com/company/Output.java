package com.company;



import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

//так как байтовый поток, то в конец потокапишется 0 до кратного 8 бит.

public final class Output implements AutoCloseable {

    private OutputStream output;

    private int currentByte;

    private int numBitsFilled;

    public Output(OutputStream out) {
        output = out;
        currentByte = 0;
        numBitsFilled = 0;
    }
    public void write(int b) throws IOException {
        if (b != 0 && b != 1)
            throw new IllegalArgumentException(" 0 or 1");
        currentByte = (currentByte << 1) | b;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }
    public void close() throws IOException {
        while (numBitsFilled != 0)
            write(0);
        output.close();
    }

}