package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Classification {
	
	public static final int DISTANCE_EUCLIDIAN = 1;
	public static final int DISTANCE_MANHATTAN = 2;
	public static final int NOT_USE_ADM = 0;
	public static final int USE_ADM = 1;
	
	private List<Pattern> trainingSet;
	private List<Pattern> testingSet;
	private int kNumber;
	private int distanceType;
	private int useAdaptiveDistanceMeasure;
	
	public Classification(List<Pattern> trainingSet, List<Pattern> testingSet, int kNumber, int distanceType, int useAdaptiveDistanceMeasure) {
		this.trainingSet = trainingSet;
		this.testingSet = testingSet;
		this.kNumber = kNumber;
		this.distanceType = distanceType;
		this.useAdaptiveDistanceMeasure = useAdaptiveDistanceMeasure;
	}
	
	public List<Pattern> classify() throws IndexOutOfBoundsException {
		if(useAdaptiveDistanceMeasure == USE_ADM) createAdaptiveDistanceMeasures();
		
		for(Pattern testedPattern : testingSet) {
			setDistances(testedPattern);
			sortTrainingPatterns();
			setClassifiedClass(testedPattern);
		}
		
		ArrayList<Pattern> classifiedSet = new ArrayList<Pattern>(testingSet);
		return classifiedSet;
	}
	
	private void createAdaptiveDistanceMeasures() throws IndexOutOfBoundsException {
		for(int i = 0; i < trainingSet.size(); i++) {
			
			double distanceMeasure = Double.MAX_VALUE;
			
			for(int j = 0; j < trainingSet.size(); j++) {
				if(!(trainingSet.get(i).getPatternClass().equals(trainingSet.get(j).getPatternClass()))) {
					double currDistance = 0.0;
					
					switch(distanceType) {
					case DISTANCE_EUCLIDIAN:
						currDistance = calculateDistanceEuclidian(trainingSet.get(i).getVector(), trainingSet.get(j).getVector());
					case DISTANCE_MANHATTAN:
						currDistance = calculateDistanceManhattan(trainingSet.get(i).getVector(), trainingSet.get(j).getVector());
					default:
						currDistance = 1.0;
					}
					
					if(currDistance < distanceMeasure) distanceMeasure = currDistance;
				}
			}
			
			trainingSet.get(i).setAdaptiveDistanceMeasure(distanceMeasure);
		}
	}
	
	private void setDistances(Pattern testedPattern) throws IndexOutOfBoundsException {
		for(Pattern trainingPattern : trainingSet) {
			
			double distance = 0.0;
			
			switch(distanceType) {
			case DISTANCE_EUCLIDIAN:
				distance = calculateDistanceEuclidian(testedPattern.getVector(), trainingPattern.getVector());
			case DISTANCE_MANHATTAN:
				distance = calculateDistanceManhattan(testedPattern.getVector(), trainingPattern.getVector());
			default:
				distance = 0.0;
			}
			
			trainingPattern.setDistance(distance / trainingPattern.getAdaptiveDistanceMeasure());
		}
	}
	
	private void sortTrainingPatterns() {
		PatternComparator pc = new PatternComparator();
		Collections.sort(trainingSet, pc);
	}
	
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
	 * Method for calculating  the Euclidian distance between two given vectors.
	 * @param vector1 array of double values representing the vector
	 * @param vector2 array of double values representing the vector
	 * @return distance between two given vector (double)
	 */
	private double calculateDistanceEuclidian(double[] vector1, double[] vector2) {
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
	 * @author Vlada47
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
