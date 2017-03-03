/**
 * Copyright (C) 2015-2016 Cristian Ioan Vasile <cvasile@bu.edu>
 * Hybrid and Networked Systems (HyNeSs) Group, BU Robotics Lab, Boston University
 * See license.txt file for license information.
 */
package stl;

/**
 * Operation enumeration class defines the allowed Boolean and temporal
 * operators used in the STL grammar.
 *
 * @author Cristian-Ioan Vasile
 */

public enum Operation {
    NOP, NOT, OR, AND, UNTIL, EVENT, ALWAYS, PRED, IMPLIES, BOOL, CONCAT, PARALLEL;

    /**
     * Gets the code corresponding to the string representation.
     * @param text string representation of an operation
     * @return the operation code of this operation
     */

    public static Operation getCode(String text) {
        if(text.equals("!")) return Operation.NOT;
        if(text.equals("&&")) return Operation.AND;
        if(text.equals("||")) return Operation.OR;
        if(text.equals("=>")) return Operation.IMPLIES;
        if(text.equals("U")) return Operation.UNTIL;
        if(text.equals("F")) return Operation.EVENT;
        if(text.equals("G")) return Operation.ALWAYS;
        if(text.equals(">>")) return Operation.CONCAT;
        if(text.equals("#")) return Operation.PARALLEL;
        return Operation.NOP;
    }

    /**
     * Gets custom string representation for each operation.
     * @param op the operation code of an operation
     * @return string representation of this operation
     */
    public static String getString(Operation op) {
        switch(op) {
            case NOT: return "!";
            case AND: return "&&";
            case OR: return "||";
            case IMPLIES: return "=>";
            case UNTIL: return "U";
            case EVENT: return "F";
            case ALWAYS: return "G";
            case CONCAT: return ">>";
            case PARALLEL: return "#";
            default: return "";
        }
    }
}