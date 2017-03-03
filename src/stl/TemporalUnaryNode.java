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
public abstract class TemporalUnaryNode extends TreeNode {
    /**
     * The child node of this tree node.
     */
    public TreeNode child;
    /**
     * The lower bound of the temporal operator's interval.
     */
    public double low;
    /**
     * The upper bound of the temporal operator's interval.
     */
    public double high;

    public TemporalUnaryNode(Operation op, TreeNode child, double l, double h) {
        super(op);
        this.child = child;
        this.low = l;
        this.high = h;
    }

    public String toString() {
        return "(" + op + "[" + low + ", " + high + "] " + child + ")";
    }
}
