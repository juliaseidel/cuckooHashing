package tests;

import main.CuckooHashSet;
import main.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCuckooHashSetInteger {
    private CuckooHashSet<Integer> cuckooHashSet;

    @BeforeEach
    public void setUp() {
        cuckooHashSet = new CuckooHashSet<>(11);
    }

    @Test
    @DisplayName("Hash Functions.")
    public void testHashFunctions() {
        int keys[] = {20, 50, 53, 75, 100, 67, 105, 3, 36, 39};
        int hash0[] = {9, 6, 9, 9, 1, 1, 6, 3, 3, 6};
        int hash1[] = {1, 4, 4, 6, 9, 6, 9, 0, 3, 3};

        for (int i = 0; i < keys.length; i++) {
            assertEquals(hash0[i], cuckooHashSet.hashFunction(0, keys[i]),
                    "Hash function value for " + keys[i] + "on table 0 incorrect.");
            assertEquals(hash1[i], cuckooHashSet.hashFunction(1, keys[i]),
                    "Hash function value for " + keys[i] + "on table 1 incorrect.");
        }
    }

    @Test
    @DisplayName("Find Index.")
    public void testFindIndex() {
        int[] keys = {20, 50, 53, 75, 100, 67, 105, 3, 36, 39};
        int[] hash0 = {9, 6, 9, 9, 1, 1, 6, 3, 3, 6};
        int[] hash1 = {1, 4, 4, 6, 9, 6, 9, 0, 3, 3};

        int[] finalPositionTable = {1, 0, 1, 0, 0, 1, 1, 1, 0, 1};
        int[] finalPositionIndex = {1, 6, 4, 9, 1, 6, 9, 0, 3, 3};

        int[] nonInsertedKeys = {2, 9, 11, 12, 17};

        // Test if first position is correct
        for (int i = 0; i < keys.length - 1; i++) {
            cuckooHashSet.put(keys[i]);
            int[] returnedPosition = cuckooHashSet.findIndex(keys[i]);

            assertEquals(0, returnedPosition[0],
                    keys[i] + "is in the wrong table after put.");
            assertEquals(hash0[i], returnedPosition[1],
                    "Hash function value for " + keys[i] + "on table 0 incorrect after put.");
        }
        cuckooHashSet.put(keys[keys.length - 1]);
        int[] returnedPosition = cuckooHashSet.findIndex(keys[keys.length - 1]);

        assertEquals(1, returnedPosition[0],
                keys[keys.length - 1] + "is in the wrong table after put.");
        assertEquals(hash1[keys.length - 1], returnedPosition[1],
                "Hash function value for " + keys[keys.length - 1] + "on table 0 incorrect after put.");

        // Testing final positions in table
        for (int i = 0; i < keys.length - 1; i++) {
            returnedPosition = cuckooHashSet.findIndex(keys[i]);
            assertEquals(finalPositionTable[i], returnedPosition[0],
                    "Final table of "+keys[i] + "incorrect.");
            assertEquals(finalPositionIndex[i], returnedPosition[1],
                    "Final position of "+keys[i] + "incorrect.");
        }

        // Test for keys that were not inserted.
        for (int i = 0; i < nonInsertedKeys.length; i++) {
            returnedPosition = cuckooHashSet.findIndex(nonInsertedKeys[i]);
            assertEquals(null, returnedPosition,
                    "Key "+nonInsertedKeys[i]+" should not be in table.");
        }
    }

    @Test
    @DisplayName("Without Cycle.")
    public void testWithoutCycle () {
        int keys[] = {20, 50, 53, 75, 100, 67, 105, 3, 36, 39};
        for (int i = 0; i < keys.length; i++) {
            cuckooHashSet.put(keys[i]);
            assertEquals(true, cuckooHashSet.contains(keys[i]),
                    "Key "+keys[i]+" not found, but should be in table.");
            assertEquals(false, cuckooHashSet.contains(keys[i]+1),
                    "Key "+(keys[i]+1)+" found, but should not be in table.");
        }
    }

    @Test
    @DisplayName("With Cycle.")
    public void testWithCycle () {
        int keys[] = {20, 50, 53, 75, 100, 67, 105, 3, 36, 39, 6};
        for (int i = 0; i < keys.length - 1; i++)
            cuckooHashSet.put(keys[i]);

        assertThrows(IllegalStateException.class,
                () -> cuckooHashSet.put(keys[keys.length-1]),
                "Cycle not detected or exception not thrown.");
    }

    @Test
    @DisplayName("Add and remove.")
    public void testAddRemove() {
        int keys[] = {20, 50, 53, 75, 100, 67, 105, 3, 36, 39};
        for (int i = 0; i < keys.length; i++) {
            assertEquals(true, cuckooHashSet.put(keys[i]),
                    "Key not added.");
            assertEquals(false, cuckooHashSet.put(keys[i]),
                    "Key added, but already present.");


            assertEquals(true, cuckooHashSet.contains(keys[i]),
                    "Key "+keys[i]+"not found, but should be in table.");
            assertEquals(false, cuckooHashSet.contains(keys[i]+1),
                    "Key "+(keys[i]+1)+"found, but should not be in table.");
        }

        for (int i = 0; i < keys.length; i++) {
            assertEquals(true, cuckooHashSet.remove(keys[i]),
                    "Key not removed, but present.");
            assertEquals(false, cuckooHashSet.remove(keys[i]),
                    "Key removed, but not present.");

            assertEquals(false, cuckooHashSet.contains(keys[i]),
                    "Key "+(keys[i])+"found, but should not be in table.");
        }
    }
}
