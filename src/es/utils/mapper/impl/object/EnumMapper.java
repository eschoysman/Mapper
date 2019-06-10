package es.utils.mapper.impl.object;

import java.util.EnumMap;
import java.util.EnumSet;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.MapperObject;

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

	public void activate(Mapper mapper) {
		super.activate(mapper);
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
	 * @param to destinatio enum value
     * @return the current istance
	 */
    public EnumMapper<T,U> add(T from, U to) {
        mapping.put(from,to);
        return this;
    }
    /**
     * Allow to avoid some input enum values to be mapped
     * @param inputValueToIgnore
     * @param otherInputValuesToIgnore
     * @return the current istance
     */
	@SuppressWarnings("unchecked")
	public EnumMapper<T,U> ignore(T inputValueToIgnore, T... otherInputValuesToIgnore) {
		this.inputValuesToIgnore.addAll(EnumSet.of(inputValueToIgnore, otherInputValuesToIgnore));
    	return this;
    }
	/**
	 * Allow to specify a default enum value for mapping without a counterparty in the destinatio enum or if some excception is thrown.
	 * @param defaultDestinationEnumValue
     * @return the current istance
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
