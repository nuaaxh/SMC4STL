/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package stl;

/**
 * @author Cristian-Ioan Vasile
 *
 */
public abstract class TemporalBinaryNode extends TreeNode {
    /**
     * The left child of this tree node.
     */
    public TreeNode left;
    /**
     * The right child of this tree node.
     */
    public TreeNode right;
    /**
     * The lower bound of the temporal operator's interval.
     */
    public double low;
    /**
     * The upper bound of the temporal operator's interval.
     */
    public double high;

    public TemporalBinaryNode(Operation op, TreeNode left, TreeNode right, double l, double h) {
        super(op);
        this.left = left;
        this.right = right;
        this.low = l;
        this.high = h;
    }

    public String toString() {
        return "(" + op + "[" + low + ", " + high + "] " + left + " " + right + ")";
    }
}
