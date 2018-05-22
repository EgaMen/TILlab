package main;

import java.util.Objects;



public final class FrequencyTable implements IFT {


    private int[] frequencies;


    private int[] cumulative;


    private int total;




    public FrequencyTable(int[] freqs) {
        Objects.requireNonNull(freqs);
        if (freqs.length < 1)
            throw new IllegalArgumentException("At least 1 symbol needed");

        frequencies = freqs.clone();  // Make copy
        total = 0;
        for (int x : frequencies) {
            if (x < 0)
                throw new IllegalArgumentException("Negative frequency");
            total = checkedAdd(x, total);
        }
        cumulative = null;
    }



    public FrequencyTable(IFT freqs) {
        if (freqs == null)
            throw new NullPointerException("Argument is null");
        int numSym = freqs.getSymbolLimit();
        if (numSym < 1)
            throw new IllegalArgumentException("At least 1 symbol needed");

        frequencies = new int[numSym];
        total = 0;
        for (int i = 0; i < frequencies.length; i++) {
            int x = freqs.get(i);
            if (x < 0)
                throw new IllegalArgumentException("Negative frequency");
            frequencies[i] = x;
            total = checkedAdd(x, total);
        }
        cumulative = null;
    }




    public int getSymbolLimit() {
        return frequencies.length;
    }



    public int get(int symbol) {
        checkSymbol(symbol);
        return frequencies[symbol];
    }



    public void set(int symbol, int freq) {
        checkSymbol(symbol);
        if (freq < 0)
            throw new IllegalArgumentException("Negative frequency");

        int temp = total - frequencies[symbol];
        if (temp < 0)
            throw new AssertionError();
        total = checkedAdd(temp, freq);
        frequencies[symbol] = freq;
        cumulative = null;
    }



    public void increment(int symbol) {
        checkSymbol(symbol);
        if (frequencies[symbol] == Integer.MAX_VALUE)
            throw new ArithmeticException("Arithmetic overflow");
        total = checkedAdd(total, 1);
        frequencies[symbol]++;
        cumulative = null;
    }



    public int getTotal() {
        return total;
    }



    public int getLow(int symbol) {
        checkSymbol(symbol);
        if (cumulative == null)
            initCumulative();
        return cumulative[symbol];
    }



    public int getHigh(int symbol) {
        checkSymbol(symbol);
        if (cumulative == null)
            initCumulative();
        return cumulative[symbol + 1];
    }



    private void initCumulative() {
        cumulative = new int[frequencies.length + 1];
        int sum = 0;
        for (int i = 0; i < frequencies.length; i++) {

            sum = checkedAdd(frequencies[i], sum);
            cumulative[i + 1] = sum;
        }
        if (sum != total)
            throw new AssertionError();
    }



    private void checkSymbol(int symbol) {
        if (symbol < 0 || symbol >= frequencies.length)
            throw new IllegalArgumentException("Symbol out of range");
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < frequencies.length; i++)
            sb.append(String.format("%d\t%d%n", i, frequencies[i]));
        return sb.toString();
    }



    private static int checkedAdd(int x, int y) {
        int z = x + y;
        if (y > 0 && z < x || y < 0 && z > x)
            throw new ArithmeticException("Arithmetic overflow");
        else
            return z;
    }

}