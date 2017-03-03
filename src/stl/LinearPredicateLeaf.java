/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package stl;

import data.Trace;

/**
 * LinearPredicateLeaf class represents simple linear predicates.
 *
 * @author Cristian-Ioan Vasile
 *
 */
public class LinearPredicateLeaf extends TreeNode {
    /**
     * Name of the variable used in the predicate.
     */
    public String variable;
    /**
     * Value of the spatial parameter used a threshold in the predicate.
     */
    public double threshold;
    /**
     * Type of inequality used in the predicate.
     */
    public RelOperation rop;

    /**
     *
     * @param rop
     * @param var
     * @param th
     */
    public LinearPredicateLeaf(RelOperation rop, String var, double th) {
        super(Operation.PRED);
        this.rop = rop;
        this.variable = var;
        this.threshold = th;
    }

    public double robustness(Trace s, double t) {
        double value = s.getValue(this.variable, t);
        switch(this.rop) {
            case LT: case LE:
                value = this.threshold - value;
                break;
            case GT: case GE:
                value = value - this.threshold;
                break;
            case EQ:
                value = -Math.abs(this.threshold - value);
                break;
            default:
                throw new RuntimeException("Unknown relation!");
        }
        return value;
    }

    public String toString() {
        return "(" + variable + " " + RelOperation.getString(rop) + " " + threshold + ")";
    }

    @Override
    public TreeNode negate() {
        RelOperation newRop;
        switch(this.rop){
            case LT:
                newRop = RelOperation.GE;
                break;
            case LE:
                newRop = RelOperation.GT;
                break;
            case GT:
                newRop = RelOperation.LE;
                break;
            case GE:
                newRop = RelOperation.LT;
                break;
            case EQ: //Change this line..
                return new ConjunctionNode(new LinearPredicateLeaf(RelOperation.GT,this.variable, this.threshold),new LinearPredicateLeaf(RelOperation.LT,this.variable,this.threshold));
            default:
                throw new RuntimeException("Unknown relation!");
        }
        return new LinearPredicateLeaf(newRop,this.variable,this.threshold);
    }

    @Override
    public TreeNode shifted(double shift) {
        return this;
    }
}
