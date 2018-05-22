package com.company;


/**
 * Узел листа в кодовом дереве. Символьное значение.
 */
public final class Leaf extends Knot {
    public final int symbol;  // Всегда положителен
    public Leaf(int sym) {
        if (sym < 0)
            throw new IllegalArgumentException(" non-negative");
        symbol = sym;
    }

}