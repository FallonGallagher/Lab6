import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final static int DEFAULT_INITIAL_CAPACITY =4;
    private final static int MAXIMUM_CAPACITY = 1 << 30;
    private int capacity;
    private final static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
    private float loadFactorThreshold;
    private int size =0;
    LinkedList<MyMap.Entry<K,V>>[] table;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }
    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }
    public MyHashMap(int initialCapacity, float loadFactorThreshold) {
        if (initialCapacity > MAXIMUM_CAPACITY) {
            this.capacity = MAXIMUM_CAPACITY;
        } else { 
            this.capacity = trimToPowerOf2(initialCapacity);
        }

        this.loadFactorThreshold = loadFactorThreshold;
        table = new LinkedList[capacity];
    }

        public void clear() {
            size = 0;
            removeEntries();
        }

        public boolean containsKey(K key) {
            return get(key) != null;
        }

        public boolean containsValue(V value) {
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    for (Entry<K, V> entry : table[i]) {
                        if (entry.getValue().equals(value)) return true;
                    }
                }
            }
            return false;
        }

        public java.util.Set<MyMap.Entry<K, V>> entrySet() {
            java.util.Set<MyMap.Entry<K, V>> set = new java.util.HashSet<>();
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    for (Entry<K, V> entry : table[i]) {
                        set.add(entry);
                    }
                }
            }
            return set;
        }

        public V get(K key) {
            int bucketIndex = hash(key.hashCode());
            if (table[bucketIndex] != null) {
                for (Entry<K, V> entry : table[bucketIndex]) {
                    if (entry.getKey().equals(key)) return entry.getValue();
                }
            }
            return null;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public java.util.Set<K> keySet() {
            java.util.Set<K> set = new java.util.HashSet<>();
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    for (Entry<K, V> entry : table[i]) {
                        set.add(entry.getKey());
                    }
                }
            }
            return set;
        }

        public V put(K key, V value) {
            if (get(key) != null) {
                int bucketIndex = hash(key.hashCode());
                LinkedList<Entry<K, V>> bucket = table[bucketIndex];
                for (Entry<K, V> entry : bucket) {
                    if (entry.getKey().equals(key)) {
                        V oldValue = entry.getValue();
                        entry.value = value;
                        return oldValue;
                    }
                }
            }
            if (size >= capacity * loadFactorThreshold) {
                if (capacity == MAXIMUM_CAPACITY) {
                    throw new RuntimeException("Maximum capacity exceeded");
                }
                rehash();
            }
            int bucketIndex = hash(key.hashCode());
            if (table[bucketIndex] == null) { 
                table [bucketIndex] = new LinkedList<>();
            }
            

            table[bucketIndex].add(new MyMap.Entry<K, V>(key, value));
            size ++;
            return value;
        }
        public void remove(K key) {
            int bucketIndex = hash(key.hashCode());
            if (table[bucketIndex] != null) {
                LinkedList<Entry<K, V>> bucket = table[bucketIndex];
                for (Entry<K, V> entry : bucket) {
                    if (entry.getKey().equals(key)) {
                        bucket.remove(entry);
                        size--;
                        break;
                    }
                }
            }
        }
        public int size() {
            return size;
        }
        public java.util.Set<V> values() {
            java.util.Set<V> set = new java.util.HashSet<>();
            for (int i = 0; i < capacity; i++){
                if (table[i] != null) {
                    for (Entry<K, V> entry : table[i]) {
                        set.add(entry.getValue());
                    }
                }
            }
            return set;
        }
        
        private int hash(int hashCode) {
            return supplementalHash(hashCode) & (capacity - 1);
        }
        private static int supplementalHash(int h) {
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }

        private int trimToPowerOf2(int initialCapacity) {
            int capacity = 1;
            while (capacity < initialCapacity)
            capacity <<= 1;
            return capacity; 
        }
        
        private void removeEntries() {
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    table[i].clear();
                }
            }
        }

        private void rehash() {
            java.util.Set<Entry<K, V>> set = entrySet();
            capacity <<= 1;
            table = new LinkedList[capacity];
            size = 0;
            for (Entry<K, V> entry : set) {
                put (entry.getKey(), entry.getValue());
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("[");
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null && table[i].size() > 0) {
                    for (Entry<K, V> entry : table[i]) {
                        builder.append(entry);
                    }
                }
            }
            builder.append("]");
            return builder.toString();
        }
    }
        
