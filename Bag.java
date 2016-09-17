/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblet;

/**
 *
 * @author Shadab Khan Zed
 */
import java.util.Random;
class Bag {
    private final Random rand;
    private final int letter_counts[] = {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    private final int letter_points[] = {2, 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
    private final Letter letters [] = new Letter[100];
    private int n = 0;
    Bag(int seed) {
        rand  = new Random(seed);
        for (int i = 0; i < letter_counts.length; i++) {
            for (int j = 0; j < letter_counts[i]; j++) {
                Letter l = new Letter(i == 0 ? '*' : (char) ('A' + i - 1), letter_points[i]);
                putBack(l);
            }
        }
    }
    synchronized Letter takeout() {
        if(n == 0)
            return null;
        int i = (int)(rand.nextDouble() * n);
        Letter l = letters[i];
        if(i != n - 1)
            System.arraycopy(letters, i + 1, letters, i, n - i - 1);
        n--;
        return l;
    }
    final synchronized void putBack(Letter l){
        letters[n++] = l;
    }

}
