import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {
    private final Map<Integer, String> sets; // [ index, synset.toString() ]
    private final Map<String, List<Integer>> nouns; // [ word, listOf(index) ]
    private Digraph net;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        sets = new HashMap<>();
        nouns = new HashMap<>();
        int v = initSynsets(synsets);
        initNet(hypernyms, v);
        if (!validate()) throw new IllegalArgumentException();
        sap = new SAP(net);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA)) throw new IllegalArgumentException(nounA + " is NOT in the net");
        if (!isNoun(nounB)) throw new IllegalArgumentException(nounB + " is NOT in the net");
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA)) throw new IllegalArgumentException(nounA + " is NOT in the net");
        if (!isNoun(nounB)) throw new IllegalArgumentException(nounB + " is NOT in the net");
        int index = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return sets.get(index);
    }

    private int initSynsets(String synsets) {
        In in = new In(synsets);
        int v = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            sets.put(id, line[1]);
            String[] words = line[1].split(" ");
            for (String word : words) {
                List<Integer> list;
                if (nouns.containsKey(word)) list = nouns.get(word);
                else list = new ArrayList<>();
                list.add(id);
                nouns.put(word, list);
            }
            v++;
        }
        return v;
    }

    private void initNet(String hypernyms, int v) {
        In in = new In(hypernyms);
        net = new Digraph(v);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int from = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int to = Integer.parseInt(line[i]);
                net.addEdge(from, to);
            }
        }
    }

    private boolean validate() {
        DirectedCycle directedCycle = new DirectedCycle(net);
        if (directedCycle.hasCycle()) return false;
        int roots = 0;
        for (int v = 0; v < net.V(); v++) {
            if (net.outdegree(v) == 0) roots++;
            if (roots > 1) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        String a = "fire", b = "flame";
        System.out.println(wordNet.sap(a, b));
        System.out.println(wordNet.distance(a, b));
    }
}