import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		In in = new In(fileName);
        String file = in.readAll().replace("\r", "");
        int i = 0 ;
        while (i<file.length()-windowLength) {

            if (CharDataMap.containsKey(file.substring(i, i+windowLength))) {
                CharDataMap.get(file.substring(i, i+windowLength)).update(file.charAt(i+windowLength));
                i++;
            }
            else{
                List list = new List();
                
                list.update(file.charAt(i+windowLength));
                CharDataMap.put(file.substring(i,i+windowLength) , list);
                i++;
        }
	}
    for (List list : CharDataMap.values() ){
        calculateProbabilities(list);
    }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		double tot = 0.0;
        ListIterator it = probs.listIterator(0);
        while (it.hasNext()) {
            CharData data = it.next();
            tot += data.count; 
        }
        it = probs.listIterator(0);
        while (it.hasNext()) {
            CharData data = it.next();
            data.p = data.count/tot;
        }
        double cpi = 0.0;
        it = probs.listIterator(0);
        while (it.hasNext()) {
            CharData data = it.next();
            data.cp = cpi + data.p;
            cpi = data.cp;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		double param = randomGenerator.nextDouble();
        ListIterator it = probs.listIterator(0);
        while (it.hasNext()) {
            CharData data = it.next();
            if (data.cp > param) {
                return data.chr;
            }
        }
		return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		String str = initialText.substring(0,windowLength);
            if (!CharDataMap.containsKey(initialText)) {
            return initialText;
        }
        if(initialText.length() < windowLength){
            return initialText;
        }        
        else{
        char c = getRandomChar(CharDataMap.get(str));
         str = str + c;
         int i = 1;
            while (str.length()<textLength+windowLength) {
              c =  getRandomChar(CharDataMap.get(str.substring(i , i+windowLength)));
              str = str + c;
              i++;
            }
        }
        return str;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

public static void main(String[] args) {
    int windowLength = Integer.parseInt(args[0]);
    String initialText = args[1];
    int generatedTextLength = Integer.parseInt(args[2]);
    Boolean randomGeneration = args[3].equals("random");
    String fileName = args[4];

    // יצירת המודל לפי הבחירה (אקראי או קבוע)
    LanguageModel lm;
    if (randomGeneration) {
        lm = new LanguageModel(windowLength);
    } else {
        lm = new LanguageModel(windowLength, 20); // Seed קבוע לבדיקות
    }

    // אימון ויצירה
    lm.train(fileName);
    System.out.println(lm.generate(initialText, generatedTextLength));
}
}