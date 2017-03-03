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
public class ImplicationNode extends BooleanBinaryNode {

    public ImplicationNode(TreeNode left, TreeNode right) {
        super(Operation.IMPLIES, left, right);
    }

    @Override
    public double robustness(Trace s, double t) {
        return Math.min(-left.robustness(s, t), right.robustness(s, t));
    }

    @Override
    public TreeNode negate() {
        return new DisjunctionNode(this.left,this.right.negate());
    }

    @Override
    public String toString(){
        return "(" + left + Operation.getString(Operation.IMPLIES) + right + ")";
    }

    @Override
    public TreeNode shifted(double shift) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
