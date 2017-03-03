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
public abstract class BooleanUnaryNode extends TreeNode{
    /**
     * The child node of this tree node.
     */
    public TreeNode child;

    public BooleanUnaryNode(Operation op, TreeNode ch) {
        super(op);
        this.child = ch;
    }

    @Override
    public String toString() {
        return "(" + Operation.getString(op) + " " + child + ")";
    }
}
