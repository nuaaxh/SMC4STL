/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package stl;

import data.Trace;

/**
 * @author Cristian-Ioan Vasile
 *
 */
public class ConjunctionNode extends BooleanBinaryNode {

    public ConjunctionNode(TreeNode left, TreeNode right) {
        super(Operation.AND, left, right);
    }

    @Override
    public double robustness(Trace s, double t) {
        return Math.min(left.robustness(s, t), right.robustness(s, t));
    }

    @Override
    public TreeNode negate() {
        return new DisjunctionNode(left.negate(),right.negate());
    }

    @Override
    public String toString(){
        return "(" + left + " " + Operation.getString(Operation.AND) + " " + right + ")";
    }

    @Override
    public TreeNode shifted(double shift) {
        return new ConjunctionNode(this.left.shifted(shift),this.right.shifted(shift));
    }
}
