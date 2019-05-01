package es.utils.doublekeymap;

import java.util.LinkedHashMap;

/**
 * 
 * @author Emmanuel
 *
 * @param <K1>
 * @param <K2>
 * @param <V>
 */
public class TwoKeyMap<K1,K2,V> extends LinkedHashMap<PairKey<K1,K2>,V> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TwoKeyMap() {
		super();
	}

	/**
	 * 
	 * @param key1
	 * @param key2
	 * @param value
	 * @return
	 */
	public V put(K1 key1, K2 key2, V value) {
		return super.put(new PairKey<K1,K2>(key1,key2),value);
	}
	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	public V get(K1 key1, K2 key2) {
		return super.get(new PairKey<K1,K2>(key1,key2));
	}
	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	public boolean containsKey(K1 key1, K2 key2) {
		return super.containsKey(new PairKey<K1,K2>(key1,key2));
	}
	
}