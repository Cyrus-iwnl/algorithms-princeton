import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
	public static void main(String[] args) {
		String ans = null;
		int i = 0;
		while (!StdIn.isEmpty()) {
			String s = StdIn.readString();
			if (StdRandom.bernoulli(1.0 / ++i)) {
				ans = s;
			}
		}
		System.out.println(ans);
	}
}
