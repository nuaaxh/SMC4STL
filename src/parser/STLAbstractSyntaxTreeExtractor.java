/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package parser;

import stl.AlwaysNode;
import stl.ConcatenationNode;
import stl.ConjunctionNode;
import stl.DisjunctionNode;
import stl.EventNode;
import stl.ImplicationNode;
import stl.LinearPredicateLeaf;
import stl.NotNode;
import stl.Operation;
import stl.RelOperation;
import stl.TreeNode;
import stl.UntilNode;


/**
 *
 * @author Cristian-Ioan Vasile
 *
 */
public class STLAbstractSyntaxTreeExtractor extends STLBaseVisitor<TreeNode>{

    public static double maximumRobustness = 1e12;

    public TreeNode visitFormula(STLParser.FormulaContext ctx){
        Operation op = Operation.getCode(ctx.op.getText());
        TreeNode ret = null;
        double low = -1, high = -1;
        switch(op) {
            case OR:
                ret = new DisjunctionNode(visit(ctx.left), visit(ctx.right));
                break;
            case AND:
                ret = new ConjunctionNode(visit(ctx.left), visit(ctx.right));
                break;
            case IMPLIES:
                ret = new ImplicationNode(visit(ctx.left), visit(ctx.right));
                break;
            case NOT:
                ret = new NotNode(visit(ctx.child));
                break;
            case UNTIL:
                low = Double.valueOf(ctx.low.getText());
                high = Double.valueOf(ctx.high.getText());
                ret = new UntilNode(visit(ctx.left), visit(ctx.right), low, high);
                break;
            case EVENT:
                low = Double.valueOf(ctx.low.getText());
                high = Double.valueOf(ctx.high.getText());
                ret = new EventNode(visit(ctx.child), low, high);
                break;
            case ALWAYS:
                low = Double.valueOf(ctx.low.getText());
                high = Double.valueOf(ctx.high.getText());
                ret = new AlwaysNode(visit(ctx.child), low, high);
                break;
            case CONCAT:
                ret = new ConcatenationNode(visit(ctx.left), visit(ctx.right));
            default:
                System.out.println("default");
                break;
        }
        return ret;
    }

    public TreeNode visitBooleanPred(STLParser.BooleanPredContext ctx) {
//        System.out.println("BPRED");
        return visit(ctx.booleanExpr());
    }

    public TreeNode visitBooleanExpr(STLParser.BooleanExprContext ctx) {
//        System.out.println("BEXPR " + ctx.left.getText() + " " + ctx.right.getText());
        return new LinearPredicateLeaf(RelOperation.getCode(ctx.op.getText()),
                        ctx.left.getText(),
                        Double.valueOf(ctx.right.getText()));
    }

    public TreeNode visitParprop(STLParser.ParpropContext ctx) {
//        System.out.println("PARS");
        return visit(ctx.child);
    }

//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        STLLexer lexer = new STLLexer(new ANTLRInputStream("!(x < 10) && F[0, 2] y > 2 || G[1, 3] z<=8"));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//        STLParser parser = new STLParser(tokens);
//        ParserRuleContext t = parser.property();
//
//        System.out.println(t.toStringTree(parser));
//
//        TreeNode ast = new STLAbstractSyntaxTreeExtractor().visit(t);
//
//        System.out.println("AST: " + ast);
//
//        String[] varnames = new String[] {new String("x"), new String("y"), new String("z")};
//        double[][] data = new double[][]{{8, 8, 11, 11}, {2, 3, 1, 2}, {3, 9, 8, 9}};
//        Trace s = new Trace(varnames, data, 1);
//
//        System.out.println("r: " + ast.robustness(s, 0));
//    }

}
