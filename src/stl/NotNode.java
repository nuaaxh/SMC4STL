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
public class NotNode extends BooleanUnaryNode {

    public NotNode(TreeNode ch) {
        super(Operation.NOT, ch);
    }

    @Override
    public double robustness(Trace s, double t) {
        return -this.child.robustness(s, t);
    }

    @Override
    public TreeNode negate() {
        return this.child;
    }

    public String toString(){
        return "(" + Operation.getString(Operation.NOT) + child + ")" ;
    }

    @Override
    public TreeNode shifted(double shift) {
        return new NotNode(this.child.shifted(shift));
    }

}
