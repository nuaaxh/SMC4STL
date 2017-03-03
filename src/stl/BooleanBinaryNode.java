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
public abstract class BooleanBinaryNode extends TreeNode{
    /**
     * The left child of this tree node.
     */
    public TreeNode left;
    /**
     * The right child of this tree node.
     */
    public TreeNode right;

    /**
     * Class constructor.
     *
     * @param op operation associate with this tree node
     * @param left the left child of this tree node
     * @param right the right child of this tree node
     */
    public BooleanBinaryNode(Operation op, TreeNode left, TreeNode right) {
        super(op);
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + Operation.getString(op) + " " + right + ")";
        //return "(" + op + " " + left + " " + right + ")";
    }
}

