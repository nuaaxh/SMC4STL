/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package stl;


/**
 * RelOperation enumeration class defines the operation codes for inequality
 * operators used in predicates of STL formulae.
 * @author Cristian-Ioan Vasile
 */
public enum RelOperation {
    NOP, LT, LE, GT, GE, EQ;

    /**
     *
     * @param text
     * @return
     */
    public static RelOperation getCode(String text) {
        if(text.equals("<")) return LT;
        if(text.equals("<=")) return LE;
        if(text.equals(">")) return GT;
        if(text.equals(">=")) return GE;
        if(text.equals("=")) return EQ;
        return NOP;
    }

    public static String getString(RelOperation rop){
        switch(rop){
            case LT:
                return "<";
            case LE:
                return "<=";
            case GT:
                return ">";
            case GE:
                return ">=";
            case EQ:
                return "=";
            default:
                return "NOP";
        }
    }

}

