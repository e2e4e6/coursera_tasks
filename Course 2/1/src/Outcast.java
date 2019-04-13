import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("wordnet == null");
        }

        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("nouns == null");
        }

        if (nouns.length == 0) {
            throw new IllegalArgumentException("nouns is empty");
        }

        int max = 0;
        String result = nouns[0];
        for (String baseNoun : nouns) {

            int sum = 0;
            for (String noun : nouns) {
                sum = sum + wordnet.distance(baseNoun, noun);
            }

            if (sum > max) {
                result = baseNoun;
                max = sum;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
