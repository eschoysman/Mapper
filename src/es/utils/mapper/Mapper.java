package es.utils.mapper;

import es.utils.doublekeymap.PairKey;
import es.utils.doublekeymap.TwoKeyMap;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.configuration.Configuration;
import es.utils.mapper.exception.CustomException;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.CollectionFactory;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;
import es.utils.mapper.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
@Component
public class Mapper {

	private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

	private String name;
	private TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> mappings;
	private Map<Class<?>,Map<String,FieldHolder>> fieldHolderCache;
	private boolean isDirty;
	private Configuration config;
	
	/**
	 * Create an empty Mapper instance with default name "defaultMapper".
	 */
	public Mapper() {
		this("defaultMapper");
	}
	/**
	 * Create an empty Mapper instance with the given name.
	 * @param name the name of the Mapper instance
	 */
	public Mapper(String name) {
		this.name = name;
		this.mappings = new TwoKeyMap<>();
		this.fieldHolderCache = new HashMap<>();
		this.config = new Configuration(this);
		this.isDirty = false;
	}

	/**
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * Allow to add a previously created mapping between type {@code T} and {@code U}.
	 * @param <T> the origin type of the mapping
	 * @param <U> the destination type of the mapping
	 * @param objectMapper the mapping to add to this {@code Mapper} instance
	 * @return The current {@code Mapper} instance
	 */
	public <T,U> Mapper add(MapperObject<T,U> objectMapper) {
		return add(objectMapper.fromClass(),objectMapper.toClass(),objectMapper);
	}
	/**
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * Allow to add a default mapping between type {@code T} and {@code U}.
	 * @param <T> type of the input object
	 * @param <U> type of the destination object
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @return this {@code Mapper} instance
	 * @throws MappingException in the following cases:
	 * <ul>
	 * 	<li>one between {@code from} and {@code to} is {@code null}</li>
	 * 	<li>one between {@code from} and {@code to} is a interface type</li>
	 * 	<li>{@code to} does not have an empty constructor</li>
	 * </ul>
	 * @see #addForClass(Class, Class)
	 * @see #addForEnum(Class, Class)
	 * @see #addBidirectional(Class, Class)
	 * @see #add(Class, Class, Function)
	 */
	public <T,U> Mapper add(Class<T> from, Class<U> to) throws MappingException {
		try {
			Objects.requireNonNull(from);
			Objects.requireNonNull(to);
			if(from.isInterface() || to.isInterface()) {
				throw CustomException.forType(MappingException.class).message("From class or To class is an interface. Please provide a concrete class.").build();
			}
			if(from.isEnum() && to.isEnum()) {
				createEnumMapper(from,to);
			}
			if(!from.isEnum() && !to.isEnum()) {
				addForClass(from,to);
			}
		} catch (NullPointerException npe) {
			throw CustomException.forType(MappingException.class).message("From class or To class is null").cause(npe).build();
		}
		return this;
	}
	/**
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * Allow to add a default mapping between class type {@code T} and {@code U}.
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @return this {@code Mapper} instance
	 * @throws MappingException if {@code to} does not have an empty constructor
	 * @see #add(Class, Class)
	 * @see #addForEnum(Class, Class)
	 * @see #addBidirectional(Class, Class)
	 * @see #add(Class, Class, Function)
	 */
	public <T,U> ClassMapper<T,U> addForClass(Class<T> from, Class<U> to) throws MappingException {
		ClassMapper<T,U> classMapper = new ClassMapper<T,U>(from,to);
		add(from,to,classMapper);
		return classMapper;
	}
	/**
	 * @param <T> the type of the source enum
	 * @param <U> the type of the destination enum
	 * Allow to add a default mapping between enum type {@code T} and {@code U}.
	 * @param from enum type of the input object
	 * @param to enum type of the destination object
	 * @return this {@code Mapper} instance
	 * @see #add(Class, Class)
	 * @see #addForClass(Class, Class)
	 * @see #addBidirectional(Class, Class)
	 * @see #add(Class, Class, Function)
	 */
	public <T extends Enum<T>,U extends Enum<U>> EnumMapper<T,U> addForEnum(Class<T> from, Class<U> to) {
		EnumMapper<T,U> enumMapper = new EnumMapper<T,U>(from,to);
		add(from,to,enumMapper);
		return enumMapper;
	}

