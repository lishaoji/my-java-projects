package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;

public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /* meaning no two SimpleOomages should EVER have the same
           hashCode!
         */
        int[] tab = new int[256 * 256 * 256];

        for (int r = 0; r < 256; r += 1) {
            for (int g = 0; g < 256; g += 1) {
                for (int b = 0; b < 256; b += 1) {
                    SimpleOomage so = new SimpleOomage(r, g, b);
                    int hashCode = so.hashCode();
                    int bucket = hashCode % tab.length;
                    if (bucket < 0) {
                        bucket += tab.length;
                    }
                    tab[bucket] += 1;
                }
            }
        }

        for (int i = 0; i < 256 * 256 * 256; i += 1) {
            assertFalse(tab[i] > 1);
        }
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<SimpleOomage>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
