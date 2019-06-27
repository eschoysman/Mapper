package es.utils.doublekeymap;

import java.util.Objects;

/**
 * This class is a wrapper for the {@link TwoKeyMap} key object.
 * @author eschoysman
 *
 * @param <K1> type of the first part of the key
 * @param <K2> type of the second part of the key
 */
public class PairKey<K1,K2> {
	
	private K1 key1;
	private K2 key2;
	
	/**
	 * 
	 * @param key1 first key
	 * @param key2 second key
	 */
	public PairKey(K1 key1, K2 key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key1,key2);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairKey<?,?> other = (PairKey<?,?>)obj;
		return Objects.equals(key1,other.key1)
			&& Objects.equals(key2,other.key2);
	}
	
	/**
	 * return a human readable string of the paired key
	 */
	@Override
	public String toString() {
		return "<"+key1+","+key2+">";
	}
	
}
