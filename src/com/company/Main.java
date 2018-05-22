package com.company;

import java.io.*;


import static com.company.Adaptive.compress;
import static com.company.Adaptive.decompress;
import static com.company.HuffmanUsual.getFrequencies;
import static com.company.HuffmanUsual.readCodeLengthTable;
import static com.company.HuffmanUsual.writeCodeLengthTable;


public class Main {

    public static void main(String[] args) {

        File inputFile  = new File("C:/Users/EgaMen/IdeaProjects/TI/dddd");
        File outputFile = new File("C:/Users/EgaMen/IdeaProjects/TI/eeeeeee.txt");
        Main aa = new Main();


            aa.AdaptiveDecode(inputFile, outputFile);

        // write your code here
    }
    public void AdaptiveEncode(File inputFile, File outputFile) {


        // Perform file compression
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (Output out = new Output(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                compress(in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void AdaptiveDecode(File inputFile, File outputFile) {
        try (Input in = new Input(new BufferedInputStream(new FileInputStream(inputFile)))) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                decompress(in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Encode(File inputFile, File outputFile) throws IOException {
        FrequencyTable freqs = getFrequencies(inputFile);
        freqs.increment(256);  // EOF symbol gets a frequency of 1
        HuffmanTree code = freqs.buildCodeTree();
        HuffmanCanonical canonCode = new HuffmanCanonical(code, 257);
        code = canonCode.toCodeTree();
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (Output out = new Output(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeCodeLengthTable(out, canonCode);
                HuffmanUsual.compress(code, in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Decode(File inputFile, File outputFile) throws IOException {
        try (Input in = new Input(new BufferedInputStream(new FileInputStream(inputFile)))) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                HuffmanCanonical canonCode = readCodeLengthTable(in);
                HuffmanTree code = canonCode.toCodeTree();
                HuffmanUsual.decompress(code, in, out);
            }
        }

    }
}
