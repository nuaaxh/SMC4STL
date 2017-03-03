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
public class ConcatenationNode extends TreeNode {

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
     * @param left the left child of this tree node
     * @param right the right child of this tree node
     */
    public ConcatenationNode(TreeNode left, TreeNode right) {
        super(Operation.CONCAT);

        this.left = left;
        this.right = right;
    }

    public TreeNode costFunctionEquivalent(){

        if(this.left  instanceof ConjunctionNode){
            ConjunctionNode conjunctionNode = (ConjunctionNode) this.left;
            return new ConjunctionNode(new ConcatenationNode(conjunctionNode.left,this.right),new ConcatenationNode(conjunctionNode.right,right));
        }
        if(this.left instanceof DisjunctionNode){
            DisjunctionNode disjunctionNode = (DisjunctionNode) this.left;
            return new DisjunctionNode(new ConcatenationNode(disjunctionNode.left,this.right),new ConcatenationNode(disjunctionNode.right,this.right));
        }
        if((this.left instanceof EventNode) && (this.right instanceof EventNode)){
            EventNode event1 = (EventNode) this.left;
            return new EventNode(new ConjunctionNode(event1.child,this.right),event1.low,event1.high);
        }
        if((this.left instanceof EventNode) && (this.right instanceof AlwaysNode)){
            EventNode event1 = (EventNode) this.left;
            return new EventNode(new ConjunctionNode(event1.child,this.right),event1.low,event1.high);
        }
        if((this.left instanceof AlwaysNode) && (this.right instanceof AlwaysNode)){
            AlwaysNode always1 = (AlwaysNode) this.left;
            return new ConjunctionNode(this.left,this.right.shifted(always1.high-always1.low));
        }
        if((this.left instanceof AlwaysNode) && (this.right instanceof EventNode)){
            AlwaysNode always1 = (AlwaysNode) this.left;
            return new ConjunctionNode(this.left,this.right.shifted(always1.high-always1.low));
        }
        if(this.left instanceof EventNode){
            EventNode eventNode = (EventNode) this.left;
            return new EventNode(new ConjunctionNode(eventNode.child,this.right),eventNode.low,eventNode.high);
        }
        if(this.left instanceof AlwaysNode){
            AlwaysNode alwaysNode = (AlwaysNode) this.left;
            return new ConjunctionNode(this.left, this.right.shifted(alwaysNode.high - alwaysNode.low));
        }
        if((this.left instanceof LinearPredicateLeaf) || (this.right instanceof LinearPredicateLeaf)){
            System.out.println("Cannot concatenate with LinearPredicate");
            System.err.println("Cannot concatenate with LinearPredicate");
            return null;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see hyness.stl.TreeNode#robustness(hyness.stl.Trace, double)
     */
    @Override
    public double robustness(Trace s, double t) {
        // TODO Is this ok?
        return Math.min(left.robustness(s, t), right.robustness(s, t));
    }

    @Override
    public String toString() {
        return "(" + left + " " + Operation.getString(Operation.CONCAT) + " " + right +")";
        //return "(" + Operation.CONCAT + " " + left + " " + right + ")";
    }

    @Override
    public TreeNode negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TreeNode shifted(double shift) {
        return new ConcatenationNode(this.left.shifted(shift),this.right.shifted(shift));
    }

}
