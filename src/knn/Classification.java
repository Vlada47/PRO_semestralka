package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Instance of this class represents the "K Nearest Neighbors" classifier. It holds passed parameters such as what distance function
 * should be used or how many nearest neighbors should be used for classification. Based on these parameters it classifies
 * tested patterns via several methods.
 * @author Vladimír Láznièka
 *
 */
public class Classification {
	
	/**
	 * Constant value indicating the Euclidean distance function will be used.
	 */
	public static final int DISTANCE_EUCLIDEAN = 1;
	
	/**
	 * Constant value indicating the Manhattan distance function will be used.
	 */
	public static final int DISTANCE_MANHATTAN = 2;
	
	/**
	 * Constant value indicating the adaptive distance measure will be used for training the classifier.
	 */
	public static final int USE_ADM = 1;
	
	/**
	 * Set of patterns used as training samples.
	 */
	private List<Pattern> trainingSet;
	
	/**
	 * Set of patterns that will be classified.
	 */
	private List<Pattern> testingSet;
	
	/**
	 * Number of nearest neighbors that will be used for classification.
	 */
	private int kNumber;
	
	/**
	 * Type of the distance function that will be used (1 - Euclidean; 2 - Manhattan).
	 */
	private int distanceType;
	
	/**
	 * Variable indicating, whether the adaptive distance measure will be used to train the classifier (1 - YES, other - NO).
	 */
	private int useAdaptiveDistanceMeasure;
	
	/**
	 * Constructor for the classifier. It stores passed parameters.
	 * @param trainingSet					training set for the classifier
	 * @param testingSet					testing set to be classified
	 * @param kNumber						number of nearest neighbors
	 * @param distanceType					type of the distance function to use
	 * @param useAdaptiveDistanceMeasure	specify, whether the adaptive distance measure will be used
	 */
	public Classification(List<Pattern> trainingSet, List<Pattern> testingSet, int kNumber, int distanceType, int useAdaptiveDistanceMeasure) {
		this.trainingSet = trainingSet;
		this.testingSet = testingSet;
		this.kNumber = kNumber;
		this.distanceType = distanceType;
		this.useAdaptiveDistanceMeasure = useAdaptiveDistanceMeasure;
	}
	
	/**
	 * Main method of the classifier, which calls other methods providing necessary functions of the KNN classifier.
	 * Based on passed parameter it may call method {@code createAdaptiveDistanceMeasures} for setting distance measures
	 * to training patterns. It then takes individual testing patterns and sequentially calculates distance to
	 * training patterns, sort them based on that distance and classifies the class of the tested pattern from first {@code K}
	 * training patterns in sorted list. Lastly it copies the list of classified tested patterns to a new one and return it.
	 * @return	the list with classified testing patterns (they have determined their class)
	 * @throws IndexOutOfBoundsException
	 */
	public List<Pattern> classify() throws IndexOutOfBoundsException {
		if(useAdaptiveDistanceMeasure == USE_ADM) { createAdaptiveDistanceMeasures(); System.out.println("Using ADM"); }
		
		for(Pattern testedPattern : testingSet) {
			setDistances(testedPattern);
			sortTrainingPatterns();
			setClassifiedClass(testedPattern);
		}
		
		ArrayList<Pattern> classifiedSet = new ArrayList<Pattern>(testingSet);
		return classifiedSet;
	}
	
	/**
	 * Method that creates adaptive distance measure to all training patterns.
	 * It iterates through the list for each training pattern calculating the distance between it
	 * and all other patterns with different class. The nearest distance is then chosen as
	 * the adaptive distance measure for the particular training pattern. The method for the distance
	 * function is chosen based on the set parameter {@code distanceType}.
	 * @throws IndexOutOfBoundsException
	 */
	private void createAdaptiveDistanceMeasures() throws IndexOutOfBoundsException {
		for(int i = 0; i < trainingSet.size(); i++) {
			
			double distanceMeasure = Double.MAX_VALUE;
			
			for(int j = 0; j < trainingSet.size(); j++) {
				if(!(trainingSet.get(i).getPatternClass().equals(trainingSet.get(j).getPatternClass()))) {
					double currDistance = 0.0;
					
					switch(distanceType) {
					case DISTANCE_EUCLIDEAN:
						currDistance = calculateDistanceEuclidean(trainingSet.get(i).getVector(), trainingSet.get(j).getVector()); break;
					case DISTANCE_MANHATTAN:
						currDistance = calculateDistanceManhattan(trainingSet.get(i).getVector(), trainingSet.get(j).getVector()); break;
					default:
						currDistance = calculateDistanceEuclidean(trainingSet.get(i).getVector(), trainingSet.get(j).getVector()); break;
					}
					
					if(currDistance < distanceMeasure) distanceMeasure = currDistance;
				}
			}
			
			trainingSet.get(i).setAdaptiveDistanceMeasure(distanceMeasure);
		}
	}
	
