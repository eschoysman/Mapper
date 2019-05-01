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
 * 
 * @author Emmanuel
 *
 */
public class Mapper {

	private TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> mappings;
	private Map<Class<?>,Map<String,FieldHolder>> fieldHolderCache;
	private boolean isDirty;
	
	/**
	 * 
	 */
	public Mapper() {
		this.mappings = new TwoKeyMap<>();
		this.fieldHolderCache = new HashMap<>();
		isDirty = false;
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MappingException
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
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MappingException
	 */
	public <T,U> ClassMapper<T,U> addForClass(Class<T> from, Class<U> to) throws MappingException {
		try {
			to.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new MappingException("Destination class does not have a empty constructor. Please provide a empty contructor such that the mapping can be done.");
		}
		ClassMapper<T,U> classMapper = new ClassMapper<T,U>(from,to);
		mappings.put(from,to, classMapper);
		isDirty = true;
		return classMapper;
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public <T extends Enum<T>,U extends Enum<U>> EnumMapper<T,U> addForEnum(Class<T> from, Class<U> to) {
		EnumMapper<T,U> enumMapper = new EnumMapper<T,U>(from,to);
		mappings.put(from,to, enumMapper);
		isDirty = true;
		return enumMapper;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MappingException
	 */
	public <T,U> Mapper addBidirectional(Class<T> from, Class<U> to) throws MappingException {
		add(from,to);
		add(to,from);
		return this;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param transformer
	 * @return
	 * @throws MappingException
	 */
	public <T,U> Mapper add(Class<T> from, Class<U> to, Function<T,U> transformer) throws MappingException {
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		Objects.requireNonNull(transformer);
		mappings.put(from,to, new DirectMapper<>(from,to,transformer));
		this.isDirty = true;
		return this;
	}
	
	/**
	 * 
	 */
	public void build() {
		if(isDirty) {
			mappings.values().forEach(mapping->mapping.activate(this));
			isDirty = false;
		}
	}

	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public <T,U> U mapOrNull(T from, Class<U> to) {
		try {
			return map(from,to);
		} catch (MappingNotFoundException | MappingException e) {
			return null;
		}
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public <T,U> U mapOrNull(T from, U to) {
		try {
			return map(from,to);
		} catch (MappingNotFoundException | MappingException e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MappingNotFoundException
	 * @throws MappingException
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
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws MappingNotFoundException
	 * @throws MappingException
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
	 * 
	 * @param origin
	 * @param destination
	 * @return
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
	 * 
	 * @param origin
	 * @param destinationType
	 * @return
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
	 * @param origin
	 * @param collectionType
	 * @param resultElementType
	 * @return
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
	 * 
	 * @param origin
	 * @param destination
	 * @param resultElementType
	 * @return
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
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public <T,U> boolean hasMappingBetween(Class<T> src, Class<U> dest) {
		return mappings.containsKey(src,dest);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public <T,U> MapperObject<T,U> getMappingBetween(Class<T> from, Class<U> to) {
		@SuppressWarnings("unchecked")
		MapperObject<T,U> result = (MapperObject<T,U>)mappings.get(from,to);
		return result;
	}

	
	/**
	 * 
	 * @return
	 */
	public TwoKeyMap<Class<?>,Class<?>,MapperObject<?,?>> getAllMappings() {
		return mappings;
	}
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Mapper["+mappings.keySet().stream().map(PairKey::toString).collect(Collectors.joining(", "))+"]";
	}

	/**
	 * 
	 * @param from
	 * @return
	 */
	public <T> Map<String,FieldHolder> getFieldsHolderFromCache(Class<T> from) {
		return this.fieldHolderCache.computeIfAbsent(from,this::getAllFields);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public <T> List<String> getNames(Class<T> type) {
		List<String> names = new ArrayList<>();
		if(this.fieldHolderCache.containsKey(type)) {
			names.addAll(this.fieldHolderCache.get(type).keySet());
		}
		return names;
	}
	
	private <T,U> MapperObject<T,U> createEnumMapper(Class<T> from, Class<U> to) throws MappingException {
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
