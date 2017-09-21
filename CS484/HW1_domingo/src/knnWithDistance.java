/*
 * Author: Marcus Domingo
 * Date: 9/21/2017
 * HW 1: K-Nearest Neighbor Classifier
 * Description: Uses KNN with Euclidean distance method to calculate the nearest neighbors.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class knnWithDistance {
	
	// Dictionaries contain all the words that occur in the collection
	// Dictionaries run in parallel with the word occurrence
	// Ranks is either a positive or negative value for the word dependent on the collection frequency
	private List<String> trainDictionary = new ArrayList<String>();
	private List<String> testDictionary = new ArrayList<String>();
	private List<Integer> trainOccurs = new ArrayList<Integer>();
	private List<Integer> testOccurs = new ArrayList<Integer>();
	private List<Integer> trainRanks = new ArrayList<Integer>();
	
	// Words contain the matrices of the words that appear in a review
	// Words run in parallel with the word frequency in the review
	private List<String[]> trainWords = new ArrayList<String[]>();
	private List<String[]> testWords = new ArrayList<String[]>();
	private List<Integer[]> trainFreqs = new ArrayList<Integer[]>();
	private List<Integer[]> testFreqs = new ArrayList<Integer[]>();
	private List<Boolean> trainReviews = new ArrayList<Boolean>();
	
	// X and Y contain the x(positive) and the y(negative) values of each review.
	private List<Double> trainX = new ArrayList<Double>();
	private List<Double> trainY = new ArrayList<Double>();
	private List<Double> testX = new ArrayList<Double>();
	private List<Double> testY = new ArrayList<Double>();
	
	public static void main(String[] args) {
		
		// To call private variables and methods
		knnWithDistance knn = new knnWithDistance();
		
		// To read in file
		BufferedReader br = null;
		FileReader fr = null;
		
		try {

			// Read in train file
			fr = new FileReader("1504108575_8218148_train_file.data");
			br = new BufferedReader(fr);
			
			// Count to know what line is being read in from training data
			int count = 1;

			String sCurrentLine;
			
			// Read all the lines
			while ((sCurrentLine = br.readLine()) != null) {
				// Split the current line on all spaces
				String[] line  = sCurrentLine.split("\\s");
				
				// Print line count and increment
				System.out.println(count);
				count++;
				
				// Check if the review was positive or negative
				if(line[0].equals("+1")) {
					
					// Add a true for positive
					// Replace characters that link words with spaces and split again on spaces
					// Call buildTrain
					knn.trainReviews.add(true);
					line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
					knn.buildTrain(line, true);
				} else if (line[0].equals("-1")) {
					
					// Add a false for negative
					// Replace characters that link words with spaces and split again on spaces
					// Call buildTrain
					knn.trainReviews.add(false);
					line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
					knn.buildTrain(line, false);
				} else {
					
					// Should never enter here
					System.out.println("Houstoun we have a problem.");
					System.exit(0);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		// Calculate tf-idf weights for training data
		knn.tfidfTrain();
				
		try {

			// Read in test file
			fr = new FileReader("1504108575_8450022_test.data");
			br = new BufferedReader(fr);
			
			// Count to know what line is being read in from test data
			int count = 1;

			String sCurrentLine;
			
			// Read all the lines
			while ((sCurrentLine = br.readLine()) != null) {
				
				// Replace characters that link words with spaces and split on spaces
				String[] line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
				
				// Print line count and increment
				System.out.println(count);
				count++;
				
				// Call buildTest
				knn.buildTest(line);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			
			// Close the buffered and file readers
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		
		// Calculate the tf-idf weights for test data
		knn.tfidfTest();

		// Open a buffered writer
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("result.dat"))) {
			
			// Count for lines to write
			int count = 0;
			
			// Number of neighbors to grab
			int k = 201;
			
			// Only do 18506 times
			while (count != 18506) {
				
				// Lists of neighbors and their types (positive/negative)
				List<Double> neighbors = new ArrayList<Double>();
				List<Boolean> neighborTypes = new ArrayList<Boolean>();
				
				// For all the training reviews
				for(int i = 0; i < knn.trainReviews.size(); i++) {
					
					// Get the x1 - x0 and y1 - y0
					// Then calculate the Euclidean distance
					double x = (knn.trainX.get(i) - knn.testX.get(count));
					double y = (knn.trainY.get(i) - knn.testY.get(count));
					double dist = Math.sqrt((x*x) + (y*y));
					
					// If neighbors is empty add
					// Else inserted sort into neighbors and their types
					// Remove the last element if it is over the amount of k elements
					if(neighbors.isEmpty()) {
						neighbors.add(dist);
						neighborTypes.add(knn.trainReviews.get(i));
					} else {
						int j;
						for(j = 0; j < neighbors.size(); j++) {
							if(neighbors.get(j) > dist) {
								break;
							}
						}
						if(j < k) {
							if(j < neighbors.size()) {
								neighbors.add(j, dist);
								neighborTypes.add(j, knn.trainReviews.get(i));
							} else {
								neighbors.add(dist);
								neighborTypes.add(knn.trainReviews.get(i));
							}
						}
						if(neighbors.size() > k) {
							neighbors.remove(k);
							neighborTypes.remove(k);
						}
					}
				}
				
				// Positive and negative review counts
				int posCount = 0;
				int negCount = 0;
				
				// Add up the number of positive and negative reviews
				for(int i = 0; i < neighbors.size(); i++) {
					if(neighborTypes.get(i)) {
						posCount++;
					} else {
						negCount++;
					}
				}
				
				// Increment the count
				count++;
				
				// If there are more positive write +1 to file and print for visual
				// Else write -1 to file and print for visual
				if(posCount > negCount) {
					bw.write("+1\n");
					System.out.println("+++++++++++1 :::: " + count);
				} else {
					bw.write("-1\n");
					System.out.println("-----------1 :::: " + count);
				}
			}
			
			// Print done
			System.out.println("Done");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void buildTrain(String[] line, boolean isPositive) {
		
		// tmpWords for array in trainWords
		// tmpFreqs for array in trainFreqs
		List<String> tmpWords = new ArrayList<String>();
		List<Integer> tmpFreqs = new ArrayList<Integer>();
		
		// For words in the line, ignoring the first word because "+1" and "-1"
		for(int i = 1; i < line.length; i++) {
			
			// Remove all special characters
			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
			
			// If there is a word
			if(!s.equals("")) {
				
				// If the training dictionary already has the word
				// Add +1 to the rank for positive and -1 for negative
				// Else add to the training dictionary, 1 to the occurrence
				// And +1 to the rank for positive and -1 for negative
				// And add the word to tmpWords and 1 to frequency
				if(trainDictionary.contains(s)) {
					int wordIndex = trainDictionary.indexOf(s);
					int rank = trainRanks.get(wordIndex);
					if(isPositive) {
						trainRanks.set(wordIndex, rank + 1);
					} else {
						trainRanks.set(wordIndex, rank -1);
					}
					
					// If tmpWords already has the word increment the frequency
					// Else increment the words occurrence and add the word to tmpWords and 1 to frequency
					if(tmpWords.contains(s)) {
						int index = tmpWords.indexOf(s);
						tmpFreqs.set(index, tmpFreqs.get(index) + 1);
					} else {
						int newOccur = trainOccurs.get(wordIndex) + 1;
						trainOccurs.set(wordIndex, newOccur);
						tmpWords.add(s);
						tmpFreqs.add(1);
					}
				} else {
					trainDictionary.add(s);
					trainOccurs.add(1);
					if(isPositive) {
						trainRanks.add(1);
					} else {
						trainRanks.add(-1);
					}
					tmpWords.add(s);
					tmpFreqs.add(1);
				}
			}
		}
		
		// Add the array to train words and frequencies
		trainWords.add(Arrays.copyOf(tmpWords.toArray(), tmpWords.toArray().length, String[].class));
		trainFreqs.add(Arrays.copyOf(tmpFreqs.toArray(), tmpFreqs.toArray().length, Integer[].class));
		
	}
	
	private void buildTest(String[] line) {
		
		// tmpWords for array in trainWords
		// tmpFreqs for array in trainFreqs
		List<String> tmpWords = new ArrayList<String>();
		List<Integer> tmpFreqs = new ArrayList<Integer>();
		
		// For words in the line, ignoring the first word because "+1" and "-1"
		for(int i = 0; i < line.length; i++) {
			
			// Remove all special characters
			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
			
			// If there is a word
			if(!s.equals("")) {
				
				// If the test dictionary already has the word
				// Else add to the training dictionary, 1 to the occurrence
				// And add the word to tmpWords and 1 to frequency
				if(testDictionary.contains(s)) {
					
					// If tmpWords already has the word increment the frequency
					// Else increment the words occurrence and add the word to tmpWords and 1 to frequency
					if(tmpWords.contains(s)) {
						int index = tmpWords.indexOf(s);
						tmpFreqs.set(index, tmpFreqs.get(index) + 1);
					} else {
						int wordIndex = testDictionary.indexOf(s);
						int newOccur = testOccurs.get(wordIndex) + 1;
						testOccurs.set(wordIndex, newOccur);
						tmpWords.add(s);
						tmpFreqs.add(1);
					}
				} else {
					testDictionary.add(s);
					testOccurs.add(1);
					tmpWords.add(s);
					tmpFreqs.add(1);
				}
			}
		}
		
		// Add the array to test words and frequencies
		testFreqs.add(Arrays.copyOf(tmpFreqs.toArray(), tmpFreqs.toArray().length, Integer[].class));
		testWords.add(Arrays.copyOf(tmpWords.toArray(), tmpWords.toArray().length, String[].class));
		
	}
	
	private void tfidfTrain() {
		
		// For all the matrices of train words per review
		for(int i = 0; i < trainWords.size(); i++) {
			Integer[] freqs = trainFreqs.get(i);
			String[] words = trainWords.get(i);
			double x = 0;
			double y = 0;
			
			// For each word calculate the tf-idf
			// And add it to x if the rank is positive
			// Else add it to y
			for(int j = 0; j < words.length; j++) {
				int index = trainDictionary.indexOf(words[j]);
				int rank = trainRanks.get(index);
				int occur = trainOccurs.get(index);
				int freq = freqs[j];
				double weight = (freq * (Math.log((18506/occur))/Math.log(2)));
				if(rank > 0) {
					x = x + weight;
				} else {
					y = y + weight;
				}
			}
			
			// Add x to training x and y to training y
			trainX.add(x);
			trainY.add(y);
		}
	}
	
	private void tfidfTest() {
		
		// For all the matrices of test words per review
		for(int i = 0; i < testWords.size(); i++) {
			
			// For each word calculate the tf-idf based
			// If it occurs in the training dictionary
			// And use training occurrence and rank
			// And add it to x if the rank is positive
			// Else add it to y
			Integer[] freqs = testFreqs.get(i);
			String[] words = testWords.get(i);
			double x = 0;
			double y = 0;
			for(int j = 0; j < words.length; j++) {
				if(trainDictionary.contains(words[j])) {
					int index = trainDictionary.indexOf(words[j]);
					int rank = trainRanks.get(index);
					int occur = trainOccurs.get(index);
					int freq = freqs[j];
					double weight = (freq * (Math.log((18506/occur))/Math.log(2)));
					if(rank > 0) {
						x = x + weight;
					} else {
						y = y + weight;
					}
				}
			}
			
			// Add x to test x and y to test y
			testX.add(x);
			testY.add(y);
		}
	}
}
