package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.STLAbstractSyntaxTreeExtractor;
import parser.STLLexer;
import parser.STLParser;
import stl.TreeNode;

import data.Trace;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Main {

	public static void main(String[] args) {
		try {
			if (args.length > 1) {
				if (args[0].equals("-h") || args[0].equals("--h") || args[0].equals("-help")
						|| args[0].equals("--help")) {
					System.out
							.println("Usage:  java -jar SMC4STL-<version>.jar <confidence_interval> <directory_of_csv_traces> <property_to_check>");
					return;
				}
				if (args.length > 2) {
					String property = args[2];
					for (int i = 3; i < args.length; i++) {
						property += args[i];
					}
					double confidenceInterval = Double.parseDouble(args[0]);
					File dir = new File(args[1]);
					if (!dir.isDirectory()) {
						System.out.println("Second argument must be a directory of CSV trace files.");
					}
					else {
						List<String> traceFiles = new ArrayList<String>();
						for (File file : dir.listFiles()) {
							if (file.getName().endsWith(".csv") || file.getName().endsWith(".CSV")) {
								traceFiles.add(file.getAbsolutePath());
							}
						}
						double satisfyingPercent = computeSatisfyingPercent(traceFiles, property);
						double error = computeError(satisfyingPercent, traceFiles.size(), confidenceInterval);
						System.out.printf("%.2f", (satisfyingPercent * 100));
						System.out.println("% of traces satisfy the property.");
						System.out.print("The true satisfying percentage is between ");
						System.out.printf("%.2f", Math.max(((satisfyingPercent - error) * 100), 0.0));
						System.out.print("% and ");
						System.out.printf("%.2f", Math.min(((satisfyingPercent + error) * 100), 100.0));
						System.out.print("% with ");
						System.out.printf("%.2f", (confidenceInterval * 100));
						System.out.println("% confidence.");
					}
				}
			}
			else {
				System.out.println("Incorrect number of arguments.");
				System.out
						.println("Usage:  java -jar SMC4STL-<version>.jar <confidence_interval> <directory_of_csv_traces> <property_to_check>");
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double getRobustness(String trace, String property) throws FileNotFoundException {
		STLLexer lexer = new STLLexer(new ANTLRInputStream(property));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		STLParser parser = new STLParser(tokens);
		ParserRuleContext t = parser.property();
		TreeNode ast = new STLAbstractSyntaxTreeExtractor().visit(t);
		return ast.robustness(Trace.parseCSV(trace), 0);
	}

	public static double computeSatisfyingPercent(List<String> traces, String property) throws IOException {
		int sat = 0;
		for (String trace : traces) {
			if (getRobustness(trace, property) >= 0) {
				sat++;
			}
		}
		return ((double) sat) / ((double) traces.size());
	}

	public static double computeError(double satisfyingPercent, int numTraces, double confidenceInterval) {
		NormalDistribution distribution = new NormalDistribution(0, 1);
		double confidenceValue = distribution.inverseCumulativeProbability(confidenceInterval + ((1 - confidenceInterval) / 2));
		return confidenceValue * Math.sqrt((1 - satisfyingPercent) / (satisfyingPercent * numTraces));
	}

}
