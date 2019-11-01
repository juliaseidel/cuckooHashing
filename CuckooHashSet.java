package main;

public class CuckooHashSet<K> implements Set<K> {

    private K[][] data;
    private int capacity;
    private int size;

    public CuckooHashSet(int capacity){
        data = (K[][]) new Object[2][capacity];
        size=0;
        this.capacity=capacity;
    }

    public int hashFunction(int functionNumber, K key){
        if (functionNumber==0){
            return key.hashCode()%capacity;
        }
        else if (functionNumber==1){
            return (key.hashCode()/capacity)%capacity;
        }
        throw new IllegalArgumentException("function number is 0 or 1");
    }

    public int[] findIndex(K key){
        int hashZero = hashFunction(0, key);
        int hashOne = hashFunction(1, key);
        if (data[0][hashZero]==key){
            return new int[]{0, hashZero};
        }
        else if (data[1][hashOne]==key){
            return new int[]{1, hashOne};
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        int[] position = findIndex(key);
        return position != null;
    }

    @Override
    public boolean put(K key) {
        if (contains(key)){
            return false;
        }
        else {
            int currentTable = 0;
            K currentKey = key;
            int numberOfEvictions = 0;
            boolean successfullyPlaced = false;
            while (!successfullyPlaced){
                int index = hashFunction(currentTable, currentKey);
                if (data[currentTable][index]==null){
                    data[currentTable][index]=currentKey;
                    successfullyPlaced = true;
                }
                else {
                    K anotherKey = data[currentTable][index];
                    data[currentTable][index] = currentKey;
                    currentKey = anotherKey;
                    currentTable = 1 - currentTable;
                    numberOfEvictions++;
                    if (numberOfEvictions >= capacity) {
                        throw new IllegalStateException("Eviction cycle has been found");
                    }
                }
            }
            return true;
        }
    }

    @Override
    public boolean remove(K key) {
        int[] position = findIndex(key);
        if (position==null){
            return false;
        }
        else {
            data[position[0]][position[1]]=null;
            return true;
        }
    }
}
