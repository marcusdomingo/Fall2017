import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class knnWithDistance {
	
	private List<String> trainDictionary = new ArrayList<String>();
	private List<String> testDictionary = new ArrayList<String>();
	private List<Integer[]> trainFreqs = new ArrayList<Integer[]>();
	private List<Integer[]> testFreqs = new ArrayList<Integer[]>();
	private List<Integer> trainOccurs = new ArrayList<Integer>();
	private List<Integer> testOccurs = new ArrayList<Integer>();
	private List<Double[]> allWeights = new ArrayList<Double[]>();
	private List<Boolean> reviews = new ArrayList<Boolean>();
	private List<Integer> trainRanks = new ArrayList<Integer>();
	private List<Double> trainX = new ArrayList<Double>();
	private List<Double> trainY = new ArrayList<Double>();
	private List<Double> testX = new ArrayList<Double>();
	private List<Double> testY = new ArrayList<Double>();
	private List<String[]> trainWords = new ArrayList<String[]>();
	private List<Double[]> trainMatrices = new ArrayList<Double[]>();
	private List<String[]> reviewWords = new ArrayList<String[]>();
	private List<Double[]> reviewMatrix = new ArrayList<Double[]>();
	private List<String[]> testWords = new ArrayList<String[]>();
	private List<Double[]> testMatrices = new ArrayList<Double[]>();
	private String[] stopwords = {};
//	private String[] stopwords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"};
//	private String[] stopwords = {"a", "an", "and", "are", "as", "at", "be", "but", "by",
//			"for", "if", "in", "into", "is", "it",
//			"no", "not", "of", "on", "or", "such",
//			"that", "the", "their", "then", "there", "these",
//			"they", "this", "to", "was", "will", "with"};
//	private String[] stopwords = {"the","of","and","a","to","in","is","you","that","it","he","was","for","on","are","as","with","his","they","I","at","be","this","have","from","or","one","had","by","word","but","what","all","were","we","when","your","can","said","there","use","an","each","which","she","do","how","their","if","will","up","other","about","out","many","then","them","these","so","some","her","would","make","like","him","into","time","has","look","two","more","write","go","see","number","way","could","people","my","than","first","water","been","call","who","oil","its","now","find","long","down","day","did","get","come","made","may","part"};
	
	public static void main(String[] args) {
		knnWithDistance knn = new knnWithDistance();
		BufferedReader br = null;
		FileReader fr = null;
		
		try {

			fr = new FileReader("1504108575_8218148_train_file.data");
			br = new BufferedReader(fr);
			int count = 1;
//			System.out.print("Training: ");

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line  = sCurrentLine.split("\\s");
				System.out.println(count);
				count++;
				if(line[0].equals("+1")) {
					knn.reviews.add(true);
					line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
					knn.giveRanks(line, true);
				} else if (line[0].equals("-1")) {
					knn.reviews.add(false);
					line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
					knn.giveRanks(line, false);
				} else {
//					System.out.println("Houstoun we have a problem.");
					break;
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		knn.calculateWeight();
		System.out.println(knn.trainDictionary.size());
		System.out.println(knn.trainFreqs.size());
		System.out.println(knn.trainOccurs.size());
		System.out.println(knn.trainWords.size());
		System.out.println(knn.trainMatrices.size());
		System.out.println(knn.trainX.size());
		System.out.println(knn.trainY.size());
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		try {
//
//			fr = new FileReader("1504108575_8218148_train_file.data");
//			br = new BufferedReader(fr);
//
//			String sCurrentLine;
//			while ((sCurrentLine = br.readLine()) != null) {
//				String[] line = sCurrentLine.split("\\s");
//				knn.setValues(line);
//			}
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
		
		System.out.println(knn.reviews.size() + " " + knn.trainWords.size() + " " + knn.trainMatrices.size());
		
		try {

			fr = new FileReader("1504108575_8450022_test.data");
			br = new BufferedReader(fr);
			int count = 1;

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.replaceAll("[-_/]", " ").split("\\s");
				System.out.println(count);
				count++;
				knn.giveRanksTest(line);
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		knn.calculateWeightTest();
		System.out.println(knn.testDictionary.size());
		System.out.println(knn.testFreqs.size());
		System.out.println(knn.testOccurs.size());
		System.out.println(knn.testWords.size());
		System.out.println(knn.testMatrices.size());
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		try {

			fr = new FileReader("1504108575_8450022_test.data");
			br = new BufferedReader(fr);

			String sCurrentLine;
			int count = 0;
			int k = 15;
			
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("result.dat"))) {
				
				while ((sCurrentLine = br.readLine()) != null) {
//					while(count < 13000) {
//						sCurrentLine = br.readLine();
//						count++;
//					}
					
					String[] line = sCurrentLine.replaceAll("[^\\w\\s]", " ").toLowerCase().split("\\s");
					int arrayCounter = 0;
					double[] distances = new double[18506];
					List<Double> neighbors = new ArrayList<Double>();
					List<Boolean> neighborTypes = new ArrayList<Boolean>();
					List<String> testWord = Arrays.asList(knn.testWords.get(count));
					List<Double> testMatrix = Arrays.asList(knn.testMatrices.get(count));
//					List<String> testWords = new ArrayList<String>();
//					List<Double> testMatrices = new ArrayList<Double>();
//					List<Integer> testFreq = new ArrayList<Integer>();
//					for(int i = 0; i < line.length; i++) {
//						String s = line[i];
//						if(!s.equals("") && !Arrays.asList(knn.stopwords).contains(s) && knn.trainDictionary.contains(s)) {
//							int wordIndex = knn.trainDictionary.indexOf(s);
//							int rank = knn.allRanks.get(wordIndex);
////							double num = knn.allWeights.get(wordIndex);
//							if(testWords.contains(s)) {
//								int index = testWords.indexOf(s);
//								testFreq.set(index, testFreq.get(index) + 1);
//							} else {
//								testWords.add(s);
//								testFreq.add(1);
//							}
////							if(rank > 0) {
////								x = x + num;
////							} else {
////								y = y + num;
////							}
//						}
//					}
					
//					for(int i = 0; i < testWords.size(); i++) {
//						if(knn.trainDictionary.contains(testWords.get(i))) {
//							int index = knn.trainDictionary.indexOf(testWords.get(i));
//							int occur = knn.trainOccurs.get(index);
//							int freq = testFreq.get(i);
//							testMatrices.add((freq * (Math.log((18506/occur))/Math.log(2))));
//						} else {
//							testMatrices.add((double) 0);
//						}
//					}
					
					for(int i = 0; i < knn.reviews.size(); i++) {
//						double xRev = knn.xValue.get(i);
//						double yRev = knn.yValue.get(i);
						List<String> trainWords = Arrays.asList(knn.trainWords.get(i));
						List<Double> trainMatrices = Arrays.asList(knn.trainMatrices.get(i));
						List<String> occurredWords = new ArrayList<String>();
						double sumSquares = 0;
						double numerator = 0;
						double trainDenom = 0;
						double testDenom = 0;
						double pq;
//						System.out.println(trainWords.toString());
//						System.out.println(testWord.toString());
//						for(int j = 0; j < trainWords.size(); j++) {
//							String trainWord = trainWords.get(j);
//							if(testWord.contains(trainWord)) {
//								int index = testWord.indexOf(trainWord);
//								pq = trainMatrices.get(j) - testMatrix.get(index);
//								sumSquares = (pq*pq) + sumSquares;
////								numerator = (trainMatrices[j]*testMatrices.get(index)) + numerator;
//								occurredWords.add(trainWord);
////								System.out.println(j + " " + pq);
////							}
//							
////							trainDenom = ((trainMatrices[j])*(trainMatrices[j])) + trainDenom;
//							} else {
//								pq = trainMatrices.get(j);
//								sumSquares = (pq*pq) + sumSquares;
////								System.out.println("else: " + j + " " + pq);
//							}
//						}
//						
//						for(int j = 0; j < testWord.size(); j++) {
//							String word = testWord.get(j);
//							if(!trainWords.contains(word)) {
//								pq = testMatrix.get(j);
//								sumSquares = (pq*pq) + sumSquares;
//								
////								System.out.println("Test Side: " + j + " " + pq);
//								
//							}
////							testDenom = ((testMatrices.get(j))*(testMatrices.get(j))) + testDenom;
//						}
//						double denominator = (Math.sqrt(trainDenom))*(Math.sqrt(testDenom));
						double x = (knn.trainX.get(i) - knn.testX.get(count));
						double y = (knn.trainY.get(i) - knn.testY.get(count));
						double dist = Math.sqrt((x*x) + (y*y));
						
//						if(dist == 0) {
//							System.out.println(dist);
//						}
						
						if(neighbors.isEmpty()) {
							neighbors.add(dist);
							neighborTypes.add(knn.reviews.get(i));
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
									neighborTypes.add(j, knn.reviews.get(i));
								} else {
									neighbors.add(dist);
									neighborTypes.add(knn.reviews.get(i));
								}
							}
							if(neighbors.size() > k) {
								neighbors.remove(k);
								neighborTypes.remove(k);
							}
						}
						
//						if(arrayCounter == 0) {
//							neighbors[arrayCounter] = dist;
//							neighborTypes[arrayCounter] = knn.reviews.get(i);
//							arrayCounter++;
////							} else {
////								for(int j = 0; j < arrayCounter; j++) {
////									if(neighbors[j] > 0)
////								}
////							}
//						} else {
//							int j;
//							for(j = 0; j < arrayCounter; j++) {
//								if(neighbors[j] > dist) {
//									break;
//								}
//							}
//							if(j <= arrayCounter) {
//								for(int m = neighbors.length-1; m > j; m--) {
//									neighbors[m] = neighbors[m - 1];
//									neighborTypes[m] = neighborTypes[m - 1];
//								}
//								neighbors[j] = dist;
//								neighborTypes[j] = knn.reviews.get(i);
//							}
//						}
					}
					
//					for(int i = 0; i < distances.size(); i++) {
//						System.out.println(distances.get(i) + " " + reviewsUsed.get(i));
//					}
					
					
//					System.out.println("Distance size: " + distances.size());
					
//					for(int i = 0; i < distances.size(); i++) {
//						if(i < k) {
//							if(neighbors.isEmpty()) {
//								neighbors.add(distances.get(i));
//								neighborTypes.add(knn.reviews.get(i));
//							} else {
//								boolean add = false;
//								for(int j = 0; j < neighbors.size(); j++) {
//									add = false;
//									if(neighbors.get(j) > distances.get(i)) {
//										neighbors.add(j, distances.get(i));
//										neighborTypes.add(j, knn.reviews.get(i));
//										break;
//									}
//									add = true;
//								}
//								if(add) {
//									neighbors.add(distances.get(i));
//									neighborTypes.add(knn.reviews.get(i));
//								}
//							}
//						} else {
//							for(int j = 0; j < neighbors.size(); j++) {
//								int size = neighbors.size();
//								if(neighbors.get(j) > distances.get(i)) {
//									neighbors.remove(size-1);
//									neighborTypes.remove(size-1);
//									neighbors.add(j, distances.get(i));
//									neighborTypes.add(j, knn.reviews.get(i));
//									break;
//								}
//							}
//						}
//					}
					
//					System.out.println(neighbors.size() + " " + neighborTypes.size());
//					System.out.println(neighbors.toString());
//					System.out.println(neighborTypes.toString());
//					
//					
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					int posCount = 0;
					int negCount = 0;
					
					for(int i = 0; i < neighbors.size(); i++) {
						if(neighborTypes.get(i)) {
							posCount++;
						} else {
							negCount++;
						}
					}
					
//					System.out.println("Positive: " + posCount);
//					System.out.println("Negative: " + negCount);
//					
//					try {
//						Thread.sleep(100000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					count++;
					if(posCount > negCount) {
						bw.write("+1\n");
						System.out.println("+++++++++++1 :::: " + count);
					} else {
						bw.write("-1\n");
						System.out.println("-----------1 :::: " + count);
					}
				}
				
				System.out.println("Done");
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	private void giveRanks(String[] line, boolean isPositive) {
		List<String> tmpWords = new ArrayList<String>();
		List<Integer> tmpFreqs = new ArrayList<Integer>();
		for(int i = 1; i < line.length; i++) {
			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
			if(!s.equals("") && !Arrays.asList(stopwords).contains(s)) {
				if(trainDictionary.contains(s)) {
					int wordIndex = trainDictionary.indexOf(s);
					int rank = trainRanks.get(wordIndex);
//					int newFreq = trainFreqs.get(wordIndex) + 1;
//					trainFreqs.set(wordIndex, newFreq);
					if(isPositive) {
						trainRanks.set(wordIndex, rank + 1);
					} else {
						trainRanks.set(wordIndex, rank -1);
					}
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
//					trainFreqs.add(1);
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
		
		trainFreqs.add(Arrays.copyOf(tmpFreqs.toArray(), tmpFreqs.toArray().length, Integer[].class));
		trainWords.add(Arrays.copyOf(tmpWords.toArray(), tmpWords.toArray().length, String[].class));
		
	}
	
	private void giveRanksTest(String[] line) {
		List<String> tmpWords = new ArrayList<String>();
		List<Integer> tmpFreqs = new ArrayList<Integer>();
		for(int i = 0; i < line.length; i++) {
			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
			if(!s.equals("") && !Arrays.asList(stopwords).contains(s)) {
				if(testDictionary.contains(s)) {
					int wordIndex = testDictionary.indexOf(s);
//					int newFreq = trainFreqs.get(wordIndex) + 1;
//					trainFreqs.set(wordIndex, newFreq);
					if(tmpWords.contains(s)) {
						int index = tmpWords.indexOf(s);
						tmpFreqs.set(index, tmpFreqs.get(index) + 1);
					} else {
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
		
		testFreqs.add(Arrays.copyOf(tmpFreqs.toArray(), tmpFreqs.toArray().length, Integer[].class));
		testWords.add(Arrays.copyOf(tmpWords.toArray(), tmpWords.toArray().length, String[].class));
		
	}
	
	private void calculateWeight() {
		for(int i = 0; i < trainWords.size(); i++) {
			Integer[] freqs = trainFreqs.get(i);
			String[] words = trainWords.get(i);
			Double[] weights = new Double[words.length];
			double x = 0;
			double y = 0;
			for(int j = 0; j < words.length; j++) {
				int index = trainDictionary.indexOf(words[j]);
				int rank = trainRanks.get(index);
				int occur = trainOccurs.get(index);
				int freq = freqs[j];
				double weight = freq * (freq * (Math.log((18506/occur))/Math.log(2)));
				weights[j] = weight;
				if(rank > 0) {
					x = x + weight;
				} else {
					y = y + weight;
				}
			}
//			int occur = trainOccurs.get(i);
//			System.out.println(freq + " " + occur);
//			double weight = freq * (Math.log((18506/occur))/Math.log(2));
//			allWeights.add(weight);
			
			trainMatrices.add(weights);
			trainX.add(x);
			trainY.add(y);
		}
	}
	
	private void calculateWeightTest() {
		for(int i = 0; i < testWords.size(); i++) {
			Integer[] freqs = testFreqs.get(i);
			String[] words = testWords.get(i);
			Double[] weights = new Double[words.length];
			double x = 0;
			double y = 0;
//			for(int j = 0; j < words.length; j++) {
//				int index = testDictionary.indexOf(words[j]);
//				int occur = testOccurs.get(index);
//				int freq = freqs[j];
//				weights[j] = (freq * (Math.log((18506/occur))/Math.log(2)));
//			}
			for(int j = 0; j < words.length; j++) {
				if(trainDictionary.contains(words[j])) {
					int index = trainDictionary.indexOf(words[j]);
					int rank = trainRanks.get(index);
					int occur = trainOccurs.get(index);
					int freq = freqs[j];
					double weight = freq * (freq * (Math.log((18506/occur))/Math.log(2)));
					if(rank > 0) {
						x = x + weight;
					} else {
						y = y + weight;
					}
				}
			}
//			int occur = trainOccurs.get(i);
//			System.out.println(freq + " " + occur);
//			double weight = freq * (Math.log((18506/occur))/Math.log(2));
//			allWeights.add(weight);
			
			testMatrices.add(weights);
			testX.add(x);
			testY.add(y);
		}
	}
	
//	private void setValues(String[] line) {
//		List<String> words = new ArrayList<String>();
//		List<Double> values = new ArrayList<Double>();
//		
//		if(line[0].equals("+1")) {
//			reviews.add(true);
//		} else if (line[0].equals("-1")) {
//			reviews.add(false);
//		} else {
//			System.out.println("Houstoun we have a problem.");
//			System.exit(0);
//		}
//		
////		double x = 0;
////		double y = 0;
////		
////		for(int i = 1; i < line.length; i++) {
////			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
////			if(!s.equals("") && !Arrays.asList(stopwords).contains(s)) {
////				int wordIndex = trainDictionary.indexOf(s);
////				int rank = allRanks.get(wordIndex);
////				double num = allWeights.get(wordIndex);
////				if(words.contains(s)) {
////					int index = words.indexOf(s);
////					values.set(index, values.get(index) + num);
////				} else {
////					words.add(s);
////					values.add(num);
////				}
//////				if(rank > 0) {
//////					x = x + num;
//////				} else {
//////					y = y + num;
//////				}
////			}
////		}
////		
//////		xValue.add(x);
//////		yValue.add(y);
////		reviewWords.add(Arrays.copyOf(words.toArray(), words.toArray().length, String[].class));
////		reviewMatrix.add(Arrays.copyOf(values.toArray(), values.toArray().length, Double[].class));
//	}
}
