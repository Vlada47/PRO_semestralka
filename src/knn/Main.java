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
	private static Classification knnClassification;

	public static void main(String[] args) {
		if(args.length != 3) {
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
				knnClassification = new Classification(getTrainingSet(trainingSetFile), getTestingSet(testingSetFile), kNumber);
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
				System.err.println("Parsing the file failed: \n" + e4.getMessage());
			}
			finally {
				System.out.println("Program exiting.");
				System.exit(0);
			}
		}	
	}
	
	private static List<Pattern> getTrainingSet(File trainingSetFile) throws FileNotFoundException, IOException, NumberFormatException {
		ArrayList<Pattern> trainingSet = new ArrayList<Pattern>();
		
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
			
			Pattern newPattern = new Pattern(parsedVector, myLineSplit[1]);
			trainingSet.add(newPattern);
		}
		
		br.close();
		return trainingSet;
	}
	
	private static List<Pattern> getTestingSet(File testingSetFile) throws FileNotFoundException, IOException, NumberFormatException {
		ArrayList<Pattern> testingSet = new ArrayList<Pattern>();
		
		BufferedReader br = new BufferedReader(new FileReader(trainingSetFile));
		
		int patternCnt = Integer.parseInt(br.readLine());
		String[] myVectorSplit = null;
		double[] parsedVector = null;
		
		for(int i = 0; i < patternCnt; i++) {
			myVectorSplit = br.readLine().split(VECTOR_PARSING_REGEX);
			parsedVector = new double[myVectorSplit.length];
			
			for(int j = 0; j < myVectorSplit.length; j++) {
				parsedVector[j] = Double.parseDouble(myVectorSplit[j]);
			}
			
			Pattern newPattern = new Pattern(parsedVector);
			testingSet.add(newPattern);
		}
		
		br.close();
		return testingSet;
	}
	
	

}
