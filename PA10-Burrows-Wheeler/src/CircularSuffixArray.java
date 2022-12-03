import java.util.Arrays;

public class CircularSuffixArray {

    private final Integer[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        int len = s.length();
        index = new Integer[len];
        for (int i = 0; i < len; i++) index[i] = i;
        Arrays.sort(index, (a, b) -> {
            for (int i = 0; i < len; i++) {
                char c1 = s.charAt((i + a) % len);
                char c2 = s.charAt((i + b) % len);
                if (c1 > c2) return 1;
                if (c1 < c2) return -1;
            }
            return 0;
        });
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        System.out.println("length: " + csa.length());
        int[] ans = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            ans[i] = csa.index(i);
        }
        System.out.println("index: " + Arrays.toString(ans));
    }

}