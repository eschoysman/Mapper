package es.utils.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.utils.doublekeymap.PairKey;
import es.utils.doublekeymap.TwoKeyMap;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.CollectionFactory;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;
import es.utils.mapper.utils.MapperUtil;

/**
 * This class handle a set of mapping between two objects. It is the central part of
 * the mapping logic and allow the user to create multiple mappings easily.
 * @author eschoysman
 * 
 * @see MapperObject
 * @see ClassMapper
 * @see DirectMapper
 * @see EnumMapper
 */
public class Mapper {

	private TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> mappings;
	private Map<Class<?>,Map<String,FieldHolder>> fieldHolderCache;
	private boolean isDirty;
	
	/**
	 * Create an empty Mapper istance.
	 */
	public Mapper() {
		this.mappings = new TwoKeyMap<>();
		this.fieldHolderCache = new HashMap<>();
		isDirty = false;
	}

	/**
	 * Allow to add a previoously created mapping between type {@code T} and {@code U}.
	 * @param <T> the origin type of the mapping
	 * @param <U> the destination type of the mapping
	 * @param objectMapper the mapping to add to this {@code Mapper} istance
	 * @return this {@code Mapper} istance
	 */
	public <T,U> Mapper add(MapperObject<T,U> objectMapper) {
		return add(objectMapper.fromClass(),objectMapper.toClass(),objectMapper);
	}
	/**
	 * Allow to add a default mapping between type {@code T} and {@code U}.
	 * @param <T> type of the input object
	 * @param <U> type of the destination object
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @return this {@code Mapper} istance
	 * @throws MappingException in the following cases:
	 * <ul>
	 * 	<li>one between {@code from} and {@code to} is {@code null}</li>
	 * 	<li>one between {@code from} and {@code to} is a interface type</li>
	 * 	<li>{@code to} does not have an empty constructor</li>
	 * </ul>
	 * @see {@link #addForClass(Class, Class)}
	 * @see {@link #addForEnum(Class, Class)}
	 * @see {@link #addBidirectional(Class, Class)}
	 * @see {@link #add(Class, Class, Function)}
	 */
	public <T,U> Mapper add(Class<T> from, Class<U> to) throws MappingException {
		try {
			Objects.requireNonNull(from);
			Objects.requireNonNull(to);
			if(from.isInterface() || to.isInterface()) {
				throw new MappingException("From class or To class is an interface. Please provide a concrete class.");
			}
			if(from.isEnum() && to.isEnum()) {
				createEnumMapper(from,to);
			}
			if(!from.isEnum() && !to.isEnum()) {
				addForClass(from,to);
			}
		} catch (NullPointerException npe) {
			throw new MappingException("From class or To class is null", npe);
		}
		return this;
	}
	/**
	 * Allow to add a default mapping between class type {@code T} and {@code U}.
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @return this {@code Mapper} istance
	 * @throws MappingException if {@code to} does not have an empty constructor
	 * @see {@link #add(Class, Class)}
	 * @see {@link #addForEnum(Class, Class)}
	 * @see {@link #addBidirectional(Class, Class)}
	 * @see {@link #add(Class, Class, Function)}
	 */
	public <T,U> ClassMapper<T,U> addForClass(Class<T> from, Class<U> to) throws MappingException {
		try {
			to.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new MappingException("Destination class does not have a empty constructor. Please provide a empty contructor such that the mapping can be done.");
		}
		ClassMapper<T,U> classMapper = new ClassMapper<T,U>(from,to);
		add(from,to,classMapper);
		return classMapper;
	}
	/**
	 * Allow to add a default mapping between enum type {@code T} and {@code U}.
	 * @param from enum type of the input object
	 * @param to enm type of the destination object
	 * @return this {@code Mapper} istance
	 * @see {@link #add(Class, Class)}
	 * @see {@link #addForClass(Class, Class)}
	 * @see {@link #addBidirectional(Class, Class)}
	 * @see {@link #add(Class, Class, Function)}
	 */
	public <T extends Enum<T>,U extends Enum<U>> EnumMapper<T,U> addForEnum(Class<T> from, Class<U> to) {
		EnumMapper<T,U> enumMapper = new EnumMapper<T,U>(from,to);
		add(from,to,enumMapper);
		return enumMapper;
	}

	/**
	 * Allow to add a default mapping between type {@code T} and {@code U} and between {@code U} and {@code T}.
	 * @param from type of the input and destination object
	 * @param to type of the destination and input object
	 * @return this {@code Mapper} istance
	 * @throws MappingException if both {@code from} and {@code to} does not have an empty constructor
	 * @see {@link #add(Class, Class)}
	 * @see {@link #addForClass(Class, Class)}
	 * @see {@link #addForEnum(Class, Class)}
	 * @see {@link #add(Class, Class, Function)}
	 */
	public <T,U> Mapper addBidirectional(Class<T> from, Class<U> to) throws MappingException {
		add(from,to);
		add(to,from);
		return this;
	}