	/**
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * Allow to add a default mapping between type {@code T} and {@code U} and between {@code U} and {@code T}.
	 * @param from type of the input and destination object
	 * @param to type of the destination and input object
	 * @return this {@code Mapper} instance
	 * @throws MappingException if both {@code from} and {@code to} does not have an empty constructor
	 * @see #add(Class, Class)
	 * @see #addForClass(Class, Class)
	 * @see #addForEnum(Class, Class)
	 * @see #add(Class, Class, Function)
	 */
	public <T,U> Mapper addBidirectional(Class<T> from, Class<U> to) throws MappingException {
		add(from,to);
		add(to,from);
		return this;
	}

	/**
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * Allow to add a custom mapping between types {@code T} and {@code U} applying a custom mapping operation
	 * @param from type of the input object
	 * @param to type of the destination object
	 * @param transformer the function that execute the mapping from {@code T} to {@code U}
	 * @return this {@code Mapper} instance
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
	 * Operation that activate all the mappings present in this {@code Mapper} instance.
	 * This operation is called automatically when the first mapping is required but
	 * it is possible to call it when preferred.
	 * @return the current {@code Mapper} instance
	 */
	public Mapper build() {
		if(isDirty) {
//			this.fieldHolderCache.keySet().forEach(k->this.fieldHolderCache.put(k,getAllFields(k)));
			mappings.values().forEach(mapping->mapping.activate());
			isDirty = false;
		}
		return this;
	}

	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the resulting object
	 * @param from the input object to be mapped
	 * @param to the destination type required for the mapping
	 * @return an Optional containing an instance of type {@code U}. If the input is {@code null} or some exception occurs during the mapping, returns {@code Optional.empty()}.
	 * @see #mapAsOptional(Object, Object)
	 * @see #map(Object, Class)
	 * @see #map(Object, Object)
	 */
	public <T,U> Optional<U> mapAsOptional(T from, Class<U> to) {
		try {
			return Optional.ofNullable(map(from,to));
		} catch (MappingNotFoundException | MappingException e) {
			return Optional.empty();
		}
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the destination object
	 * @param from the input object to be mapped
	 * @param to the destination instance that will be overridden during the mapping
	 * @return an Optional containing an instance of type {@code U}. If the input is {@code null} or some exception occurs during the mapping, returns {@code Optional.empty()}.
	 * @see #mapAsOptional(Object, Class)
	 * @see #map(Object, Class)
	 * @see #map(Object, Object)
	 */
	public <T,U> Optional<U> mapAsOptional(T from, U to) {
		try {
			return Optional.ofNullable(map(from,to));
		} catch (MappingNotFoundException | MappingException e) {
			return Optional.empty();
		}
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the destination object
	 * @param from the input object to be mapped
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the destination object
	 * @param from the input object to be mapped
	 * @return if there is only one mapping from {@code from.getClass()}, execute that mapping and return the result as an Optional,
	 * otherwise or if some exception occurs during the mapping, returns {@code Optional.empty()}.
	 * @see #mapAsOptional(Object, Class)
	 * @see #map(Object, Class)
	 * @see #map(Object, Object)
	 * @see #map(Object)
	 */
	public <T,U> Optional<U> mapAsOptional(T from) {
		try {
			return Optional.ofNullable(map(from));
		} catch (MappingNotFoundException | MappingException e) {
			return Optional.<U>empty();
		}
	}

	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the resulting object
	 * Convert the {@code from} object into a {@code U} type object
	 * @param from the input object to be mapped
	 * @param to the destination type required for the mapping
	 * @return a instance of type {@code U} created following the mappings rules between types {@code T} and {@code U}.
	 * @throws MappingNotFoundException if no mapping between types {@code T} and {@code U} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 * @see #mapAsOptional(Object, Class)
	 * @see #mapAsOptional(Object, Object)
	 * @see #map(Object, Object)
	 */
	public <T,U> U map(T from, Class<U> to) throws MappingNotFoundException, MappingException {
		if(from==null) {
			return null;
		}
		if(to == null) {
			throw CustomException.forType(MappingException.class).message(MessageFormat.format("Error mapping {0}: Destination class cannot be null",from)).build();
		}
		U map = null;
		MapperObject<T,U> mapper = getMappingBetween(getEffectiveClass(from),to);
		if(mapper==null) {
			mappingNotFound(from,to);
		}
		else {
			map = mapper.map(from);
		}
		return map;
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the destination object
	 * @param from the input object to be mapped
	 * @param to the destination instance that will be overridden during the mapping
	 * @return the object {@code to} updated during the mapping
	 * @throws MappingNotFoundException if no mapping between types {@code T} and {@code U} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 * @see #mapAsOptional(Object, Class)
	 * @see #mapAsOptional(Object, Object)
	 * @see #map(Object, Class)
	 */
	public <T,U> U map(T from, U to) throws MappingNotFoundException, MappingException {
		if(from==null) {
			return to;
		}
		if(to == null) {
			throw CustomException.forType(MappingException.class).message(MessageFormat.format("Error mapping {0}: Destination object cannot be null",from)).build();
		}
		MapperObject<T,U> mapperBetween = getMappingBetween(getEffectiveClass(from),getEffectiveClass(to));
		if(mapperBetween==null) {
			mappingNotFound(from,to.getClass());
		}
		else {
			to = mapperBetween.map(from,to);
		}
		return to;
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the mapped object
	 * @param from the input object to be mapped
	 * @return if there is only one mapping from {@code from.getClass()}, execute that mapping and return the result
	 * @throws MappingNotFoundException if no mapping or more than one from {@code from.getClass()} is found
	 * @throws MappingException if {@code to}is {@code null} or an error occurs during the mapping
	 */
	public <T,U> U map(T from) throws MappingNotFoundException, MappingException {
		U map = null;
		if(from==null) {
			return map;
		}
		build();
		List<MapperObject<?,?>> list = getAllMappings().values().stream()
											.filter(m->from.getClass().equals(m.fromClass()))
											.filter(m->from.getClass().isAssignableFrom(m.fromClass()))
											.collect(Collectors.toList());
		if(list.size()!=1) {
			throw CustomException.forType(MappingNotFoundException.class).message(MessageFormat.format("Found {0} mapping(s) from {1}. Cannot uniquely map the input {2}.",list.size(),from.getClass(),from)).build();
		}
		@SuppressWarnings("unchecked")
		MapperObject<T,U> mapper = (MapperObject<T,U>)list.get(0);
		map = mapper.map(from);
		return map;
	}

	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the resulting object
	 * @param <E> the type of the exception to throw
	 * Convert the {@code from} object into a {@code U} type object
	 * @param from the input object to be mapped
	 * @param to the destination type required for the mapping
	 * @param customException instance of {@code CustomException} used to throw custom {@link Exception} in case of error curing the mapping
	 * @return a instance of type {@code U} created following the mappings rules between types {@code T} and {@code U}.
	 * @throws E the exception used to create the customException in one of the following case:
	 * <ol>
	 *     <li>no mapping between types {@code T} and {@code U} is found</li>
	 *     <li>{@code to} is {@code null}</li>
	 *     <li>an error occurs during the mapping</li>
	 * </ol>
	 * @see #mapAsOptional(Object, Class)
	 * @see #mapAsOptional(Object, Object)
	 * @see #map(Object, Object)
	 */
	public <T,U, E extends Exception> U map(T from, Class<U> to, CustomException<E> customException) throws E {
		try {
			return map(from, to);
		} catch(MappingException | MappingNotFoundException e) {
			throw customException.cause(e).build();
		}
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the destination object
	 * @param <E> the type of the exception to throw
	 * @param from the input object to be mapped
	 * @param to the destination instance that will be overridden during the mapping
	 * @param customException instance of {@code CustomException} used to throw custom {@link Exception} in case of error curing the mapping
	 * @return the object {@code to} updated during the mapping
	 * @throws E the exception used to create the customException in one of the following case:
 	 * <ol>
 	 *     <li>no mapping between types {@code T} and {@code U} is found</li>
 	 *     <li>{@code to} is {@code null}</li>
 	 *     <li>an error occurs during the mapping</li>
 	 * </ol>
	 * @see #mapAsOptional(Object, Class)
	 * @see #mapAsOptional(Object, Object)
	 * @see #map(Object, Class)
	 */
	public <T,U, E extends  Exception> U map(T from, U to, CustomException<E> customException) throws E {
		try {
			return map(from, to);
		} catch(MappingException | MappingNotFoundException e) {
			throw customException.cause(e).build();
		}
	}
	/**
	 * @param <T> the type of the object to be mapped
	 * @param <U> the type of the mapped object
	 * @param <E> the type of the exception to throw
	 * @param from the input object to be mapped
	 * @param customException instance of {@code CustomException} used to throw custom {@link Exception} in case of error curing the mapping
	 * @return if there is only one mapping from {@code from.getClass()}, execute that mapping and return the result
	 * @throws E the exception used to create the customException in one of the following case:
	 * <ol>
	 *     <li>no mapping or more than one from {@code from.getClass()} is found</li>
	 *     <li>{@code to} is {@code null}</li>
	 *     <li>an error occurs during the mapping</li>
	 * </ol>
	 */
	public <T,U, E extends  Exception> U map(T from, CustomException<E> customException) throws E {
		try {
			return map(from);
		} catch(MappingException | MappingNotFoundException e) {
			throw customException.cause(e).build();
		}
	}

	/**
	 * @param <T> the type of the source class
	 * @param <U> the type of the destination class
	 * @param origin array of type {@code T} object 
	 * @param destination array of type {@code U} object
	 * @return the {@code destination} array filled with {@code origin} elements mapped or {@code null}s if the mapping does not exists.
	 */
	public <T,U> U[] mapArray(T[] origin, U[] destination) {
		if(origin==null) {
			return destination;
		}
		Objects.requireNonNull(destination);
		destination = Arrays.copyOf(destination,Math.max(destination.length,origin.length));
		@SuppressWarnings("unchecked")
		Class<T> originType = (Class<T>)origin.getClass().getComponentType();
		@SuppressWarnings("unchecked")
		Class<U> destinationType = (Class<U>)destination.getClass().getComponentType();
		MapperObject<T,U> mapper = getMappingBetween(originType,destinationType);
		if(mapper!=null) {
			Arrays.setAll(destination,i->mapper.mapOrNull(origin[i]));
		}
		return destination;
	}
	/**
	 * @param <T> the type of the source class
	 * @param <U> the type of the destination class
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
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * @param <CT> the type of the input collection
	 * @param <CU> the type of the resulting collection
	 * @param origin original collection to map
	 * @param collectionType type of the destination collection
	 * @param resultElementType destination type of the mapping 
	 * @return the mapped collection
	 * @see CollectionType
	 * @see CollectionFactory#create(Class, Class)
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
	 * @param <T> the type of the source object
	 * @param <U> the type of the destination object
	 * @param <CU> the type of the resulting collection
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

		Class<?> srcGenericType = null;
		Iterator<T>	iter = origin.iterator();
		if(iter.hasNext())
			srcGenericType = iter.next().getClass();
		if(!origin.isEmpty() && srcGenericType!=null && resultElementType!=null) {
			@SuppressWarnings("unchecked")
			MapperObject<T,U> mapper = (MapperObject<T,U>)getMappingBetween(srcGenericType,resultElementType);
			Optional.ofNullable(mapper)
					.map(m->origin.stream().map(m::mapOrNull))
					.get().forEach(destination::add);
		}
		return destination;
	}
	
	/**
	 * @param <T> the type of the source class
	 * @param <U> the type of the destination class
	 * @param src origin type
	 * @param dest destination type
	 * @return {@code true} the mapping between types {@code T} and {@code U} is present, {@code false} otherwise
	 */
	public <T,U> boolean hasMappingBetween(Class<T> src, Class<U> dest) {
		return mappings.containsKey(src,dest);
	}
	
	/**
	 * @param <T> the type of the source class
	 * @param <U> the type of the destination class
	 * @param from origin type
	 * @param to destination type
	 * @return the mapping between types {@code T} and {@code U} if present, {@code null} otherwise 
	 */
	public <T,U> MapperObject<T,U> getMappingBetween(Class<T> from, Class<U> to) {
		@SuppressWarnings("unchecked")
		MapperObject<T,U> result = (MapperObject<T,U>)mappings.get(from,to);
		if(result==null && to.isAssignableFrom(from)) {
			result = new DirectMapper<T,U>(from,to,to::cast);
			add(result);
		}
		build();
		return result;
	}

	
	/**
	 * @return the {@code TwoKeyMap} containing all the mappings present in this {@code Mapper} instance
	 * @see TwoKeyMap
	 */
	public TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> getAllMappings() {
		return mappings;
	}
	/**
	 * Returns a human readable representation of the mappings present in this {@code Mapper} instance
	 */
	@Override
	public String toString() {
		return "Mapper["+name+"]["+mappings.keySet().stream().map(PairKey::toString).collect(Collectors.joining(", "))+"]";
	}
	/**
	 * Prints the same output as {@code toString} in a nicer way
	 * @return a string representation of the mapper
	 */
	public String prettyPrint() {
		return "Mapper \""+name+"\""+mappings.values().stream().map(MapperObject::toString).collect(Collectors.joining("\n\t","\n\t",""));
	}

	/**
	 * @param <T> the type of the class
	 * @param from class type
	 * @return the Map having key the name or alias and the associate FieldHolder
	 * @see FieldHolder
	 */
	public <T> Map<String,FieldHolder> getFieldsHolderFromCache(Class<T> from) {
		return this.fieldHolderCache.computeIfAbsent(from,this::getAllFields);
	}
	
	/**
	 * @param <T> the type of the class
	 * @param type class type
	 * @return the list of the names and alias allowed for the given type {@code T}
	 */
	public <T> List<String> getNames(Class<T> type) {
		return new ArrayList<>(getFieldsHolderFromCache(type).keySet());
	}

	/**
	 * Returns the current configuration of the mapping
	 * @return The current configuration of the mapping
	 */
	public Configuration config() {
		return this.config;
	}
	/**
	 * Set the configuration to use
	 * @param config The {@code Configuration} instance to associate
	 * @return The current {@code Mapper} instance
	 */
	public Mapper setConfig(Configuration config) {
		this.config = config;
		return this;
	}
	
	/**
	 * Create a new instance of the given type first by looking for a supplier in the configuration, second by the empty constructor
	 * @param <TYPE> the type of the returned instance
	 * @param type the type of the returned instance
	 * @return a new instance of the given type
	 * @throws MappingException if there is no empty constructor to be invoked 
	 */
    public <TYPE> TYPE createNewInstance(Class<TYPE> type) throws MappingException {
		Supplier<TYPE> supplier = config().getSupplier(type);
    	if(supplier==null) {
			return newInstance(type);
    	}
		return supplier.get();
    }

	/**
	 * Returns the name of the current mapper
	 * @return the name of the current mapper
	 */
	public String getMapperName() {
    	return this.name;
	}
    
	private <T,U> Mapper add(Class<T> from, Class<U> to, MapperObject<T,U> objectMapper) {
		mappings.put(from,to, objectMapper);
		objectMapper.setMapper(this);
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
		add(from,to,enumMapper);
		return enumMapper;
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
	private <T,U> void mappingNotFound(T from, Class<U> toClass) throws MappingNotFoundException {
		@SuppressWarnings("unchecked")
		Class<T> fromClass = (Class<T>)from.getClass();
		StringBuilder sb = new StringBuilder("WARNING - No mappings found in " + this.getClass().getName() + "["+getMapperName()+"] for input " + fromClass + " and output " + toClass + "\nInput instance: "+from+"\n");
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
		sb.append("Existing destination mappings from "+fromClass+":\n");
		if(dest.isEmpty()) {
			dest.add("none");
		}
		dest.forEach(s->sb.append("\t"+s+"\n"));
		sb.append("Existing source mappings to "+toClass+":\n");
		if(src.isEmpty()) {
			src.add("none");
		}
		src.forEach(s->sb.append("\t"+s+"\n"));
		sb.append("Other existing mappings:\n");
		if(other.isEmpty()) {
			other.add("none");
		}
		other.forEach(s->sb.append("\t"+s+"\n"));
		throw CustomException.forType(MappingNotFoundException.class).message(sb).build();
	}
	private Map<String,FieldHolder> getAllFields(Class<?> type) {
    	Map<String,FieldHolder> result = new HashMap<>();
    	for(Field field : MapperUtil.getAllFields(type)) {
    		FieldHolder fieldHolder = new FieldHolder(field,config());
    		if(!fieldHolder.ignoreField()) {
				for (String name : fieldHolder.getAllNames()) {
					if (result.put(name, fieldHolder) != null) {
						try {
							throw CustomException.forType(MappingException.class).message(MessageFormat.format("At least two fields in {0} have the same name or alias \"{1}\"", type, name)).build();
						} catch (MappingException e) {
							e.printStackTrace();
						}
					}
				}
			}
    	}
    	return result;
    }
    private <T> T newInstance(Class<T> type) throws MappingException {
		try {
			T dest = type.getDeclaredConstructor().newInstance();
			return dest;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw CustomException.forType(MappingException.class).message("Destination class does not have a public empty constructor. Please provide a public empty constructor or a Supplier in the configuration such that the mapping can be done.").build();
		}
    }

}
