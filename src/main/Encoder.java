package main;

import java.io.IOException;
import java.util.Objects;



public final class Encoder extends CoderBase {



    private Output output;


    private int numUnderflow;






    public Encoder(Output out) {
        super();
        Objects.requireNonNull(out);
        output = out;
        numUnderflow = 0;
    }




    public void write(IFT freqs, int symbol) throws IOException {
        write(new CheckedFrequency(freqs), symbol);
    }



    public void write(CheckedFrequency freqs, int symbol) throws IOException {
        update(freqs, symbol);
    }


    public void finish() throws IOException {
        output.write(1);
    }


    protected void shift() throws IOException {
        int bit = (int)(low >>> (STATE_SIZE - 1));
        output.write(bit);

        // Write out the saved underflow bits
        for (; numUnderflow > 0; numUnderflow--)
            output.write(bit ^ 1);
    }


    protected void underflow() {
        if (numUnderflow == Integer.MAX_VALUE)
            throw new ArithmeticException("Maximum underflow reached");
        numUnderflow++;
    }

}