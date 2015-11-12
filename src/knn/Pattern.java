package knn;

/**
 * Class for creating instances of patterns, which holds vectors and their classes (+ some other values).
 * @author Vladimír Láznièka
 *
 */
public class Pattern {
	
	/**
	 * Vector of the pattern.
	 */
	private double[] vector;
	
	/**
	 * Class of the pattern. It's set from the start for training patterns,
	 * for testing patterns it's set during the classification.
	 */
	private String patternClass;
	
	/**
	 * Correct class of the pattern. For training patterns it's the same class as {@code patternClass},
	 * for testing patterns is set from the start and it's used for specifying, whether classification
	 * was successful or not.
	 */
	private String correctClass;
	
	/**
	 * Distance of the pattern from another based on their vectors. It's used solely for storing the distance
	 * between training patterns and test pattern being classified (it's not stored for testing patterns themselves). 
	 */
	private double distance;
	
	/**
	 * Adaptive distance measure is calculated for training patterns based on their distance from nearest pattern
	 * with different class.
	 */
	private double adaptiveDistanceMeasure;
	
	/**
	 * Constructor for testing patterns. It doesn't have any class yet.
	 * @param vector		vector of the pattern
	 * @param correctClass	class that is deemed correct
	 */
	public Pattern(double[] vector, String correctClass) {
		this(vector, null, correctClass);
	}
	
	/**
	 * Constructor for training patterns. It has the class.
	 * @param vector		vector of the pattern
	 * @param patternClass	class of the pattern
	 * @param correctClass	correct class - for training patterns is same as the {@code patternClass}
	 */
	public Pattern(double[] vector, String patternClass, String correctClass) {
		this.vector = vector;
		this.patternClass = patternClass;
		this.correctClass = correctClass;
		this.distance = Double.MAX_VALUE;
		this.adaptiveDistanceMeasure = 1.0;
	}

	/**
	 * Getter for the vector of this pattern.
	 * @return the vector
	 */
	public double[] getVector() {
		return vector;
	}

	/**
	 * Getter for the class of this pattern.
	 * @return the patternClass
	 */
	public String getPatternClass() {
		return patternClass;
	}

	/**
	 * Getter for the correct class of this pattern.
	 * @return the correctClass
	 */
	public String getCorrectClass() {
		return correctClass;
	}

	/**
	 * Getter for the set distance on this training pattern.
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Getter for the adaptive distance measure on this training patterns.
	 * @return the adaptiveDistanceMeasure
	 */
	public double getAdaptiveDistanceMeasure() {
		return adaptiveDistanceMeasure;
	}

	/**
	 * Setter for the class of this testing pattern.
	 * @param patternClass the patternClass to set
	 */
	public void setPatternClass(String patternClass) {
		this.patternClass = patternClass;
	}

	/**
	 * Setter for the distance of this training pattern.
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Setter for the adaptive distance measure of this training pattern.
	 * @param adaptiveDistanceMeasure the adaptiveDistanceMeasure to set
	 */
	public void setAdaptiveDistanceMeasure(double adaptiveDistanceMeasure) {
		this.adaptiveDistanceMeasure = adaptiveDistanceMeasure;
	}
}
