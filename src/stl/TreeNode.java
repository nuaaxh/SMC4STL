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
public abstract class TreeNode {

    public static double maximumRobustness = 1e12;

    /**
     * The operation associated with this tree node.
     */
    public Operation op;

    /**
     * Class constructor.
     * @param op operation associated with this tree node
     */
    public TreeNode(Operation op) {
        this.op  = op;
    }

    abstract public TreeNode negate();

    abstract public TreeNode shifted(double shift);

    abstract public double robustness(Trace s, double t);
}

