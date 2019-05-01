package es.utils.mapper.impl;

import java.util.Objects;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;

/**
 * 
 * @author Emmanuel
 *
 * @param <T>
 * @param <U>
 */
public abstract class MapperObject<T,U> {

	protected Class<T> from;
	protected Class<U> to;
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @throws NullPointerException - if {@code from} or {@code to} is null
	 */
	protected MapperObject(Class<T> from, Class<U> to) {
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}
	
	/**
	 * 
	 * @return
	 */
	public Class<T> fromClass() {
		return from;
	}
	/**
	 * 
	 * @return
	 */
	public Class<U> toClass() {
		return to;
	}
	
	public abstract void activate(Mapper mapper);
	protected abstract U mapValue(T from) throws MappingException;
	protected abstract U mapValue(T from, U to);
	
    public U map(T from) throws MappingException {
    	if(from==null)
            return null;
    	return mapValue(from);
    }
    public U map(T from, U to) {
    	if(from==null)
            return to;
    	return mapValue(from,to);
    }

	public U mapOrNull(T from) {
		try {
			return map(from);
		} catch (MappingException e) {
			System.err.println("Error, returning null value.");
			return null;
		}
	}
    
}
