package com.company;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public final class HuffmanTree {




    public final InternalKnot root;

    // Хранит код каждого сивола или null, если символ не имеет кода.
    // 5 == 10011, (5)==list [1,0,0,1,1].
    private List<List<Integer>> codes;



    public HuffmanTree(InternalKnot root, int symbolLimit) {
        Objects.requireNonNull(root);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");

        this.root = root;
        codes = new ArrayList<List<Integer>>();  // Initially all null
        for (int i = 0; i < symbolLimit; i++)
            codes.add(null);
        buildCodeList(root, new ArrayList<Integer>());  // Заполняет 'codes' соответствующими данными
    }


    // Рекурсивная функция для конструктора
    private void buildCodeList(Knot knot, List<Integer> prefix) {
        if (knot instanceof InternalKnot) {
            InternalKnot internalNode = (InternalKnot) knot;

            prefix.add(0);
            buildCodeList(internalNode.leftChild , prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(internalNode.rightChild, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (knot instanceof Leaf) {
            Leaf leaf = (Leaf) knot;
            if (leaf.symbol >= codes.size())
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            if (codes.get(leaf.symbol) != null)
                throw new IllegalArgumentException("Symbol has more than one code");
            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));

        } else {
            throw new AssertionError("Illegal knot type");
        }
    }




    public List<Integer> getCode(int symbol) {
        if (symbol < 0)
            throw new IllegalArgumentException("Illegal symbol");
        else if (codes.get(symbol) == null)
            throw new IllegalArgumentException("No code for given symbol");
        else
            return codes.get(symbol);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString("", root, sb);
        return sb.toString();
    }


    // Рекурсивная функция для  toString()
    private static void toString(String prefix, Knot knot, StringBuilder sb) {
        if (knot instanceof InternalKnot) {
            InternalKnot internalNode = (InternalKnot) knot;
            toString(prefix + "0", internalNode.leftChild , sb);
            toString(prefix + "1", internalNode.rightChild, sb);
        } else if (knot instanceof Leaf) {
            sb.append(String.format("Code %s: Symbol %d%n", prefix, ((Leaf) knot).symbol));
        } else {
            throw new AssertionError("Illegal knot type");
        }
    }

}