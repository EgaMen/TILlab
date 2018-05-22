package com.company;

import java.io.EOFException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Objects;
/**
 *  общее количество бит всегда кратно 8.
 */

public final class Input implements AutoCloseable {

    private InputStream input;
    //диапазон [0x00, 0xFF], если бит доступен, либо -1, если eof.
    private int currentByte;
    // Число оставшихся битов в текущем байте, всегда между 0 и 7 (включительно).
    private int numBitsRemaining;

    public Input(InputStream in) {
        input = in;
        currentByte = 0;
        numBitsRemaining = 0;
    }
    /**
     * Возвращает 0 или 1, если бит доступен, или -1, если
     * конец потока достигнут. Конец потока всегда будет на левой границе байта
     */
    public int read() throws IOException {
        if (currentByte == -1)
            return -1;
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1)
                return -1;
            numBitsRemaining = 8;
        }
        if (numBitsRemaining <= 0)
            throw new AssertionError();
        numBitsRemaining--;
        return (currentByte >>> numBitsRemaining) & 1;
    }
    public int readNoEof() throws IOException {
        int result = read();
        if (result != -1)
            return result;
        else
            throw new EOFException();
    }
    public void close() throws IOException {
        input.close();
        currentByte = -1;
        numBitsRemaining = 0;
    }

}
