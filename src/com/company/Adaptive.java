package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class Adaptive {

    static void compress(InputStream in, Output out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanCoder enc = new HuffmanCoder(out);
        enc.huffmanTree = freqs.buildCodeTree();  // Don't need to make canonical code because we don't transmit the code tree
        int count = 0;  // Number of bytes read from the input file
        while (true) {
            // Read and encode one byte
            int symbol = in.read();
            if (symbol == -1)
                break;
            enc.write(symbol);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                enc.huffmanTree = freqs.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
        enc.write(256);  // EOF
    }
    static void decompress(Input in, OutputStream out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanCoder dec = new HuffmanCoder(in);
        dec.huffmanTree = freqs.buildCodeTree();  // Use same algorithm as the compressor
        int count = 0;  // Number of bytes written to the output file
        while (true) {
            // Decode and write one byte
            int symbol = dec.read();
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                dec.huffmanTree = freqs.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }

}
