import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding,
    // reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> seq = new LinkedList<>();
        for (int i = 0; i < R; i++) seq.add((char) i);
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = seq.indexOf(c);
            BinaryStdOut.write(index, 8);
            seq.remove(index);
            seq.add(0, c);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding,
    // reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> seq = new LinkedList<>();
        for (int i = 0; i < R; i++) seq.add((char) i);
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = seq.get(index);
            BinaryStdOut.write(c);
            seq.remove(index);
            seq.add(0, c);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}