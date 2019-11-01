package main;

/**
 * A Set interface.
 * @author Christine Zarges
 * @version 1.0, 10 October 2019
 *
 * @param <K> the type of keys in this set
 */
public interface Set<K> {
    /**
     * Perform a membership test for a key.
     *
     * @param key the key to look up
     * @return true if the key is in the set, false otherwise
     */
    boolean contains(K key);

    /**
     * Add a key to the set. If the key already exists, do nothing.
     *
     * @param key the key to insert
     * @return false if the key already existed, true otherwise
     */
    boolean put(K key);

    /**
     * Remove key from the set.
     *
     * @param key the key to be removed
     * @return true if the key was removed, false if it does not exist
     */
    boolean remove(K key);
}
