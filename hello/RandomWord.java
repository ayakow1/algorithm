/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String value = "";
        int i = 0;
        while (!StdIn.isEmpty()) {
            i++;
            String valueNext = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / i)) {
                value = valueNext;
            }
        }
        System.out.println(value);
    }
}