	/**
	 * Allow to add a custom mapping between types {@code T} and {@code U} applying a custom mapping operation
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @param transformer the function that execute the mapping from {@code T} to {@code U}
	 * @return this {@code Mapper} istance
	 * @throws MappingException if {@code from}, {@code to} or {@code transformer} is {@code null}
	 */
	public <T,U> Mapper add(Class<T> from, Class<U> to, Function<T,U> transformer) throws MappingException {
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		Objects.requireNonNull(transformer);
		DirectMapper<T, U> directMapper = new DirectMapper<>(from,to,transformer);
		add(from,to,directMapper);
		return this;
	}
	
	/**
	 * Operation that activate all the mappings present in this {@code Mapper} istance.
	 * This operation is called automatically when the first mapping is required but
	 * it is possible to call it when preferred. 
	 */
	public void build() {
		if(isDirty) {
			mappings.values().forEach(mapping->mapping.activate(this));
			isDirty = false;
		}
	}

	/**
	 * @param from the input object to be mapped
	 * @param to the destination type required for the mapping
	 * @return an instance of type {@code U}. If the input is {@code null} or some eception occurs during the mapping, returns {@code null}.
	 * @see {@link #mapOrNull(Object, Object)}
	 * @see {@link #map(Object, Class)}
	 * @see {@link #map(Object, Object)}
	 */
	public <T,U> U mapOrNull(T from, Class<U> to) {
		try {
			return map(from,to);
		} catch (MappingNotFoundException | MappingException e) {
			return null;
		}
	}
	/**
	 * @param from the input object to be mapped
	 * @param to the destination istance that wil be overridden during the mapping
	 * @return an instance of type {@code U}. If the input is {@code null} or some eception occurs during the mapping, returns {@code null}.
	 * @see {@link #mapOrNull(Object, Class)}
	 * @see {@link #map(Object, Class)}
	 * @see {@link #map(Object, Object)}
	 */
	public <T,U> U mapOrNull(T from, U to) {
		try {
			return map(from,to);
		} catch (MappingNotFoundException | MappingException e) {
			return null;
		}
	}
	
	/**
	 * Convert the {@code from} object into a {@code U} type object
	 * @param from the input object to be mapped
	 * @param to the destination type required for the mapping
	 * @return a istance of type {@code U} created following the mappings rules between types {@code T} and {@code U}.
	 * @throws MappingNotFoundException if no mapping bwteween types {@code T} and {@code U} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 * @see {@link #mapOrNull(Object, Class)}
	 * @see {@link #mapOrNull(Object, Object)}
	 * @see {@link #map(Object, Object)}
	 */
	public <T,U> U map(T from, Class<U> to) throws MappingNotFoundException, MappingException {
		if(to == null) {
			throw new MappingException("Destination class cannot be null");
		}
		build();
		U map = null;
		if(from==null) {
			return map;
		}
		MapperObject<T,U> mapper = getMappingBetween(getEffectiveClass(from),to);
		if(mapper==null) {
			mappingNotFound(from.getClass(),to);
		}
		else {
			map = mapper.map(from);
		}
		return map;
	}
	/**
	 * @param from the input object to be mapped
	 * @param to the destination istance that wil be overridden during the mapping
	 * @return the object {@code to} updated during the mapping
	 * @throws MappingNotFoundException if no mapping beteween types {@code T} and {@code U} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 * @see {@link #mapOrNull(Object, Class)}
	 * @see {@link #mapOrNull(Object, Object)}
	 * @see {@link #map(Object, Class)}
	 */
	public <T,U> U map(T from, U to) throws MappingNotFoundException, MappingException {
		build();
		if(from==null) {
			return to;
		}
		if(to == null) {
			throw new MappingException("Destination object cannot be null");
		}
		else {
			MapperObject<T,U> mapperBetween = getMappingBetween(getEffectiveClass(from),getEffectiveClass(to));
			if(mapperBetween==null) {
				mappingNotFound(from.getClass(),to.getClass());
			}
			else {
				to = mapperBetween.map(from,to);
			}
		}
		return to;
	}
	
