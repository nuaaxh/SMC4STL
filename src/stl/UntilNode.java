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
public class UntilNode extends TemporalBinaryNode {

    public UntilNode(TreeNode left, TreeNode right, double l, double h) {
        super(Operation.UNTIL, left, right, l, h);
    }

    @Override
    public double robustness(Trace s, double t) {
        double value = -maximumRobustness;
        double r, rLeft, rRight;
        double rAcc = maximumRobustness;
        for(int tu=(int) t; tu < (int)(t+this.low); tu++) {
            rLeft = this.left.robustness(s, tu);
            if(rAcc > rLeft) {
                rAcc = rLeft;
            }
        }
        for(int tu=(int) (t+this.low); tu <= (int)(t+this.high); tu++) {
            rLeft = this.left.robustness(s, tu);
            rRight = this.right.robustness(s, tu);
            if(rAcc > rLeft) {
                rAcc = rLeft;
            }
            r = Math.max(rAcc, rRight);
            if(value < r) {
                value = r;
            }
        }
        return value;
    }

    @Override
    public TreeNode negate() {
        return new UntilNode(this.left.negate(),this.right.negate(),this.low,this.high);
    }

    public String toString(){
        return "(U["  + low + "," + high + "]" + left + " " + right +  ")";
    }

    @Override
    public TreeNode shifted(double shift) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
