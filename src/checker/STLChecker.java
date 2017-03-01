package checker;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.STLBaseListener;
import parser.STLLexer;
import parser.STLParser;
import parser.STLParser.PropertyContext;
import data.Trace;

/**
 * This class utilizes the listener class that is auto-generated from the ANTLR file specifying the
 * STL grammar. The overridden methods are used to visit nodes in the parse tree and compute the
 * associated robustness signals for each node. These robustness signals are stored as arrays in the
 * ParseTreeProperty map, "values."
 * 
 * @author Curtis Madsen
 * 
 */
public class STLChecker extends STLBaseListener {
	private Trace trace; // The trace to check the property against
	private ParseTreeProperty<double[]> values; // A map that stores the robustness signals of
												// sub-formulae

	/**
	 * Creates a new robustness checker by initializing the property and trace with the provided
	 * variables and by creating a new map for storing robustness signals of sub-formulae.
	 * 
	 * @param trace
	 *            - the trace to check properties against
	 */
	public STLChecker(Trace trace) {
		this.trace = trace;
		values = new ParseTreeProperty<double[]>();
	}

	/**
	 * Parses the provided property and then checks it against the trace using a ParseTreeWalker to
	 * visit each node and compute robustness signals for each sub-formula. This method prints out
	 * the robustness signal of the top-level property before returning a single value representing
	 * the robustness of the trace in the initial state.
	 * 
	 * @param property
	 *            - the property to be checked
	 * @return The robustness value of the trace checked against the provided property
	 */
	public double robustness(String property) {
		CharStream charStream = new ANTLRInputStream(property);
		STLLexer lexer = new STLLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		STLParser parser = new STLParser(tokenStream);
		PropertyContext context = parser.property();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, context);
		if (values.get(context) != null && values.get(context).length > 0) {
			System.out.print("[" + values.get(context)[0]);
			for (int i = 1; i < values.get(context).length; i++) {
				System.out.print(", " + values.get(context)[i]);
			}
			System.out.println("]");
			return values.get(context)[0];
		}
		return Double.NEGATIVE_INFINITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exitBoolean_expr(STLParser.Boolean_exprContext ctx) {
		double[] value = new double[trace.getTimePoints().length];
		// A single boolean value (i.e., "true" or "false")
		if (ctx.getChildCount() == 1) {
			values.put(ctx, values.get(ctx.getChild(0)));
		}
		else if (ctx.getChildCount() == 3) {
			switch (ctx.getChild(1).getText()) {
			// Less than operator
			case "<":
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					double val = values.get(ctx.getChild(2))[i] - values.get(ctx.getChild(0))[i];
					value[i] = val;
				}
				break;
			// Less than or equal to operator
			case "<=":
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					double val = values.get(ctx.getChild(2))[i] - values.get(ctx.getChild(0))[i];
					value[i] = val;
				}
				break;
			// Equals operator
			case "=":
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					double val = values.get(ctx.getChild(0))[i] - values.get(ctx.getChild(2))[i];
					value[i] = -Math.abs(val);
				}
				break;
			// Greater than operator
			case ">":
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					double val = values.get(ctx.getChild(0))[i] - values.get(ctx.getChild(2))[i];
					value[i] = val;
				}
				break;
			// Greater than or equal to operator
			case ">=":
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					double val = values.get(ctx.getChild(0))[i] - values.get(ctx.getChild(2))[i];
					value[i] = val;
				}
				break;
			}
			values.put(ctx, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exitExpr(STLParser.ExprContext ctx) {
		double[] value = new double[trace.getTimePoints().length];
		// A variable or a value
		if (ctx.getChildCount() == 1) {
			values.put(ctx, values.get(ctx.getChild(0)));
		}
		else if (ctx.getChildCount() == 3) {
			// Expression wrapped in parenthesis
			if (ctx.getChild(0).getText().equals("(")) {
				values.put(ctx, values.get(ctx.getChild(1)));
			}
			// Negated expression
			else if (ctx.getChild(0).getText().equals("-(")) {
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = -values.get(ctx.getChild(1))[i];
				}
				values.put(ctx, value);
			}
			// Square root
			else if (ctx.getChild(0).getText().equals("sqrt(")) {
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = Math.pow(values.get(ctx.getChild(1))[i], 0.5);
				}
				values.put(ctx, value);
			}
			// Log base 10
			else if (ctx.getChild(0).getText().equals("log(")) {
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = Math.log10(values.get(ctx.getChild(1))[i]);
				}
				values.put(ctx, value);
			}
			// Natural log
			else if (ctx.getChild(0).getText().equals("ln(")) {
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = Math.log(values.get(ctx.getChild(1))[i]);
				}
				values.put(ctx, value);
			}
			// Absolute value
			else if (ctx.getChild(0).getText().equals("abs(")) {
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = Math.abs(values.get(ctx.getChild(1))[i]);
				}
				values.put(ctx, value);
			}
			// Derivative
			else if (ctx.getChild(0).getText().equals("der(")) {
				value[0] = 0.0;
				for (int i = 1; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = (values.get(ctx.getChild(1))[i] - values.get(ctx.getChild(1))[i - 1])
							/ (trace.getTimePoints()[i] - trace.getTimePoints()[i - 1]);
				}
				values.put(ctx, value);
			}
			// Integral from the initial time point
			else if (ctx.getChild(0).getText().equals("int(")) {
				value[0] = 0.0;
				for (int i = 1; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = value[i - 1]
							+ (((values.get(ctx.getChild(1))[i] + values.get(ctx.getChild(1))[i - 1]) / 2) * (trace
									.getTimePoints()[i] - trace.getTimePoints()[i - 1]));
				}
				values.put(ctx, value);
			}
			else {
				switch (ctx.getChild(1).getText()) {
				// Addition
				case "+":
					for (int i = 0; i < trace.getTimePoints().length; i++) {
						value[i] = values.get(ctx.getChild(0))[i] + values.get(ctx.getChild(2))[i];
					}
					break;
				// Subtraction
				case "-":
					for (int i = 0; i < trace.getTimePoints().length; i++) {
						value[i] = values.get(ctx.getChild(0))[i] - values.get(ctx.getChild(2))[i];
					}
					break;
				// Multiplication
				case "*":
					for (int i = 0; i < trace.getTimePoints().length; i++) {
						value[i] = values.get(ctx.getChild(0))[i] * values.get(ctx.getChild(2))[i];
					}
					break;
				// Division
				case "/":
					for (int i = 0; i < trace.getTimePoints().length; i++) {
						value[i] = values.get(ctx.getChild(0))[i] / values.get(ctx.getChild(2))[i];
					}
					break;
				// Power
				case "^":
					for (int i = 0; i < trace.getTimePoints().length; i++) {
						value[i] = Math.pow(values.get(ctx.getChild(0))[i],
								values.get(ctx.getChild(2))[i]);
					}
					break;
				}
				values.put(ctx, value);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exitProperty(STLParser.PropertyContext ctx) {
		double[] value;
		// Boolean expression
		if (ctx.getChildCount() == 1) {
			values.put(ctx, values.get(ctx.getChild(0)));
		}
		else if (ctx.getChildCount() == 3) {
			// Property wrapped in parenthesis
			if (ctx.getChild(0).getText().equals("(")) {
				values.put(ctx, values.get(ctx.getChild(1)));
			}
			// Negated Property
			else if (ctx.getChild(0).getText().equals("!(")) {
				value = new double[trace.getTimePoints().length];
				for (int i = 0; i < values.get(ctx.getChild(1)).length; i++) {
					value[i] = -values.get(ctx.getChild(1))[i];
				}
				values.put(ctx, value);
			}
			// Logical conjunction
			else if (ctx.getChild(1).getText().equals("&&")) {
				value = new double[trace.getTimePoints().length];
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					value[i] = Math.min(values.get(ctx.getChild(0))[i],
							values.get(ctx.getChild(2))[i]);
				}
				values.put(ctx, value);
			}
			// Logical disjunction
			else if (ctx.getChild(1).getText().equals("||")) {
				value = new double[trace.getTimePoints().length];
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					value[i] = Math.max(values.get(ctx.getChild(0))[i],
							values.get(ctx.getChild(2))[i]);
				}
				values.put(ctx, value);
			}
		}
		else if (ctx.getChildCount() == 6) {
			// Globally property (always true within the time bound)
			if (ctx.getChild(0).getText().equals("G[")) {
				value = new double[trace.getTimePoints().length];
				for (int i = 0; i < values.get(ctx.getChild(5)).length; i++) {
					value[i] = -values.get(ctx.getChild(5))[i];
				}
				value = computeEvetually(Double.parseDouble(ctx.getChild(1).getText()),
						Double.parseDouble(ctx.getChild(3).getText()), value);
				for (int i = 0; i < value.length; i++) {
					value[i] = -value[i];
				}
				values.put(ctx, value);
			}
			// Eventually property (becomes true within the time bound)
			else {
				values.put(
						ctx,
						computeEvetually(Double.parseDouble(ctx.getChild(1).getText()),
								Double.parseDouble(ctx.getChild(3).getText()),
								values.get(ctx.getChild(5))));
			}
		}
		// Until property (left hand side remains true until the right hand side becomes true within
		// the time bound)
		else if (ctx.getChildCount() == 7) {
			values.put(
					ctx,
					computeUntil(Double.parseDouble(ctx.getChild(2).getText()),
							Double.parseDouble(ctx.getChild(4).getText()),
							values.get(ctx.getChild(0)), values.get(ctx.getChild(6))));
		}
	}

	/**
	 * Computes the robustness signal of a bounded until property. This method takes advantage of a
	 * rewrite rule where bounded until can be rewritten using bounded eventually and unbounded
	 * until (lhs U[a,b] rhs ~ F[a,b] rhs && G[0,a] {lhs U rhs}) to produce its final result.
	 * 
	 * @param leftBound
	 *            - the lowerbound of the until property
	 * @param rightBound
	 *            - the upperbound of the until property
	 * @param lhs
	 *            - the left hand side's robustness signal used to compute the until robustness
	 *            signal
	 * @param rhs
	 *            - the right hand side's robustness signal used to compute the until robustness
	 *            signal
	 * @return The robustness signal for this bounded until property
	 */
	private double[] computeUntil(double leftBound, double rightBound, double[] lhs, double[] rhs) {
		// lhs U rhs
		double[] until = computeUnboundedUntil(lhs, rhs);
		// F[a,b] rhs
		double[] eventually = computeEvetually(leftBound, rightBound, rhs);
		// G[0,a] {lhs U rhs}
		for (int i = 0; i < until.length; i++) {
			until[i] = -until[i];
		}
		double[] globally = computeEvetually(0, leftBound, until);
		for (int i = 0; i < globally.length; i++) {
			globally[i] = -globally[i];
		}
		// F[a,b] rhs && G[0,a] {lhs U rhs}
		until = new double[trace.getTimePoints().length];
		for (int i = 0; i < trace.getTimePoints().length; i++) {
			until[i] = Math.min(eventually[i], globally[i]);
		}
		// lhs U[a,b] rhs
		return until;
	}

	/**
	 * Computes the robustness signal of an unbounded until property. This method implements
	 * Algorithm 2 from the paper, A Donze, T. Ferrere, O. Maler, Efficient Robust Monitoring for
	 * STL, CAV 2013. Note: Due to a typo in the paper, the computations of z1 and z2 in the if
	 * statement branches are switched.
	 * 
	 * @param lhs
	 *            - the left hand side's robustness signal used to compute the until robustness
	 *            signal
	 * @param rhs
	 *            - the right hand side's robustness signal used to compute the until robustness
	 *            signal
	 * @return The robustness signal for this unbounded until property
	 */
	private double[] computeUnboundedUntil(double[] lhs, double[] rhs) {
		double[] value = new double[trace.getTimePoints().length];
		int pos = value.length - 1;
		double z0 = Math.max(lhs[lhs.length - 1], rhs[rhs.length - 1]);
		double z1 = z0;
		double z2 = z0;
		double z3 = z0;
		value[pos] = z0;
		pos--;
		for (int i = lhs.length - 2; i >= 0; i--) {
			if (lhs[i] >= lhs[i + 1]) {
				double oldZ1 = z1;
				z1 = Math.min(lhs[i], rhs[i]);
				z2 = Math.max(oldZ1, z1);
				z3 = Math.min(lhs[i + 1], z0);
				z0 = Math.max(z2, z3);
			}
			else {
				z1 = Math.max(rhs[i], rhs[i + 1]);
				z2 = Math.min(z1, lhs[i]);
				z3 = Math.min(lhs[i], z0);
				z0 = Math.max(z2, z3);
			}
			value[pos] = z0;
			pos--;
		}
		return value;
	}

	/**
	 * Computes the robustness signal of a bounded evenutally property. This method implements
	 * Algorithm 3 from the paper, A Donze, T. Ferrere, O. Maler, Efficient Robust Monitoring for
	 * STL, CAV 2013.
	 * 
	 * @param a
	 *            - the lowerbound of the until property
	 * @param b
	 *            - the upperbound of the until property
	 * @param signalRobustness
	 *            - the robustness signal used to compute the eventually robustness signal
	 * @return The robustness signal for this bounded evenutally property
	 */
	private double[] computeEvetually(double a, double b, double[] signalRobustness) {
		double[] timePoints = trace.getTimePoints();
		List<Double> value = new ArrayList<Double>();
		double[] ya = shift(signalRobustness, timePoints, -a);
		double[] yb = shift(signalRobustness, timePoints, -b);
		double[] yPrime = new double[timePoints.length];
		for (int i = 0; i < timePoints.length; i++) {
			if (Double.compare(ya[i], Double.NaN) == 0) {
				yPrime[i] = yb[i];
			}
			else if (Double.compare(yb[i], Double.NaN) == 0) {
				yPrime[i] = ya[i];
			}
			else {
				yPrime[i] = Math.max(ya[i], yb[i]);
			}
		}
		double s = timePoints[0] - b;
		double t = s;
		int i = 0;
		List<Integer> M = new ArrayList<Integer>();
		M.add(i);
		while (t + a < timePoints[signalRobustness.length - 1] && value.size() < timePoints.length) {
			double tiPlus1 = t;
			double tMinM;
			if (M.size() > 0) {
				tMinM = timePoints[M.get(0)] - a;
			}
			else {
				tMinM = Double.NaN;
			}
			if (i + 1 < timePoints.length) {
				tiPlus1 = timePoints[i + 1] - b;
			}
			if (Double.compare(tMinM, Double.NaN) == 0) {
				t = tiPlus1;
			}
			else {
				t = Math.min(tMinM, tiPlus1);
			}
			if (t == tMinM) {
				M.remove(0);
				s = t;
			}
			if (t == tiPlus1) {
				if (i + 1 != timePoints.length) {
					while (M.size() > 0
							&& signalRobustness[i + 1] >= signalRobustness[M.get(M.size() - 1)]) {
						M.remove(M.size() - 1);
					}
					i++;
					M.add(i);
				}
				else {
					for (double timePoint : timePoints) {
						if (timePoint > t) {
							t = timePoint;
							break;
						}
					}
				}
			}
			if (t - s > b - a) {
				s = t - (b - a);
			}
			if (s >= timePoints[0]) {
				if (M.size() > 0) {
					if (Double.compare(yPrime[value.size()], Double.NaN) == 0) {
						value.add(signalRobustness[M.get(0)]);
					}
					else {
						value.add(Math.max(yPrime[value.size()], signalRobustness[M.get(0)]));
					}
				}
				else {
					value.add(yPrime[value.size()]);
					i++;
					M.add(i);
				}
			}
		}
		while (value.size() < timePoints.length) {
			value.add(Double.NaN);
		}
		double[] valueArray = new double[value.size()];
		for (int k = 0; k < value.size(); k++) {
			valueArray[k] = value.get(k);
		}
		return valueArray;
	}

	/**
	 * Shifts a vector's values given the vector, a vector of time points for each of the values in
	 * the vector, and an amount to shift by.
	 * 
	 * @param vector
	 *            - the vector to be shifted
	 * @param timePoints
	 *            - a vector of the time points that are associated with each element of the vector
	 * @param shift
	 *            - the amount of time to shift the vector to the right by
	 * @return The vector after the shift has taken place
	 */
	private double[] shift(double[] vector, double[] timePoints, double shift) {
		List<Double> shifted = new ArrayList<Double>();
		int i = 0;
		for (double timePoint : timePoints) {
			// compute the new shifted time point
			double time = timePoint - shift;
			int j = i;
			for (; j < timePoints.length && j < vector.length; j++) {
				// if the new time point lines up with another element add it to the shifted vector
				if (timePoints[j] == time) {
					shifted.add(vector[j]);
					i = j;
					break;
				}
				// otherwise, compute the interpolated value
				else if (timePoints[j] > time && j > 0 && timePoints[j - 1] < time) {
					double diff = timePoints[j] - timePoints[j - 1];
					double change = vector[j] - vector[j - 1];
					double slope = change / diff;
					shifted.add(vector[j - 1] + (slope * (time - timePoints[j - 1])));
					i = j;
					break;
				}
			}
			// Added unknown values to create a shifted vector of the same length as the original
			if (j == timePoints.length || j == vector.length) {
				shifted.add(Double.NaN);
			}
		}
		double[] shiftedArray = new double[shifted.size()];
		for (int k = 0; k < shifted.size(); k++) {
			shiftedArray[k] = shifted.get(k);
		}
		return shiftedArray;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitTerminal(TerminalNode node) {
		double[] value = new double[trace.getTimePoints().length];
		// Boolean terminal node
		if (node.getSymbol().getType() == STLParser.BOOLEAN) {
			// true
			if (node.getText().equals("true")) {
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					value[i] = Double.POSITIVE_INFINITY;
				}
			}
			// false
			else if (node.getText().equals("false")) {
				for (int i = 0; i < trace.getTimePoints().length; i++) {
					value[i] = Double.NEGATIVE_INFINITY;
				}
			}
			values.put(node, value);
		}
		// Rational terminal node
		else if (node.getSymbol().getType() == STLParser.RATIONAL) {
			for (int i = 0; i < trace.getTimePoints().length; i++) {
				value[i] = Double.parseDouble(node.getText());
			}
			values.put(node, value);
		}
		// Variable terminal node
		else if (node.getSymbol().getType() == STLParser.VARIABLE) {
			values.put(node, trace.getValues(node.getText()));
		}
	}

}
