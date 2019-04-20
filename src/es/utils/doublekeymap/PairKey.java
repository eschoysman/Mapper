package es.utils.doublekeymap;

import java.util.Objects;


public class PairKey<K1,K2> {
	
	private K1 key1;
	private K2 key2;
	
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
	
	@Override
	public String toString() {
		return "<"+key1+","+key2+">";
	}
	
}
