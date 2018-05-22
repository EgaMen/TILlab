package main;

import java.io.IOException;



public abstract class CoderBase {




    protected final long STATE_SIZE = 32;



    protected final long MAX_RANGE = 1L << STATE_SIZE;


    protected final long MIN_RANGE = (MAX_RANGE >>> 2) + 2;

    protected final long MAX_TOTAL = Math.min(Long.MAX_VALUE / MAX_RANGE, MIN_RANGE);


    protected final long MASK = MAX_RANGE - 1;

    protected final long TOP_MASK = MAX_RANGE >>> 1;


    protected final long SECOND_MASK = TOP_MASK >>> 1;






    protected long low;


    protected long high;




    public CoderBase() {
        low = 0;
        high = MASK;
    }




    protected void update(CheckedFrequency freqs, int symbol) throws IOException {

        if (low >= high || (low & MASK) != low || (high & MASK) != high)
            throw new AssertionError("Low or high out of range");
        long range = high - low + 1;
        if (range < MIN_RANGE || range > MAX_RANGE)
            throw new AssertionError("Range out of range");


        long total = freqs.getTotal();
        long symLow = freqs.getLow(symbol);
        long symHigh = freqs.getHigh(symbol);
        if (symLow == symHigh)
            throw new IllegalArgumentException("Symbol has zero frequency");
        if (total > MAX_TOTAL)
            throw new IllegalArgumentException("Cannot code symbol because total is too large");


        long newLow  = low + symLow  * range / total;
        long newHigh = low + symHigh * range / total - 1;
        low = newLow;
        high = newHigh;


        while (((low ^ high) & TOP_MASK) == 0) {
            shift();
            low = (low << 1) & MASK;
            high = ((high << 1) & MASK) | 1;
        }


        while ((low & ~high & SECOND_MASK) != 0) {
            underflow();
            low = (low << 1) & (MASK >>> 1);
            high = ((high << 1) & (MASK >>> 1)) | TOP_MASK | 1;
        }
    }



    protected abstract void shift() throws IOException;



    protected abstract void underflow() throws IOException;

}