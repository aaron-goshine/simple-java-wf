import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.*;


public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    //I convert the delimiters to a literal Regex, seems like the right thing TODO --
    String delimiters = "[ \t,;\\.\\?!\\-:@\\[\\]\\(\\)\\{\\}_\\*/&]";
    String[] stopWordsArray = {"","i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        FileReader fileReader = new FileReader(this.inputFileName);
        BufferedReader fileBuffer = new BufferedReader(fileReader);
        Boolean shouldProcess = true;
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        while (shouldProcess) {
            String line = fileBuffer.readLine();
            if (line == null) {
                shouldProcess = false;
                break;
            } else {

                line = line.toLowerCase();
                String[] splits = line.split(delimiters);
                for (int i = 0; i < splits.length; i++) {
                    String word = splits[i].replaceAll("[\\s 0-9\"\\ ]","");
                    if (!Arrays.asList(stopWordsArray).contains(word) && word != "" && word.length() > 1) {
                        Integer value = dictionary.get(word);
                        if (value != null) {
                            dictionary.put(word, value + 1);
                        }else{
                            dictionary.put(word, 1);
                        }
                    }
                }
            }
        }
        Map<String, Integer> sortedMap = sortByComparator(dictionary);

        int guard = 0;

        for (Map.Entry<String, Integer> entry : sortedMap.entrySet())
        {
            if (guard >= ret.length) break;
            ret[guard] = entry.getKey();
            guard++;
        }
        return ret;
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> index01, Map.Entry<String, Integer> index02) {
                return index02.getValue() - index01.getValue();
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            System.out.println(userName);
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }
}
