package knn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class of the program.
 * @author Vladimír Láznièka
 *
 */
public class Main {
	
	/**
	 * Regular expression used in files to split informations (like the vector from the class).
	 */
	private static final String INFO_PARSING_REGEX = ";";
	
	/**
	 * Regular expression used in files to split individual dimensions.
	 */
	private static final String VECTOR_PARSING_REGEX = ",";
	
	/**
	 * File with the training patterns.
	 */
	private static File trainingSetFile;
	
	/**
	 * File with the testing patterns.
	 */
	private static File testingSetFile;
	
	/**
	 * Number of nearest neighbors used for classification.
	 */
	private static int kNumber;
	
	/**
	 * Indicator for the type of distance function used.
	 */
	private static int distanceType;
	
	/**
	 * Indicator whether the adaptive distance measure will be used.
	 */
	private static int useAdaptiveDistanceMeasure;
	
	/**
	 * Instance of the KNN classifier.
	 */
	private static Classification knnClassification;
	
	/**
	 * List with classified testing pattern. 
	 */
	private static List<Pattern> resultSet;
	
	/**
	 * Main method of the program. It takes arguments from the command line, stores them in variables and passes them
	 * to {@code Classification} instance. It then invokes {@code classify} method to classify testing patterns and writes
	 * them to the file via {@code writeResultSet} method.
	 * @param args	arguments from the command line (there have to exactly 5 of them)
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			System.err.println("You have to pass two argumets: path to the file with training dataset"
					+ ", path to the file with testing dataset,"
					+ " number of nearest neighbors used for classification (make sure this number doesn't exceed number of training patterns in first file),"
					+ "specify the distance function (1 - Euclidian, 2 - Manhattan)"
					+ " and specify, whether adaptive distance measure should be used (1 - YES).");
		}
		else {
			try{
				trainingSetFile = new File(args[0]);
				testingSetFile = new File(args[1]);
				kNumber = Integer.parseInt(args[2]);
				distanceType = Integer.parseInt(args[3]);
				useAdaptiveDistanceMeasure = Integer.parseInt(args[4]);
				knnClassification = new Classification(getSetFromFile(trainingSetFile, true), getSetFromFile(testingSetFile, false), kNumber, distanceType, useAdaptiveDistanceMeasure);
				resultSet = knnClassification.classify();
				writeResultSet(resultSet, getAccuracy(resultSet));
				System.out.println("Classification finished.");
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
			catch(IndexOutOfBoundsException e5) {
				System.err.println("Unexpected vector sizes: \n" + e5.getMessage());
			}
			finally {
				System.out.println("Program exiting.");
				System.exit(0);
			}
		}	
	}
	
	/**
	 * Method reading patterns from given file and storing them to the list.
	 * Based on the {@code isTrainingSet} parameter it determines whether to create training or testing patterns.
	 * @param file			the file with patterns in specific format
	 * @param isTrainingSet	indicator, whether file contains training or testing patterns
	 * @return	list with patterns from the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	private static List<Pattern> getSetFromFile(File file, boolean isTrainingSet) throws FileNotFoundException, IOException, NumberFormatException {
		ArrayList<Pattern> setFromFile = new ArrayList<Pattern>();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
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
			
			if(isTrainingSet) newPattern = new Pattern(parsedVector, myLineSplit[1], myLineSplit[1]);
			else newPattern = new Pattern(parsedVector, myLineSplit[1]);
			
			setFromFile.add(newPattern);
		}
		
		br.close();
		return setFromFile;
	}
	
	/**
	 * Method calculating accuracy of the classification based on the success ratio of correctly determined class.
	 * @param resultSet	list with classified testing patterns
	 * @return	accuracy of the classification (a number between 0 and 1)
	 */
	private static double getAccuracy(List<Pattern> resultSet) {
		double accuracy = 0.0;
		int correctCnt = 0;
		
		for(Pattern p : resultSet) {
			if(p.getPatternClass().equals(p.getCorrectClass())) correctCnt++;
		}
		
		accuracy = ((double)correctCnt) / resultSet.size();
		
		return accuracy;
	}
	
	/**
	 * Method for writing classified testing patterns to the file is specific format.
	 * It writes the lines in this order: {@code vector;classified class;correct class}.
	 * It also appends the accuracy of the classification as % to the end of the file.
	 * @param resultSet	list with classified testing patterns
	 * @param accuracy	accuracy of the classification
	 * @throws IOException
	 */
	private static void writeResultSet(List<Pattern> resultSet, double accuracy) throws IOException  {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(testingSetFile.getPath()+".result")));
		
		for(Pattern p : resultSet) {
			String line = Arrays.toString(p.getVector()) + INFO_PARSING_REGEX + p.getPatternClass() + INFO_PARSING_REGEX + p.getCorrectClass() + "\n";
			bw.write(line);
		}
		
		bw.write("Accuracy result: "+accuracy*100+"%");
		bw.close();
	}
}
