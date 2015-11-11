package knn;

public class Pattern {
	
	private static final String UNKNOWN_CLASS = "?";
	
	private double[] vector;
	private String patternClass;
	private double distance;
	
	public Pattern(double[] vector) {
		this(vector, UNKNOWN_CLASS);
	}
	
	public Pattern(double[] vector, String patternClass) {
		this.vector = vector;
		this.patternClass = patternClass;
		this.distance = Double.MAX_VALUE;
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
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
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
}
