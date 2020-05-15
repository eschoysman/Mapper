package es.utils.mapper.impl;

import es.utils.mapper.Mapper;
import es.utils.mapper.defaultvalue.DefaultValueStrategy;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

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
	 * Create a mapping between the types {@code from} and {@code to}
	 * @param from The type of the source object
	 * @param to The type of the destination object
	 * @throws NullPointerException If {@code from} or {@code to} is null
	 */
	protected MapperObject(Class<T> from, Class<U> to) {
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}

	/**
	 * Set the belonging mapper of this instance
	 * @param mapper the mapper instance to set
	 */
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	/**
	 * Returns the origin class of this mapping
	 * @return The origin class of this mapping
	 */
	public Class<T> fromClass() {
		return from;
	}
	/**
	 * Returns the destination class of this mapping
	 * @return The destination class of this mapping
	 */
	public Class<U> toClass() {
		return to;
	}

	/**
	 * The activation of a {@code MapperObject} takes into account all the present mappings to create the default mappings of the current mapping 
	 */
	public abstract void activate();
	/**
	 * Maps the input according to the class logic 
	 * @param from the object to map
	 * @return the mapped object
	 * @throws MappingException if an error occurs during the mapping 
	 * @see #map(Object)
	 */
	protected abstract U mapValue(T from) throws MappingException;
	/**
	 * Maps the input according to the class logic into the {@code to} instance
	 * @param from the object to map
	 * @param to the object instance to return after beeing update with the mapped field from {@code from}
	 * @return the updated object {@code to}
	 * @see #map(Object,Object)
	 */
	protected abstract U mapValue(T from, U to);
	
	/**
	 * Returns an instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 * @param from The input object to be mapped
	 * @return An instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 * @throws MappingException If an error occurs during the mapping
	 */
    public U map(T from) throws MappingException {
    	if(from==null)
            return null;
   		from = setDefaultValuesInput(from);
    	U result = mapValue(from);
		result = setDefaultValuesOutput(result);
		return result;
    }
	/**
	 * Returns an instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 * @param from The input object to be mapped
	 * @param to A default object that will be override by the mapping 
	 * @return An instance of the destination type of this mapping. If the input is {@code null} return {@code null}.
	 */
    public U map(T from, U to) {
    	if(from==null)
            return to;
    	U result = mapValue(from,to);
		return result;
    }
    
	/**
	 * Returns an instance of the destination type of this mapping or {@code null if any exception is thrown}
	 * @param from The input object to be mapped
	 * @return An instance of the destination type of this mapping. If the input is {@code null} or some exception occurs during the mapping, returns {@code null}.
	 */
	public U mapOrNull(T from) {
		try {
			return map(from);
		} catch (MappingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a human readable string of this {@code MapperObject}
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[<"+fromClass()+","+toClass()+">]";
	}

	/**
	 * Fills the fields of {@code obj} with the default values
	 * @param obj the object to fill with default values
	 * @return the {@code obj} instance updated
	 */
	protected T setDefaultValuesInput(T obj) {
    	if(mapper==null || !(mapper.config().hasStrategy(DefaultValueStrategy.ALWAYS) && mapper.config().hasStrategy(DefaultValueStrategy.INPUT))) {
    		return obj;
    	}
    	return applyDefault(obj);
    }
	/**
	 * Fills the fields of {@code obj} with the default values
	 * @param obj the object to fill with default values
	 * @return the {@code obj} instance updated
	 */
	protected U setDefaultValuesOutput(U obj) {
    	if(obj==null) {
    		return null;
    	}
    	if(mapper==null || !(mapper.config().hasStrategy(DefaultValueStrategy.ALWAYS) && mapper.config().hasStrategy(DefaultValueStrategy.OUTPUT))) {
    		return obj;
    	}
    	return applyDefault(obj);
    }
	private <OBJ> OBJ applyDefault(OBJ obj) {
		Map<String,FieldHolder> fieldsHolder = mapper.getFieldsHolderFromCache(obj.getClass());
    	for(FieldHolder fh : fieldsHolder.values()) {
    		Field field = fh.getField();
    		field.setAccessible(true);
			try {
				if(field.get(obj)==null) {
					field.set(obj,fh.getDefautValueSupplier().get());
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
    	}
    	return obj;
	}
	
}
