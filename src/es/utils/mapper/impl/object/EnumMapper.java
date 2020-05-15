package es.utils.mapper.impl.object;

import es.utils.mapper.impl.MapperObject;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * This class customize the abstract class {@code MapperObject} creating a {@code MapperObject} that convert on enum type {@code T} into a enum {@code U}.
 * @author eschoysman
 *
 * @param <T> the enum type of the origin object
 * @param <U> the enum type of the destination object
 * 
 * @see MapperObject
 * @see ClassMapper
 * @see DirectMapper
 */
public class EnumMapper<T extends Enum<T>,U extends Enum<U>> extends MapperObject<T, U> {

	private EnumMap<T,U> mapping;
	private EnumSet<T> inputValuesToIgnore;
	private U defaultDestinationEnumValue;
	
	/**
	 * Create a {@code MapperObject} between the enum types {@code T} and {@code U}.
	 * @param fromEnum the enum type of the origin object
	 * @param toEnum the enum type of the destination object
	 */
	public EnumMapper(Class<T> fromEnum, Class<U> toEnum) {
		super(fromEnum,toEnum);
		this.mapping = new EnumMap<T,U>(from);
		this.inputValuesToIgnore = EnumSet.noneOf(from);
		this.defaultDestinationEnumValue = null;
	}

	public void activate() {
		for(T from : from.getEnumConstants()) {
			try {
				add(from,Enum.valueOf(to,from.name()));
			} catch(IllegalArgumentException e) {
			}
		}
	}
	
	/**
	 * Add a custom mapping between the enum values {@code T} and {@code U}.
	 * @param from origin enum value
	 * @param to destination enum value
     * @return the current instance
	 */
    public EnumMapper<T,U> add(T from, U to) {
        mapping.put(from,to);
        return this;
    }
    /**
     * Allow to avoid some input enum values to be mapped
     * @param inputValueToIgnore enum value to be ignore 
     * @param otherInputValuesToIgnore other enum value to be ignore
     * @return the current instance
     */
	@SuppressWarnings("unchecked")
	public EnumMapper<T,U> ignore(T inputValueToIgnore, T... otherInputValuesToIgnore) {
		this.inputValuesToIgnore.addAll(EnumSet.of(inputValueToIgnore, otherInputValuesToIgnore));
    	return this;
    }
	/**
	 * Allow to specify a default enum value for mapping without a counterpart in the destination enum or if some exception is thrown.
	 * @param defaultDestinationEnumValue the enum instance the be return
     * @return the current instance
	 */
    public EnumMapper<T,U> setDefaultDestinationEnumValue(U defaultDestinationEnumValue) {
    	this.defaultDestinationEnumValue = defaultDestinationEnumValue;
    	return this;
    }

    protected U mapValue(T from) {
    	return mapValue(from,defaultDestinationEnumValue);
    }
    protected U mapValue(T from, U to) {
		return inputValuesToIgnore.contains(from) ? to : this.mapping.getOrDefault(from,to);
	}

}
