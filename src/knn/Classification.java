package knn;

import java.util.List;

public class Classification {
	
	private List<Pattern> trainingSet;
	private List<Pattern> testingSet;
	private int kNumber;
	
	public Classification(List<Pattern> trainingSet, List<Pattern> testingSet, int kNumber) {
		this.trainingSet = trainingSet;
		this.testingSet = testingSet;
		this.kNumber = kNumber;
	}

}
