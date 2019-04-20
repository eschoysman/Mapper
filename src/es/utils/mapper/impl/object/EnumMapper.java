package es.utils.mapper.impl.object;

import java.util.EnumMap;
import java.util.EnumSet;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.MapperObject;

public class EnumMapper<T extends Enum<T>,U extends Enum<U>> extends MapperObject<T, U> {

	private EnumMap<T,U> mapping;
	private EnumSet<T> inputValuesToIgnore;
	private U defaultDestinationEnumValue;
	
	public EnumMapper(Class<T> fromEnum, Class<U> toEnum) {
		super(fromEnum,toEnum);
		this.mapping = new EnumMap<T,U>(from);
		this.inputValuesToIgnore = EnumSet.noneOf(from);
		this.defaultDestinationEnumValue = null;
	}

	public void activate(Mapper mapper) {
		for(T from : from.getEnumConstants()) {
			try {
				add(from,Enum.valueOf(to,from.name()));
			} catch(IllegalArgumentException e) {
			}
		}
	}
	
    public EnumMapper<T,U> add(T from, U to) {
        mapping.put(from,to);
        return this;
    }
	@SuppressWarnings("unchecked")
	public EnumMapper<T,U> ignore(T inputValueToIgnore, T... otherInputValuesToIgnore) {
		this.inputValuesToIgnore.addAll(EnumSet.of(inputValueToIgnore, otherInputValuesToIgnore));
    	return this;
    }
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
