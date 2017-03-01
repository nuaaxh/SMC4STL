package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import checker.STLChecker;
import data.Trace;

public class Main {

	public static void main(String[] args) {
		try {
			if (args.length > 1) {
				if (args[0].equals("-h") || args[0].equals("--h") || args[0].equals("-help")
						|| args[0].equals("--help")) {
					System.out
							.println("Usage:  java -jar STLSMC-<version>.jar <directory_of_csv_traces> <property_to_check>");
					return;
				}
				String property = args[1];
				for (int i = 2; i < args.length; i++) {
					property += args[i];
				}
				File dir = new File(args[0]);
				if (!dir.isDirectory()) {
					System.out.println("First argument must be a directory of CSV trace files.");
				}
				else {
					List<String> traceFiles = new ArrayList<String>();
					for (File file : dir.listFiles()) {
						if (file.getName().endsWith(".csv") || file.getName().endsWith(".CSV")) {
							traceFiles.add(file.getAbsolutePath());
						}
					}
					double satisfyingPercent = computeSatisfyingPercent(traceFiles, property);
					System.out.println((satisfyingPercent * 100) + "% of traces satisfy the property.");
					System.out.println("The true satisfying percentage is within "
							+ (computeError(satisfyingPercent, traceFiles.size()) * 100)
							+ "% of this value with 95% confidence.");
				}
			}
			else {
				System.out.println("Incorrect number of arguments.");
				System.out
						.println("Usage:  java -jar STLSMC-<version>.jar <directory_of_csv_traces> <property_to_check>");
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double getRobustness(String trace, String property) throws IOException {
		return getRobustness(Trace.parseCSV(trace), property);
	}

	public static double getRobustness(Trace trace, String property) {
		STLChecker check = new STLChecker(trace);
		return check.robustness(property);
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

	public static double computeError(double satisfyingPercent, int numTraces) {
		return 1.96 * Math.sqrt((1 - satisfyingPercent) / (satisfyingPercent * numTraces));
	}

}