	/**
	 * @param from the input object to be mapped
	 * @return if there is only one mapping from {@code from.getClass()}, execute that mapping and return the result
	 * @throws MappingNotFoundException if no mapping or more than one from {@code from.getClass()} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 */
	public <T,U> U map(T from) throws MappingNotFoundException, MappingException {
		build();
		U map = null;
		if(from==null) {
			return map;
		}
		List<MapperObject<?,?>> list = getAllMappings().values().stream()
											.filter(m->from.getClass().equals(m.fromClass()))
											.filter(m->from.getClass().isAssignableFrom(m.fromClass()))
											.collect(Collectors.toList());
		if(list.size()!=1) {
			throw new MappingNotFoundException("Found "+list.size()+" mapping(s) from "+from.getClass()+". Cannot uniquely map the input.");
		}
		@SuppressWarnings("unchecked")
		MapperObject<T,U> mapper = (MapperObject<T,U>)list.get(0);
		map = mapper.map(from);
		return map;
	}
	
	/**
	 * @param origin array of type {@code T} object 
	 * @param destination array of type {@code U} object
	 * @return the {@code destination} array filled with {@code origin} elements mapped
	 */
	public <T,U> U[] mapArray(T[] origin, U[] destination) {
		if(origin==null) {
			return destination;
		}
		Objects.requireNonNull(destination);
		build();
		destination = Arrays.copyOf(destination,Math.max(destination.length,origin.length));
		@SuppressWarnings("unchecked")
		Class<T> originType = (Class<T>)origin.getClass().getComponentType();
		@SuppressWarnings("unchecked")
		Class<U> destinationType = (Class<U>)destination.getClass().getComponentType();
		MapperObject<T,U> mapper = getMappingBetween(originType,destinationType);
		if(mapper!=null) {
			Arrays.setAll(destination,i->mapper.mapOrNull(origin[i]));
		}
		else if(originType.isAssignableFrom(destinationType)) {
			Arrays.setAll(destination,i->destinationType.cast(origin[i]));
		}
		return destination;
	}
	/**
	 * @param origin array of type {@code T} object 
	 * @param destinationType destination type of the mapping
	 * @return a {@code U[]} filled with {@code origin} elements mapped
	 */
	public <T,U> U[] mapArray(T[] origin, Class<U> destinationType) {
		if(origin==null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		U[] destination = (U[])Array.newInstance(destinationType,origin.length);
		return mapArray(origin,destination);
	}
	/**
	 * 
	 * @param origin original collection to map
	 * @param collectionType type of the destination collection
	 * @param resultElementType destination type of the mapping 
	 * @return the mapped collection
	 * @see CollectionType
	 * @see CollectionFactory
	 */
	public <T,U,CT extends Collection<T>, CU extends Collection<U>> CU mapCollection(CT origin, Class<CU> collectionType, Class<U> resultElementType) {
		if(origin==null) {
			return null;
		}
		Objects.requireNonNull(collectionType);
		@SuppressWarnings("unchecked")
		CU destination = (CU)CollectionFactory.create(null,collectionType);
		
		return mapCollection(origin, destination, resultElementType);
	}
	
	/**
	 * @param origin original collection to map
	 * @param destination destination collection
	 * @param resultElementType destination type of the mapping 
	 * @return the mapped collection
	 */
	public <T,U, CU extends Collection<U>> CU mapCollection(Collection<T> origin, CU destination, Class<U> resultElementType) {
		if(origin==null) {
			return null;
		}
		Objects.requireNonNull(destination);
		Objects.requireNonNull(resultElementType);
		build();

		Class<?> srcGenericType = null;
		Iterator<T>	iter = origin.iterator();
		if(iter.hasNext())
			srcGenericType = iter.next().getClass();
		if(!origin.isEmpty() && srcGenericType!=null && resultElementType!=null) {
			@SuppressWarnings("unchecked")
			MapperObject<T,U> mapper = (MapperObject<T,U>)getMappingBetween(srcGenericType,resultElementType);
			Stream<T> originStream = origin.stream();
			if(mapper!=null) {
				originStream.map(mapper::mapOrNull).forEach(destination::add);
			}
			else if(srcGenericType.isAssignableFrom(resultElementType)) {
				originStream.map(resultElementType::cast).forEach(destination::add);
			}
		}
		return destination;
	}
	
	/**
	 * @param src origin type
	 * @param dest destination type
	 * @return {@code true} the mapping between types {@code T} and {@code U} is present, {@code false} otherwise
	 */
	public <T,U> boolean hasMappingBetween(Class<T> src, Class<U> dest) {
		return mappings.containsKey(src,dest);
	}
	
	/**
	 * @param from origin type
	 * @param to destination type
	 * @return the mapping between types {@code T} and {@code U} if present, {@code null} otherwise 
	 */
	public <T,U> MapperObject<T,U> getMappingBetween(Class<T> from, Class<U> to) {
		@SuppressWarnings("unchecked")
		MapperObject<T,U> result = (MapperObject<T,U>)mappings.get(from,to);
		return result;
	}

	
	/**
	 * @return the {@code TwoKeyMap} containing all the mappings presente in this {@code Mapper} istance
	 * @see TwoKeyMap
	 */
	public TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> getAllMappings() {
		return mappings;
	}
	/**
	 * Returns a human readable rappersentation of the mappings presente in this {@code Mapper} istance
	 */
	@Override
	public String toString() {
		return "Mapper["+mappings.keySet().stream().map(PairKey::toString).collect(Collectors.joining(", "))+"]";
	}

	/**
	 * @param type class type
	 * @return the Map having key the name or alias and the associate FieldHolder
	 * @see FieldHolder
	 */
	public <T> Map<String,FieldHolder> getFieldsHolderFromCache(Class<T> from) {
		return this.fieldHolderCache.computeIfAbsent(from,this::getAllFields);
	}
	
	/**
	 * @param type class type
	 * @return the list of the names and alias allowed for the given type {@code T}
	 */
	public <T> List<String> getNames(Class<T> type) {
		List<String> names = new ArrayList<>();
		if(this.fieldHolderCache.containsKey(type)) {
			names.addAll(this.fieldHolderCache.get(type).keySet());
		}
		return names;
	}
	
	private <T,U> Mapper add(Class<T> from, Class<U> to, MapperObject<T,U> objectMapper) {
		mappings.put(from,to, objectMapper);
		isDirty = true;
		return this;
	}
	private <T,U> MapperObject<T,U> createEnumMapper(Class<T> from, Class<U> to) {
		@SuppressWarnings("unchecked")
		Class<? extends Enum<?>> enumFrom = (Class<? extends Enum<?>>)from;
		@SuppressWarnings("unchecked")
		Class<? extends Enum<?>> enumTo = (Class<? extends Enum<?>>)to;
		@SuppressWarnings({"unchecked","rawtypes"})
		MapperObject<T,U> enumMapper = new EnumMapper(enumFrom,enumTo);
		mappings.put(from,to, enumMapper);
		isDirty = true;
		return enumMapper;
	}
	private <T,U> void mappingNotFound(Class<T> fromClass, Class<U> toClass) throws MappingNotFoundException {
		StringBuilder sb = new StringBuilder("WARNING - No mappings found in " + this.getClass().getName() + " for input " + fromClass.getClass() + " and output " + toClass + "\n");
		ArrayList<String> dest  = new ArrayList<>();
		ArrayList<String> src   = new ArrayList<>();
		ArrayList<String> other = new ArrayList<>();
		for(MapperObject<?,?> mapper : mappings.values()) {
			if(mapper.fromClass().equals(fromClass)) {
				dest.add(mapper.toClass().toString());
			}
			else if(mapper.toClass().equals(toClass)) {
				src.add(mapper.fromClass().toString());
			}
			else {
				other.add(mapper.fromClass()+" -> "+mapper.toClass());
			}
		};
		sb.append("Exisiting destination mappings from "+fromClass+":\n");
		if(dest.isEmpty()) {
			dest.add("none");
		}
		dest.forEach(s->sb.append("\t"+s+"\n"));
		sb.append("Exisiting source mappings to "+toClass+":\n");
		if(src.isEmpty()) {
			src.add("none");
		}
		src.forEach(s->sb.append("\t"+s+"\n"));
		sb.append("Other exisiting mappings:\n");
		if(other.isEmpty()) {
			other.add("none");
		}
		other.forEach(s->sb.append("\t"+s+"\n"));
		throw new MappingNotFoundException(sb.toString());
	}
	private <T> Class<T> getEffectiveClass(T obj) {
		Class<?> tmp = obj.getClass();
		if(Enum.class.isInstance(obj)) {
			tmp = ((Enum<?>)obj).getDeclaringClass();
		}
		@SuppressWarnings("unchecked")
		Class<T> effectiveClass = (Class<T>)tmp;
		return effectiveClass;
	}
	private Map<String,FieldHolder> getAllFields(Class<?> type) {
    	Map<String,FieldHolder> result = new HashMap<>();
    	for(Field field : MapperUtil.getAllFields(type)) {
    		FieldHolder fieldHolder = new FieldHolder(field);
    		for(String name : fieldHolder.getAllNames()) {
    			FieldHolder prevValue = result.put(name,fieldHolder);
	    		if(prevValue!=null) {
	    			try {
						throw new MappingException("Two Fields in "+type+" have the same name or alias "+name);
					} catch (MappingException e) {
						e.printStackTrace();
					}
	    		}
    		}
    	}
    	return result;
    }

}
