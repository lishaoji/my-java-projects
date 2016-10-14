package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;


public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    public boolean haveNiceHashCodeSpread(Set<ComplexOomage> oomages) {
        /* Write a utility function that ensures that the oomages have
         * hashCodes that would distribute them fairly evenly across
         * buckets To do this, mod each's hashCode by M = 10,
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] hashTable = new int[10];
        int N = oomages.size();

        for (ComplexOomage co : oomages) {
            int hashCode = co.hashCode();
            int bucket = (hashCode & 0x7FFFFFFF) % 10;
            hashTable[bucket] += 1;
        }

        for (int i = 0; i < 10; i += 1) {
            if (hashTable[i] > N / 2.5 || hashTable[i] < N / 50) {
                return false;
            }
        }
        return true;
    }


    @Test
    public void testRandomItemsHashCodeSpread() {
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    @Test
    public void testWithDeadlyParams() {
        /* Create a Set that shows the flaw in the hashCode function.
         */
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();
        int N = 1000;
        ArrayList<Integer> hashes = new ArrayList<>();

        for (int i = 0; i < N; i += 1) {
            hashes.add(1);
            ComplexOomage co = new ComplexOomage(hashes);
            oomages.add(co);
        }

        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
