package es.utils.mapper.impl;

import java.util.Objects;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;

/**
 * This class handle the abstract logic for mapping an object of type {@code T} into a {@code U} object 
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <U> the type of the destination object
 * 
 * @see ClassMapper
 * @see DirectMapper
 * @see EnumMapper
 */
public abstract class MapperObject<T,U> {

	protected Mapper mapper;
	protected Class<T> from;
	protected Class<U> to;
	
	/**
	 * @param from the type of the source object
	 * @param to the type of the destination object
	 * @throws NullPointerException if {@code from} or {@code to} is null
	 */
	protected MapperObject(Class<T> from, Class<U> to) {
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}
	
	/**
	 * @return the origin class of this mapping
	 */
	public Class<T> fromClass() {
		return from;
	}
	/**
	 * @return the destination class of this mapping
	 */
	public Class<U> toClass() {
		return to;
	}

	/**
	 * The activation of a {@code MapperObject} takes into account all the present mappings to create the default mappings of the current mapping 
	 * @param mapper the {@code Mapper} istance used to create all the default mappings
	 */
	public void activate(Mapper mapper) {
		this.mapper = mapper;
	}
	protected abstract U mapValue(T from) throws MappingException;
	protected abstract U mapValue(T from, U to);
	
	/**
	 * @param from the input object to be mapped
	 * @return an instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 * @throws MappingException if an error occurs during the mapping
	 */
    public U map(T from) throws MappingException {
    	if(from==null)
            return null;
    	return mapValue(from);
    }
	/**
	 * @param from the input object to be mapped
	 * @param to an default object that will be override by the mapping 
	 * @return an instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 */
    public U map(T from, U to) {
    	if(from==null)
            return to;
    	return mapValue(from,to);
    }

	/**
	 * @param from the input object to be mapped
	 * @return an instance of the destination type of this mapping. If the input is {@code null} or some eception occurs during the mapping, returns {@code null}.
	 */
	public U mapOrNull(T from) {
		try {
			return map(from);
		} catch (MappingException e) {
			System.err.println("Error, returning null value.");
			return null;
		}
	}
    
}
