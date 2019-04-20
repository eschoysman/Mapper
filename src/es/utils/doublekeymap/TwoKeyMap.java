package es.utils.doublekeymap;

import java.util.LinkedHashMap;

public class TwoKeyMap<K1,K2,V> extends LinkedHashMap<PairKey<K1,K2>,V> {
	
	private static final long serialVersionUID = 1L;

	public V put(K1 key1, K2 key2, V value) {
		return super.put(new PairKey<K1,K2>(key1,key2),value);
	}
	public V get(K1 key1, K2 key2) {
		return super.get(new PairKey<K1,K2>(key1,key2));
	}
	public boolean containsKey(K1 key1, K2 key2) {
		return super.containsKey(new PairKey<K1,K2>(key1,key2));
	}
	
}