package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class Main {
	
	private static final String INFO_PARSING_REGEX = ";";
	private static final String VECTOR_PARSING_REGEX = ",";
	
	private static File trainingSetFile;
	private static File testingSetFile;
	private static int kNumber;
	private static int distanceType;
	private static int useAdaptiveDistanceMeasure;
	private static Classification knnClassification;

	public static void main(String[] args) {
		if(args.length != 5) {
			System.err.println("You have to pass two argumets: path to the file with training dataset"
					+ ", path to the file with testing dataset"
					+ " and number of nearest neighbors used for classification"
					+ "(make sure this number doesn't exceed number of training patterns in first file).");
		}
		else {
			try{
				trainingSetFile = new File(args[0]);
				testingSetFile = new File(args[1]);
				kNumber = Integer.parseInt(args[2]);
				distanceType = Integer.parseInt(args[3]);
				useAdaptiveDistanceMeasure = Integer.parseInt(args[4]);
				knnClassification = new Classification(getSetFromFile(trainingSetFile, true), getSetFromFile(testingSetFile, false), kNumber, distanceType, useAdaptiveDistanceMeasure);
			}
			catch(NullPointerException e1) {
				System.err.println("No path to the file: \n" + e1.getMessage());
			}
			catch(NumberFormatException e2) {
				System.err.println("Wrong number format: \n" + e2.getMessage());
			}
			catch(FileNotFoundException e3) {
				System.err.println("File not found: \n" + e3.getMessage());
			}
			catch(IOException e4) {
				System.err.println("Parsing of the file failed: \n" + e4.getMessage());
			}
			finally {
				System.out.println("Program exiting.");
				System.exit(0);
			}
		}	
	}
	
	private static List<Pattern> getSetFromFile(File testingSetFile, boolean trainingSet) throws FileNotFoundException, IOException, NumberFormatException {
		ArrayList<Pattern> setFromFile = new ArrayList<Pattern>();
		
		BufferedReader br = new BufferedReader(new FileReader(trainingSetFile));
		
		int patternCnt = Integer.parseInt(br.readLine());
		String[] myLineSplit = null;
		String[] myVectorSplit = null;
		double[] parsedVector = null;
		
		for(int i = 0; i < patternCnt; i++) {
			myLineSplit = br.readLine().split(INFO_PARSING_REGEX);
			myVectorSplit = myLineSplit[0].split(VECTOR_PARSING_REGEX);
			parsedVector = new double[myVectorSplit.length];
			
			for(int j = 0; j < myVectorSplit.length; j++) {
				parsedVector[j] = Double.parseDouble(myVectorSplit[j]);
			}
			
			Pattern newPattern = null;
			
			if(trainingSet) newPattern = new Pattern(parsedVector, myLineSplit[1], myLineSplit[1]);
			else newPattern = new Pattern(parsedVector, myLineSplit[1]);
			
			setFromFile.add(newPattern);
		}
		
		br.close();
		return setFromFile;
	}
	
	

}
