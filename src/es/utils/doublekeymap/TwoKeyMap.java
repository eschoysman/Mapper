package es.utils.doublekeymap;

import java.util.LinkedHashMap;

/**
 * This class rappresent a Map that has two keys. The keys are incapsulate inside a {@link PairKey} istance that is used as the key inside the Map. 
 * @author eschoysman
 *
 * @param <K1> type of the first key
 * @param <K2> type of the second key
 * @param <V> type of the value
 */
public class TwoKeyMap<K1,K2,V> extends LinkedHashMap<PairKey<K1,K2>,V> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Create an empty map.
	 */
	public TwoKeyMap() {
		super();
	}

	/**
	 * Add to the map a value associated to booth the keys
	 * @param key1 first key
	 * @param key2 second key
	 * @param value the value
	 * @return the previous value associated with {@code key1} and {@code key2}, or {@code null} if there was no mapping for key. (A {@code null} return can also indicate that the mappreviously associated null with {@code key1} and {@code key2}.)
	 */
	public V put(K1 key1, K2 key2, V value) {
		return super.put(new PairKey<K1,K2>(key1,key2),value);
	}
	/**
	 * 
	 * @param key1 first key
	 * @param key2 second key
	 * @return the value to which the specified {@code key1} and {@code key2} is mapped, or {@code null} if this map contains no mapping for the {@code key1} and {@code key2}.
	 */
	public V get(K1 key1, K2 key2) {
		return super.get(new PairKey<K1,K2>(key1,key2));
	}
	/**
	 * 
	 * @param key1 first key
	 * @param key2 second key
	 * @return {@code true} if this map contains a mapping for the specified keys.
	 */
	public boolean containsKey(K1 key1, K2 key2) {
		return super.containsKey(new PairKey<K1,K2>(key1,key2));
	}
	
}