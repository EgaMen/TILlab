package com.company;

/**
 * Внутренний узел. Имеет два потомка.
 */
public final class InternalKnot extends Knot {
    public final Knot leftChild;  // Не может быть  null
    public final Knot rightChild;
    public InternalKnot(Knot left, Knot right) {
        leftChild = left;
        rightChild = right;
    }
}
