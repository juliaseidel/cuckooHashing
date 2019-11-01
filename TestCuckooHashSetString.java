package tests;

import main.CuckooHashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCuckooHashSetString {
    private CuckooHashSet<String> cuckooHashSet;

    @BeforeEach
    public void setUp() {
        cuckooHashSet = new CuckooHashSet<>(11);
    }

    @Test
    @DisplayName("Hash Functions.")
    public void testHashFunctions() {
        String keys[] = {"AI", "AQ", "AT", "BK", "AA", "AL", "AF", "AC", "AN", "BH"};
        int hash0[] = {9, 6, 9, 9, 1, 1, 6, 3, 3, 6};
        int hash1[] = {2, 3, 3, 5, 2, 3, 2, 2, 3, 5};

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
        String keys[] = {"AI", "AQ", "AT", "BK", "AA", "AL"};
        int hash0[] = {9, 6, 9, 9, 1, 1};
        int hash1[] = {2, 3, 3, 5, 2, 3};

        int[] finalPositionTable = {0, 0, 1, 1, 1, 0};
        int[] finalPositionIndex = {9, 6, 3, 5, 2, 0};

        String[] nonInsertedKeys = {"AW", "AY", "AV", "AX", "BY"};

        // Test if first position is correct
        for (int i = 0; i < keys.length; i++) {
            cuckooHashSet.put(keys[i]);
            int[] returnedPosition = cuckooHashSet.findIndex(keys[i]);

            assertEquals(0, returnedPosition[0],
                    keys[i] + " is in the wrong table after put.");
            assertEquals(hash0[i], returnedPosition[1],
                    "Hash function value for " + keys[i] + "on table 0 incorrect after put.");
        }

        // Testing final positions in table
        for (int i = 0; i < keys.length - 1; i++) {
            int[] returnedPosition = cuckooHashSet.findIndex(keys[i]);
            assertEquals(finalPositionTable[i], returnedPosition[0],
                    "Final table of "+keys[i] + "incorrect.");
            assertEquals(finalPositionIndex[i], returnedPosition[1],
                    "Final position of "+keys[i] + "incorrect.");
        }

        // Test for keys that were not inserted.
        for (int i = 0; i < nonInsertedKeys.length; i++) {
            int[] returnedPosition = cuckooHashSet.findIndex(nonInsertedKeys[i]);
            assertEquals(null, returnedPosition,
                    "Key "+nonInsertedKeys[i]+" should not be in table.");
        }
    }

    @Test
    @DisplayName("Without Cycle.")
    public void testWithoutCycle () {
        String keys[] = {"AI", "AQ", "AT", "BK", "AA", "AL"};
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
        String keys[] = {"AI", "AQ", "AT", "BK", "AA", "AL", "AF"};
        for (int i = 0; i < keys.length - 1; i++)
            cuckooHashSet.put(keys[i]);

        assertThrows(IllegalStateException.class,
                () -> cuckooHashSet.put(keys[keys.length-1]),
                "Cycle not detected or exception not thrown.");
    }

    @Test
    @DisplayName("Add and remove.")
    public void testAddRemove() {
        String keys[] = {"AI", "AQ", "AT", "BK", "AA", "AL"};
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
