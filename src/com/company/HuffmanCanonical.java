package com.company;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



public final class HuffmanCanonical {
    private int[] codeLengths;
    public HuffmanCanonical(int[] codeLens) {

        Objects.requireNonNull(codeLens);
        if (codeLens.length < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        for (int cl : codeLens) {
            if (cl < 0)
                throw new IllegalArgumentException("Illegal code length");
        }
        codeLengths = codeLens.clone();
        Arrays.sort(codeLengths);
        int currentLevel = codeLengths[codeLengths.length - 1];
        int numNodesAtLevel = 0;
        for (int i = codeLengths.length - 1; i >= 0 && codeLengths[i] > 0; i--) {
            int cl = codeLengths[i];
            while (cl < currentLevel) {
                if (numNodesAtLevel % 2 != 0)
                    throw new IllegalArgumentException("Under-full Huffman code tree");
                numNodesAtLevel /= 2;
                currentLevel--;
            }
            numNodesAtLevel++;
        }
        while (currentLevel > 0) {
            if (numNodesAtLevel % 2 != 0)
                throw new IllegalArgumentException("Under-full Huffman code tree");
            numNodesAtLevel /= 2;
            currentLevel--;
        }
        if (numNodesAtLevel < 1)
            throw new IllegalArgumentException("Under-full Huffman code tree");
        if (numNodesAtLevel > 1)
            throw new IllegalArgumentException("Over-full Huffman code tree");

        // Copy again
        System.arraycopy(codeLens, 0, codeLengths, 0, codeLens.length);
    }
    public HuffmanCanonical(HuffmanTree tree, int symbolLimit) {
        Objects.requireNonNull(tree);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        codeLengths = new int[symbolLimit];
        buildCodeLengths(tree.root, 0);
    }
    private void buildCodeLengths(Knot knot, int depth) {
        if (knot instanceof InternalKnot) {
            InternalKnot internalNode = (InternalKnot) knot;
            buildCodeLengths(internalNode.leftChild , depth + 1);
            buildCodeLengths(internalNode.rightChild, depth + 1);
        } else if (knot instanceof Leaf) {
            int symbol = ((Leaf) knot).symbol;
            // Note: HuffmanTree already has a checked constraint that disallows a symbol in multiple leaves
            if (codeLengths[symbol] != 0)
                throw new AssertionError("Symbol has more than one code");
            if (symbol >= codeLengths.length)
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            codeLengths[symbol] = depth;
        } else {
            throw new AssertionError("Illegal knot type");
        }
    }
    public int getSymbolLimit() {
        return codeLengths.length;
    }



    public int getCodeLength(int symbol) {
        if (symbol < 0 || symbol >= codeLengths.length)
            throw new IllegalArgumentException("Symbol out of range");
        return codeLengths[symbol];
    }



    public HuffmanTree toCodeTree() {
        List<Knot> knots = new ArrayList<Knot>();
        for (int i = max(codeLengths); i >= 0; i--) {  // Descend through code lengths
            if (knots.size() % 2 != 0)
                throw new AssertionError("Violation of canonical code invariants");
            List<Knot> newKnots = new ArrayList<Knot>();

            // // Добавляет лист для каждого символа с коде>0
            if (i > 0) {
                for (int j = 0; j < codeLengths.length; j++) {
                    if (codeLengths[j] == i)
                        newKnots.add(new Leaf(j));
                }
            }

            // Merge pairs of knots from the previous deeper layer
            for (int j = 0; j < knots.size(); j += 2)
                newKnots.add(new InternalKnot(knots.get(j), knots.get(j + 1)));
            knots = newKnots;
        }

        if (knots.size() != 1)
            throw new AssertionError("Violation of canonical code invariants");
        return new HuffmanTree((InternalKnot) knots.get(0), codeLengths.length);
    }
    private static int max(int[] array) {
        int result = array[0];
        for (int x : array)
            result = Math.max(x, result);
        return result;
    }

}