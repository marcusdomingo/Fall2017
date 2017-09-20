import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kNN {

	private int POSITIVE_REVIEWS = 0;
	private int NEGATIVE_REVIEWS = 0;
	private final int NUM_REVIEWS = 18506;
	private List<String> positiveList = new ArrayList<String>();
	private List<String> negativeList = new ArrayList<String>();
	private List<Integer> positiveFreqs = new ArrayList<Integer>();
	private List<Integer> negativeFreqs = new ArrayList<Integer>();
	private List<Integer> positiveOccurs = new ArrayList<Integer>();
	private List<Integer> negativeOccurs = new ArrayList<Integer>();
	private List<Double> positiveWeights = new ArrayList<Double>();
	private List<Double> negativeWeights = new ArrayList<Double>();
	private List<String> allWords = new ArrayList<String>();
	private List<Integer> allFreqs = new ArrayList<Integer>();
	private List<Integer> allOccurs = new ArrayList<Integer>();
	private List<Integer> allRanks = new ArrayList<Integer>();
	private List<Double> allWeights = new ArrayList<Double>();
	
	public static void main(String[] args) {
		
		kNN knn = new kNN();
		BufferedReader br = null;
		FileReader fr = null;
		
		try {

			fr = new FileReader("1504108575_8218148_train_file.data");
			br = new BufferedReader(fr);

			String sCurrentLine;
			int count = 1;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.split("\\s");
				if(line[0].equals("+1")) {
					System.out.println("+1:" + count);
					knn.POSITIVE_REVIEWS++;
					knn.addToList(line, true);
				} else if (line[0].equals("-1")) {
					System.out.println("-1:" + count);
					knn.addToList(line, false);
					knn.NEGATIVE_REVIEWS++;
				} else {
					System.out.println("Houstoun we have a problem.");
					break;
				}
				count++;
			}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		knn.calculateWeight();
		
		System.out.println("Positive: " + knn.positiveList.size() + " " + knn.positiveFreqs.size() + " " + knn.positiveOccurs.size() + " " + knn.positiveWeights.size());
		System.out.println("Negative: " + knn.negativeList.size() + " " + knn.negativeFreqs.size() + " " + knn.negativeOccurs.size() + " " + knn.negativeWeights.size());
		System.out.println("All: " + knn.allWords.size() + " " + knn.allFreqs.size() + " " + knn.allOccurs.size() + " " + knn.allWeights.size());

		try {
			Thread.sleep(6000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {

			fr = new FileReader("1504108575_8450022_test.data");
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("result.dat"))) {
				
				while ((sCurrentLine = br.readLine()) != null) {
					String[] line = sCurrentLine.replaceAll("[^\\w\\s]", "").toLowerCase().split("\\s");
					double[] scores = knn.getScore(line);
					double positiveScore = scores[0];
					double negativeScore = scores[1];
					if(positiveScore > 0) {
						System.out.println("+1 ::::: " + positiveScore + ":" + negativeScore);
						bw.write("+1\n");
					} else if (0 > positiveScore) {
						System.out.println("-1  ::::: " + positiveScore + ":" + negativeScore);
						bw.write("-1\n");
					} else {
						System.out.println("There is a problem: " + positiveScore + ":" + negativeScore);
						bw.write("-1\n");
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
	
	private void addToList(String[] line, boolean isPositive) {

		List<String> tmpWords1 = new ArrayList<String>();
		List<String> tmpWords2 = new ArrayList<String>();
		String[] common = {"the","of","and","a","to","in","is","you","that","it","he","was","for","on","are","as","with","his","they","I","at","be","this","have","from","or","one","had","by","word","but","what","all","were","we","when","your","can","said","there","use","an","each","which","she","do","how","their","if","will","up","other","about","out","many","then","them","these","so","some","her","would","make","like","him","into","time","has","look","two","more","write","go","see","number","way","could","people","my","than","first","water","been","call","who","oil","its","now","find","long","down","day","did","get","come","made","may","part"};
//		String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
//		String[] lucene = {"a", "an", "and", "are", "as", "at", "be", "but", "by",
//				"for", "if", "in", "into", "is", "it",
//				"no", "not", "of", "on", "or", "such",
//				"that", "the", "their", "then", "there", "these",
//				"they", "this", "to", "was", "will", "with"};
//		for(int i = 1; i < line.length; i++) {
//			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
//			if(!s.equals("")) { //&& !Arrays.asList(common).contains(s)) {
//				if(!tmpWords.contains(s)) {
//					if(listArray.contains(s)) {
//						int wordIndex = listArray.indexOf(s);
//						int newFreq = listFreqs.get(wordIndex) + 1;
//						listFreqs.set(wordIndex, newFreq);
//					} else {
//						listArray.add(s);
//						listFreqs.add(1);
//					}
//					tmpWords.add(s);
//				}
//			}
//		}
		for(int i = 1; i < line.length; i++) {
			String s = line[i].replaceAll("[^\\w\\s]", "").toLowerCase();
			if(!s.equals("") && !Arrays.asList(common).contains(s)) {
				if(allWords.contains(s)) {
					int wordIndex = allWords.indexOf(s);
					int newWeight;
					if(isPositive) {
						newWeight = allRanks.get(wordIndex) + 1;
					} else {
						newWeight = allRanks.get(wordIndex) - 1;
					}
					int newFreq = allFreqs.get(wordIndex) + 1;
					allRanks.set(wordIndex, newWeight);
					allFreqs.set(wordIndex, newFreq);
					if(!tmpWords1.contains(s)) {
						int newOccur = allOccurs.get(wordIndex) + 1;
						allOccurs.set(wordIndex, newOccur);
						tmpWords1.add(s);
					}
				} else {
					allWords.add(s);
					if(isPositive) {
						allRanks.add(1);
					} else {
						allRanks.add(-1);
					}
					allFreqs.add(1);
					allOccurs.add(1);
					tmpWords1.add(s);
				}
				
				if(isPositive) {
					if(positiveList.contains(s)) {
						int wordIndex = positiveList.indexOf(s);
						int newFreq = positiveFreqs.get(wordIndex) + 1;
						positiveFreqs.set(wordIndex, newFreq);
						if(!tmpWords2.contains(s)) {
							int newOccur = positiveOccurs.get(wordIndex) + 1;
							positiveOccurs.set(wordIndex, newOccur);
							tmpWords2.add(s);
						}
					} else {
						positiveList.add(s);
						positiveFreqs.add(1);
						positiveOccurs.add(1);
						tmpWords2.add(s);
					}
				} else {
					if(negativeList.contains(s)) {
						int wordIndex = negativeList.indexOf(s);
						int newFreq = negativeFreqs.get(wordIndex) + 1;
						negativeFreqs.set(wordIndex, newFreq);
						if(!tmpWords2.contains(s)) {
							int newOccur = negativeOccurs.get(wordIndex) + 1;
							negativeOccurs.set(wordIndex, newOccur);
							tmpWords2.add(s);
						}
					} else {
						negativeList.add(s);
						negativeFreqs.add(1);
						negativeOccurs.add(1);
						tmpWords2.add(s);
					}
				}
			}
		}
	}
	
	private void calculateWeight() {
		for(int i = 0; i < positiveList.size(); i++) {
			int freq = positiveFreqs.get(i);
			int occur = positiveOccurs.get(i);
			double weight = freq * (Math.log((POSITIVE_REVIEWS/occur))/Math.log(2));
			positiveWeights.add(weight);
		}
		for(int i = 0; i < negativeList.size(); i++) {
			int freq = negativeFreqs.get(i);
			int occur = negativeOccurs.get(i);
			double weight = freq * (Math.log((NEGATIVE_REVIEWS/occur))/Math.log(2));
			negativeWeights.add(weight);
		}
		for(int i = 0; i < allWords.size(); i++) {
			int freq = allFreqs.get(i);
			int occur = allOccurs.get(i);
//			System.out.println(freq + " " + occur);
			double weight = freq * (Math.log((NUM_REVIEWS/occur))/Math.log(2));
			allWeights.add(weight);
		}
	}
	
	private double[] getScore(String[] line) {
		
		double[] scores = {0,0};
		
//		for(int i = 0; i < line.length; i++) {
//			String s = line[i];
//			if(positiveList.contains(s)) {
//				int wordIndex = positiveList.indexOf(s);
//				double num = positiveWeights.get(wordIndex);
//				scores[0] = scores[0] + num;
//			}
//			if(negativeList.contains(s)) {
//				int wordIndex = negativeList.indexOf(s);
//				double num = negativeWeights.get(wordIndex);
//				scores[1] = scores[1] + num;
//			}
//		}
		
		for(int i = 0; i < line.length; i++) {
			String s = line[i];
			if(allWords.contains(s)) {
				int wordIndex = allWords.indexOf(s);
				int rank = allRanks.get(wordIndex);
				double num = allWeights.get(wordIndex);
				if(rank > 0) {
					scores[0] = scores[0] + num;
				} else {
					scores[0] = scores[0] - num;
				}
			}
		}
				
//		for(int i = 0; i < line.length; i++) {
//			
//			String s = line[i];
//			
//			if(positiveList.contains(s)) {
//				if(positiveWord.contains(s)) {
//					int wordIndex = positiveWord.indexOf(s);
//					int newFreq = positiveFreq.get(wordIndex) + 1;
//					positiveFreq.set(wordIndex, newFreq);
//				} else {
//					positiveWord.add(s);
//					positiveFreq.add(1);
//				}
//			}
//			
//			if(negativeList.contains(s)) {
//				if(negativeWord.contains(s)) {
//					int wordIndex = negativeWord.indexOf(s);
//					int newFreq = negativeFreq.get(wordIndex) + 1;
//					negativeFreq.set(wordIndex, newFreq);
//				} else {
//					negativeWord.add(s);
//					negativeFreq.add(1);
//				}
//			}
//		}
//		
//		for(int i = 0; i < positiveFreq.size(); i++) {
//			String searchWord = positiveWord.get(i);
//			int allWordIndex = positiveList.indexOf(searchWord);
//			int totalOccurence = listPositiveFreqs.get(allWordIndex);
//			int frequency = positiveFreq.get(i);
//			double a = POSITIVE_REVIEWS/totalOccurence;
//			int summation = 0;
//			for(int j = 0; j < positiveFreq.size(); j++) {
//				summation = summation + (positiveFreq.get(j) * positiveFreq.get(j));
//			}
////			System.out.println(searchWord +" "+ totalOccurence +" "+ frequency +" "+ NUM_REVIEWS);
//			scores[0] = scores[0] + ((frequency/Math.sqrt(summation)) * (Math.log(a)/Math.log(2)));
//		}
//
//		for(int i = 0; i < negativeFreq.size(); i++) {
//			String searchWord = negativeWord.get(i);
//			int allWordIndex = negativeList.indexOf(searchWord);
//			int totalOccurence = listNegativeFreqs.get(allWordIndex);
//			int frequency = (Integer) negativeFreq.get(i);
//			double a = NEGATIVE_REVIEWS/totalOccurence;
//			int summation = 0;
//			for(int j = 0; j < negativeFreq.size(); j++) {
//				summation = summation + (negativeFreq.get(j) * negativeFreq.get(j));
//			}
////			System.out.println(searchWord +" "+ totalOccurence +" "+ frequency +" "+ NUM_REVIEWS);
//			scores[1] = scores[1] + ((frequency/Math.sqrt(summation)) * (Math.log(a)/Math.log(2)));
//		}
		
		return scores;
	}
}
