package main;

import java.io.*;


public class ArithmeticUsual {





    public static IFT getFrequencies(File file) throws IOException {
        IFT freqs = new FrequencyTable(new int[257]);
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            while (true) {
                int b = input.read();
                if (b == -1)
                    break;
                freqs.increment(b);
            }
        }
        return freqs;
    }


    static void writeFrequencies(Output out, IFT freqs) throws IOException {
        for (int i = 0; i < 256; i++)
            writeInt(out, 32, freqs.get(i));
    }



    static void compress(IFT freqs, InputStream in, Output out) throws IOException {
        Encoder enc = new Encoder(out);
        while (true) {
            int symbol = in.read();
            if (symbol == -1)
                break;
            enc.write(freqs, symbol);
        }
        enc.write(freqs, 256);  // EOF
        enc.finish();  // Flush remaining code bits
    }


    private static void writeInt(Output out, int numBits, int value) throws IOException {
        if (numBits < 0 || numBits > 32)
            throw new IllegalArgumentException();

        for (int i = numBits - 1; i >= 0; i--)
            out.write((value >>> i) & 1);  // Little endian
    }

    static IFT readFrequencies(Input in) throws IOException {
        int[] freqs = new int[257];
        for (int i = 0; i < 256; i++)
            freqs[i] = readInt(in, 32);
        freqs[256] = 1;  // EOF symbol
        return new FrequencyTable(freqs);
    }



    static void decompress(IFT freqs, Input in, OutputStream out) throws IOException {
        Decoder dec = new Decoder(in);
        while (true) {
            int symbol = dec.read(freqs);
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
        }
    }



    private static int readInt(Input in, int numBits) throws IOException {
        if (numBits < 0 || numBits > 32)
            throw new IllegalArgumentException();

        int result = 0;
        for (int i = 0; i < numBits; i++)
            result = (result << 1) | in.readNoEof();  // Big endian
        return result;
    }
}
