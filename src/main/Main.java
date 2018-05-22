package main;


import java.io.*;


import static main.ArithmeticUsual.compress;
import static main.ArithmeticUsual.decompress;
import static main.ArithmeticUsual.readFrequencies;
import static main.ArithmeticUsual.writeFrequencies;



public class Main {

    public static void main(String[] args) {

        File inputFile  = new File("C:/Users/EgaMen/IdeaProjects/TI/dddd");
        File outputFile = new File("C:/Users/EgaMen/IdeaProjects/TI/eeeeeee.txt");
        Main mm = new Main();

            mm.ArifmeticDecode(inputFile, outputFile);

        // write your code here
    }
    public void ArifmeticEncode(File inputFile, File outputFile) throws IOException {



       IFT freqs = ArithmeticUsual.getFrequencies(inputFile);
        freqs.increment(256);  // EOF symbol gets a frequency of 1


        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
             main.Output out = new main.Output(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
            writeFrequencies(out, freqs);
            compress(freqs, in, out);
        }

    }
    public void ArifmeticDecode(File inputFile, File outputFile) {
        try {
            try (Input in = new Input(new BufferedInputStream(new FileInputStream(inputFile)));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                IFT freqs = readFrequencies(in);
                decompress(freqs, in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
