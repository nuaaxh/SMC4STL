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
public class BooleanLeaf extends TreeNode {
    /**
     * The value associated with this Boolean constant.
     */
    public boolean value;

    public BooleanLeaf(boolean value) {
        super(Operation.BOOL);
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public double robustness(Trace s, double t) {
        return this.value ? maximumRobustness : -maximumRobustness;
    }

    public String toString() {
        return "(" + value + ")";
    }

    @Override
    public TreeNode negate() {
        return new BooleanLeaf(!this.value);
    }

    @Override
    public TreeNode shifted(double shift) {
        return this;
    }
}