	/**
	 * This method sets the distance from passed testing pattern to all training patterns.
	 * The method for the distance function is chosen based on the set parameter {@code distanceType}.
	 * Calculated distance is also divided by the adaptive distance measure value (it's 1.0 if not calculated).
	 * @param testedPattern	testing pattern for which the distance will be calculated
	 * @throws IndexOutOfBoundsException
	 */
	private void setDistances(Pattern testedPattern) throws IndexOutOfBoundsException {
		for(Pattern trainingPattern : trainingSet) {
			
			double distance = 0.0;
			
			switch(distanceType) {
			case DISTANCE_EUCLIDEAN:
				distance = calculateDistanceEuclidean(testedPattern.getVector(), trainingPattern.getVector()); break;
			case DISTANCE_MANHATTAN:
				distance = calculateDistanceManhattan(testedPattern.getVector(), trainingPattern.getVector()); break;
			default:
				distance = calculateDistanceEuclidean(testedPattern.getVector(), trainingPattern.getVector()); break;
			}
			
			trainingPattern.setDistance(distance / trainingPattern.getAdaptiveDistanceMeasure());
		}
	}
	
	/**
	 * Method for sorting out the list with training patterns based on the passed comparator.
	 */
	private void sortTrainingPatterns() {
		PatternComparator pc = new PatternComparator();
		Collections.sort(trainingSet, pc);
	}
	
	/**
	 * Method that determines the class for the passed testing pattern. It iterates through
	 * first patterns of the training set (which should be sorted in ascending order)
	 * based on the {@code kNumber} parameter and stores their classes
	 * in the local list. Next to this list it also increments the number in other list on corresponding index.
	 * The class that is present the most in the local list is then set to the pattern.
	 * @param testedPattern	pattern, which class will be determined
	 */
	private void setClassifiedClass(Pattern testedPattern) {
		ArrayList<String> classes = new ArrayList<String>();
		ArrayList<Integer> scores = new ArrayList<Integer>();
		
		for(int i = 0; i < kNumber; i++) {
			String currClass = trainingSet.get(i).getPatternClass();
			
			if(classes.contains(currClass)) {
				int index = classes.indexOf(currClass);
				scores.set(index, scores.get(index) + 1);
			}
			else {
				classes.add(currClass);
				scores.add(1);
			}
		}
		
		int maxScore = 0;
		String selectedClass = null;
		
		for(int i = 0; i < scores.size(); i++) {
			if(scores.get(i) > maxScore) maxScore = scores.get(i); selectedClass = classes.get(i);
		}
		
		testedPattern.setPatternClass(selectedClass);
	}
	
	/**
	 * Method for calculating  the Euclidean distance between two given vectors.
	 * It will return {@code IndexOutOfBoundsException} if vectors have different dimensions.
	 * @param vector1 array of double values representing the vector
	 * @param vector2 array of double values representing the vector
	 * @return distance between two given vector (double)
	 */
	private double calculateDistanceEuclidean(double[] vector1, double[] vector2) {
		double distance = 0.0;
		
		if(vector1.length == vector2.length) {
			double sum = 0.0;
			for(int i = 0; i < vector1.length; i++) {
				sum += Math.pow((vector1[i] - vector2[i]), 2.0);
			}
			
			distance = Math.sqrt(sum);
		}
		else {
			throw new IndexOutOfBoundsException("Can't calculate distance between vectors!\n"
					+ "Vector 1 length: "+vector1.length+", Vector 2 length: "+vector2.length+".");
		}
		
		return distance;
	}
	
	/**
	 * Method for calculating  the Manhattan distance between two given vectors.
	 * It will return {@code IndexOutOfBoundsException} if vectors have different dimensions.
	 * @param vector1 array of double values representing the vector
	 * @param vector2 array of double values representing the vector
	 * @return distance between two given vector (double)
	 */
	private double calculateDistanceManhattan(double[] vector1, double[] vector2) {
		double distance = 0.0;
		
		if(vector1.length == vector2.length) {
			double sum = 0.0;
			for(int i = 0; i < vector1.length; i++) {
				sum += Math.abs(vector1[i] - vector2[i]);
			}
			
			distance = sum;
		}
		else {
			throw new IndexOutOfBoundsException("Can't calculate distance between vectors!\n"
					+ "Vector 1 length: "+vector1.length+", Vector 2 length: "+vector2.length+".");
		}
		
		return distance;
	}
	
	
	/**
	 * Private class implementing the Comparator interface for sorting collections with
	 * the Pattern instances in ascending order.
	 * @author Vladimír Láznièka
	 *
	 */
	private class PatternComparator implements Comparator<Pattern> {

		@Override
		public int compare(Pattern p1, Pattern p2) {
			int result;
			
			if(p1.getDistance() > p2.getDistance()) result = 1;
			else if(p1.getDistance() < p2.getDistance()) result = -1;
			else result = 0;
			
			return result;
		}
	} 
}