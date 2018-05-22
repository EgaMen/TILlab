package com.company;

import java.io.*;
/*
* Bспользует алфавит из 257 символов. 256 символов для байтовых значений
  * и 1 символ- EOF. Сначала рассматривается как канонический код а потом переводится в код хафмана
 */

public final class HuffmanUsual {



    public static FrequencyTable getFrequencies(File file) throws IOException {
        FrequencyTable freqs = new FrequencyTable(new int[257]);
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
    static void writeCodeLengthTable(Output out, HuffmanCanonical canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);

            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");


            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }
    static void compress(HuffmanTree code, InputStream in, Output out) throws IOException {
        HuffmanCoder enc = new HuffmanCoder(out);
        enc.huffmanTree = code;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);
        }
        enc.write(256);  // EOF
    }

    static HuffmanCanonical readCodeLengthTable(Input in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = (val << 1) | in.readNoEof();
            codeLengths[i] = val;
        }
        return new HuffmanCanonical(codeLengths);
    }
    static void decompress(HuffmanTree code, Input in, OutputStream out) throws IOException {
        HuffmanCoder dec = new HuffmanCoder(in);
        dec.huffmanTree = code;
        while (true) {
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
        }
    }
}

