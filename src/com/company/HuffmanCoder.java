package com.company;

import java.io.IOException;
import java.util.List;


//читает и декодирует символы или пишет и кодирует

final class HuffmanCoder {
    private Input input;
    private Output output;
    HuffmanTree huffmanTree;
    HuffmanCoder(Input in) {
        input = in;
    }
    HuffmanCoder(Output out) {
        output = out;
    }
    int read() throws IOException {
        if (huffmanTree == null)
            throw new NullPointerException("Code tree is null");

        InternalKnot currentNode = huffmanTree.root;
        while (true) {
            int temp = input.readNoEof(); //читаем символ до конца
            Knot nextKnot;
            if      (temp == 0) nextKnot = currentNode.leftChild; //влево 0 вправо 1 дерево построение
            else if (temp == 1) nextKnot = currentNode.rightChild;
            else throw new AssertionError("Invalid value from readNoEof()");

            if (nextKnot instanceof Leaf)
                return ((Leaf) nextKnot).symbol;
            else if (nextKnot instanceof InternalKnot)
                currentNode = (InternalKnot) nextKnot;
            else
                throw new AssertionError("Illegal knot type");
        }
    }


    void write(int symbol) throws IOException {
        if (huffmanTree == null)
            throw new NullPointerException("Code tree is null");
        List<Integer> bits = huffmanTree.getCode(symbol); //код символа лист длинный ...
        for (int b : bits) //биты в числе
            output.write(b);
    }

}