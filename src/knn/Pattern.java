package knn;

public class Pattern {
	
	private double[] vector;
	private String patternClass;
	private String correctClass;
	private double distance;
	private double adaptiveDistanceMeasure;
	
	/**
	 * Constructor for testing patterns.
	 * @param vector
	 * @param correctClass
	 */
	public Pattern(double[] vector, String correctClass) {
		this(vector, null, correctClass);
	}
	
	/**
	 * Constructor for training patterns.
	 * @param vector
	 * @param patternClass
	 * @param correctClass
	 */
	public Pattern(double[] vector, String patternClass, String correctClass) {
		this.vector = vector;
		this.patternClass = patternClass;
		this.correctClass = correctClass;
		this.distance = Double.MAX_VALUE;
		this.adaptiveDistanceMeasure = 1.0;
	}

	/**
	 * @return the vector
	 */
	public double[] getVector() {
		return vector;
	}

	/**
	 * @return the patternClass
	 */
	public String getPatternClass() {
		return patternClass;
	}

	/**
	 * @return the correctClass
	 */
	public String getCorrectClass() {
		return correctClass;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return the adaptiveDistanceMeasure
	 */
	public double getAdaptiveDistanceMeasure() {
		return adaptiveDistanceMeasure;
	}

	/**
	 * @param patternClass the patternClass to set
	 */
	public void setPatternClass(String patternClass) {
		this.patternClass = patternClass;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @param adaptiveDistanceMeasure the adaptiveDistanceMeasure to set
	 */
	public void setAdaptiveDistanceMeasure(double adaptiveDistanceMeasure) {
		this.adaptiveDistanceMeasure = adaptiveDistanceMeasure;
	}
}
