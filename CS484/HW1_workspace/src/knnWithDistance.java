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
	private List<Boolean> reviews = new ArrayList<Boolean>();
	private List<Integer> trainRanks = new ArrayList<Integer>();
	private List<String[]> trainWords = new ArrayList<String[]>();
	private List<String[]> testWords = new ArrayList<String[]>();
	private List<Double> trainX = new ArrayList<Double>();
	private List<Double> trainY = new ArrayList<Double>();
	private List<Double> testX = new ArrayList<Double>();
	private List<Double> testY = new ArrayList<Double>();
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
					System.out.println("Houstoun we have a problem.");
					System.exit(0);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		knn.calculateWeight();
//		System.out.println(knn.trainDictionary.size());
//		System.out.println(knn.trainFreqs.size());
//		System.out.println(knn.trainOccurs.size());
//		System.out.println(knn.trainWords.size());
//		System.out.println(knn.trainX.size());
//		System.out.println(knn.trainY.size());
				
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
		
		knn.calculateWeightTest();
//		System.out.println(knn.testDictionary.size());
//		System.out.println(knn.testFreqs.size());
//		System.out.println(knn.testOccurs.size());
//		System.out.println(knn.testWords.size());

		int count = 0;
		int k = 15;
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("result.dat"))) {
			
			while (!(count > 18506)) {
				
				List<Double> neighbors = new ArrayList<Double>();
				List<Boolean> neighborTypes = new ArrayList<Boolean>();
				
				for(int i = 0; i < knn.reviews.size(); i++) {
					double x = (knn.trainX.get(i) - knn.testX.get(count));
					double y = (knn.trainY.get(i) - knn.testY.get(count));
					double dist = Math.sqrt((x*x) + (y*y));
					
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
				}
				
				int posCount = 0;
				int negCount = 0;
				
				for(int i = 0; i < neighbors.size(); i++) {
					if(neighborTypes.get(i)) {
						posCount++;
					} else {
						negCount++;
					}
				}
				
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
			double x = 0;
			double y = 0;
			for(int j = 0; j < words.length; j++) {
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
			
			trainX.add(x);
			trainY.add(y);
		}
	}
	
	private void calculateWeightTest() {
		for(int i = 0; i < testWords.size(); i++) {
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
					double weight = freq * (freq * (Math.log((18506/occur))/Math.log(2)));
					if(rank > 0) {
						x = x + weight;
					} else {
						y = y + weight;
					}
				}
			}
			
			testX.add(x);
			testY.add(y);
		}
	}
}
