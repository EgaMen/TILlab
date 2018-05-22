package main;

import java.io.IOException;
import java.util.Objects;



public final class Decoder extends CoderBase {




    private Input input;


    private long code;




    public Decoder(Input in) throws IOException {
        super();
        Objects.requireNonNull(in);
        input = in;
        code = 0;
        for (int i = 0; i < STATE_SIZE; i++)
            code = code << 1 | readCodeBit();
    }




    public int read(IFT freqs) throws IOException {
        return read(new CheckedFrequency(freqs));
    }


    public int read(CheckedFrequency freqs) throws IOException {

        long total = freqs.getTotal();
        if (total > MAX_TOTAL)
            throw new IllegalArgumentException("Cannot decode symbol because total is too large");
        long range = high - low + 1;
        long offset = code - low;
        long value = ((offset + 1) * total - 1) / range;
        if (value * range / total > offset)
            throw new AssertionError();
        if (value < 0 || value >= total)
            throw new AssertionError();


        int start = 0;
        int end = freqs.getSymbolLimit();
        while (end - start > 1) {
            int middle = (start + end) >>> 1;
            if (freqs.getLow(middle) > value)
                end = middle;
            else
                start = middle;
        }
        if (start + 1 != end)
            throw new AssertionError();

        int symbol = start;
        if (offset < freqs.getLow(symbol) * range / total || freqs.getHigh(symbol) * range / total <= offset)
            throw new AssertionError();
        update(freqs, symbol);
        if (code < low || code > high)
            throw new AssertionError("Code out of range");
        return symbol;
    }


    protected void shift() throws IOException {
        code = ((code << 1) & MASK) | readCodeBit();
    }


    protected void underflow() throws IOException {
        code = (code & TOP_MASK) | ((code << 1) & (MASK >>> 1)) | readCodeBit();
    }



    private int readCodeBit() throws IOException {
        int temp = input.read();
        if (temp == -1)
            temp = 0;
        return temp;
    }

}