package hw3.hash;

import java.util.HashSet;
import java.util.Set;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        double scale = 1;
        int N = 50;
        int M = 10;

        HashTableDrawingUtility.setScale(scale);
        Set<Oomage> oomies = new HashSet<Oomage>();
        for (int i = 0; i < N; i += 1) {
            oomies.add(SimpleOomage.randomSimpleOomage());
        }
        visualize(oomies, M, scale);
    }

    public static void visualize(Set<Oomage> set, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);

        /* Create a visualization of the given hash table. Use
           du.xCoord and du.yCoord to figure out where to draw
           Oomages.
         */

        int[] buckets = new int[M];
        for (Oomage oo : set) {
            int yBucket = (oo.hashCode() & 0x7FFFFFFF) % M;
            double y = HashTableDrawingUtility.yCoord(yBucket, M);
            double x = HashTableDrawingUtility.xCoord(buckets[yBucket]);
            buckets[yBucket] += 1;
            oo.draw(x, y, scale);
        }

        /* When done with visualizer, be sure to try 
           scale = 0.5, N = 2000, M = 100. */           
    }
} 
