import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.TreeMap;

public class WordNet {
    private final Digraph digraph;
    private final TreeMap<String, Bag<Integer>> synsetIdByNoun;
    private final String[] synsetById;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException("synsets == null");
        }

        if (hypernyms == null) {
            throw new IllegalArgumentException("hypernyms == null");
        }

        synsetById = readSynsets(synsets);
        synsetIdByNoun = splitNouns(synsetById);

        digraph = new Digraph(synsetById.length);
        readHypernyms(hypernyms, digraph);

        if (!doesOnlyOneRootExist()) {
            throw new IllegalArgumentException("Root element does not exist.");
        }

        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph has a cycle.");
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new ArrayList<>(synsetIdByNoun.keySet());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word == null");
        }

        return synsetIdByNoun.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA)) {
            throw new IllegalArgumentException("nounA is not a WordNet noun.");
        }

        if (!isNoun(nounB)) {
            throw new IllegalArgumentException("nounB in not a WordNet noun.");
        }

        return sap.length(synsetIdByNoun.get(nounA), synsetIdByNoun.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA)) {
            throw new IllegalArgumentException("nounA is not a WordNet noun.");
        }

        if (!isNoun(nounB)) {
            throw new IllegalArgumentException("nounB in not a WordNet noun.");
        }

        int ancestor = sap.ancestor(synsetIdByNoun.get(nounA), synsetIdByNoun.get(nounB));
        return synsetById[ancestor];
    }

    private static String[] readSynsets(String fileName) {
        In in = new In(fileName);
        String[] allLines = in.readAllLines();

        String[] result = new String[allLines.length];
        for (String line : allLines) {
            String[] lineValues = line.split(",");
            result[Integer.parseInt(lineValues[0])] = lineValues[1];
        }

        return result;
    }

    private static TreeMap<String, Bag<Integer>> splitNouns(String[] synsetById) {
        TreeMap<String, Bag<Integer>> result = new TreeMap<>();
        for (int i = 0; i < synsetById.length; ++i) {
            String[] values = synsetById[i].split(" ");
            for (String value : values) {
                if (!result.containsKey(value)) {
                    result.put(value, new Bag<>());
                }
                result.get(value).add(i);
            }
        }

        return result;
    }

    private static void readHypernyms(String filename, Digraph digraph) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] values = in.readLine().split(",");
            int v = Integer.parseInt(values[0]);
            for (int i = 1; i < values.length; ++i) {
                int w = Integer.parseInt(values[i]);
                digraph.addEdge(v, w);
            }
        }
    }

    private boolean doesOnlyOneRootExist() {
        int rootCount = 0;
        for (int v = 0; v < digraph.V(); ++v) {
            if (digraph.outdegree(v) == 0) {
                rootCount++;
            }
        }

        return rootCount == 1;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}