package es.utils.mapper.impl.object;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

import es.utils.doublekeymap.TwoKeyMap;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.Factory;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;

/**
 * This class customize the abstract class {@code MapperObject} creating a {@code MapperObject} that convert on object of type {@code T} into a {@code U}.
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <U> the type of the destination object
 * 
 * @see MapperObject
 * @see DirectMapper
 * @see EnumMapper
 * @see ElementMapper
 * @see Factory
 */
public class ClassMapper<T,U> extends MapperObject<T,U> {
	
	private TwoKeyMap<String,String,ElementMapper<T,?,?,U>> fieldMappings;
	private TwoKeyMap<String,String,ElementMapper<T,?,?,U>> customMappings;
	private Set<String> inputsToIgnore;
	private Set<String> outputsToIgnore;
	
	private List<ElementMapper<T,?,?,U>> elementMappings;
	private boolean isDirty;
	
	/**
	 * Create a {@code MapperObject} from type {@code T} to type {@code U}.
	 * @param fromClass the type of the origin object
	 * @param toClass the type of the destination object
	 */
	public ClassMapper(Class<T> fromClass, Class<U> toClass) {
		super(fromClass,toClass);
		this.fieldMappings = new TwoKeyMap<>();
		this.customMappings = new TwoKeyMap<>();
		this.inputsToIgnore = new TreeSet<>();
		this.outputsToIgnore = new TreeSet<>();
		this.elementMappings = new ArrayList<>();
		this.isDirty = false;
	}

	protected U mapValue(T from) throws MappingException {
        try {
			U dest = to.getDeclaredConstructor().newInstance();
			return mapValue(from,dest);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new MappingException("Error during the creation of the destination object.", e);
		}
	}
	protected U mapValue(T from, U to) {
		Objects.requireNonNull(to);
		getElementMappings().forEach(me->me.apply(from,to));
		return to;
    }
	
	public void activate(Mapper mapper) {
		this.mapper = mapper;
		createDefaultMappings();
	}


	// add methods
	/**
	 * @param <T1> inner type of the {@code elementMapper}, is the type returned by the getter logic
	 * @param <T2> inner type of the {@code elementMapper}, is the type required by the setter logic
	 * Allow to add a custom {@code ElementMapper} into the mapping between type {@code T} and {@code U}.
	 * @param elementMapper the mapper for a single element of this {@code ClassMapper}
     * @return this istance
	 * @see ElementMapper
	 */
    public <T1,T2> ClassMapper<T,U> addElementMapper(ElementMapper<T,T1,T2,U> elementMapper) {
    	return addElementMapper(elementMapper,false);
    }
    /**
	 * Allow to add a custom mapping into the mapping between type {@code T} and {@code U} using a {@code Getter} and a {@code Setter}.
	 * @param <TMP> the type of the result of the {@code getter} and of the input of the {@code setter}.
     * @param getter {@code Getter} instance to retrieve the value to map
     * @param setter {@code Setter} instance to set the mapped value
     * @return the current istance
     * @see Factory#element(Getter, Setter)
     */
    public <TMP> ClassMapper<T,U> addElementMapper(Getter<T,TMP> getter, Setter<U,TMP> setter) {
    	return addElementMapper(Factory.element(getter,setter),false);
    }
    /**
	 * Allow to add a custom mapping into the mapping between type {@code T} and {@code U} using a {@code Getter}, a tranformer and a {@code Setter}.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param getter {@code Getter} instance to retrieve the value to map
     * @param transformer the function to map the result of the {@code getter} into the type required by the {@code setter}
     * @param setter {@code Setter} instance to set the mapped value
     * @return the current istance
     * @see Factory#element(Getter, Function, Setter)
     */
    public <TMP1,TMP2> ClassMapper<T,U> addElementMapper(Getter<T,TMP1> getter, Function<TMP1,TMP2> transformer, Setter<U,TMP2> setter) {
    	return addElementMapper(Factory.element(getter,transformer,setter),false);
    }
    /**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <TMP> the type of the result of the {@code getter} and of the input of the {@code setter}.
	 * @param fieldFrom the name identifier of the getter operation
	 * @param getter the getter operation
	 * @param fieldTo the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current istance
     * @see Factory#element(String, String, Function, BiConsumer)
     */
	public <TMP> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<T,TMP> getter, String fieldTo, BiConsumer<U,TMP> setter) {
		return addElementMapper(Factory.element(fieldFrom,fieldTo,getter,setter),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param name the name identifier for both getter and setter operations used to create a default {@code FieldGetter} and {@code FieldSetter}
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
     * @return the current istance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
     * @see Factory
	 */
	public <TMP1,TMP2> ClassMapper<T,U> addCustomFieldMapper(String name, Function<TMP1,TMP2> transfom) {
		return addElementMapper(Factory.element(Factory.getter(name,from,name),transfom,Factory.setter(name,to,name)),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param fieldFrom the name identifier of the getter operation used to create a default {@code FieldGetter}
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param fieldTo the name identifier of the setter operation used to create a default {@code FieldSetter}
     * @return the current istance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
     * @see Factory
	 */
	public <TMP1,TMP2> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<TMP1,TMP2> transfom, String fieldTo) {
		return addElementMapper(Factory.element(Factory.getter(fieldFrom,from,fieldFrom),transfom,Factory.setter(fieldTo,to,fieldTo)),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param fieldFrom the name identifier of the getter operation
	 * @param getter the getter operation
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param fieldTo the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current istance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
	 */
	public <TMP1,TMP2> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<T,TMP1> getter, Function<TMP1,TMP2> transfom, String fieldTo, BiConsumer<U,TMP2> setter) {
		return addElementMapper(Factory.element(fieldFrom,fieldTo,getter,transfom,setter),false);
	}
	/**
	 * Allow to add a default value in the destination object.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param name  the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current istance
     * @see Factory#element(Getter, Setter)
	 */
	public <TMP1,TMP2> ClassMapper<T,U> addDefaultValue(String name, BiConsumer<U,TMP2> setter) {
		return addElementMapper(Factory.element(Getter.empty(),Factory.setter(name,setter)),false);
	}
	/**
	 * Allow to add a default value in the destination object.
	 * @param <TMP1> the type of the origin object
	 * @param <TMP2> the type of the destination object
	 * @param name  the name identifier of the setter operation
	 * @param value the default value to set
	 * @param setter the setter operation
     * @return the current istance
     * @see Factory#element(Getter, Function, Setter)
	 */
	public <TMP1,TMP2> ClassMapper<T,U> addDefaultValue(String name, TMP2 value, BiConsumer<U,TMP2> setter) {
		return addElementMapper(Factory.element(Getter.empty(),$->value,Factory.setter(name,setter)),false);
	}
	
	// ignore methods
	/**
	 * Allow to ignore field (both origin and destination object) during the mapping
	 * @param valuesToIgnore set of the field name or alias names to ignore 
	 * @return the current istance
	 */
	public ClassMapper<T,U> ignore(String... valuesToIgnore) {
		ignoreInputs(valuesToIgnore);
		ignoreOutputs(valuesToIgnore);
		return this;
	}
	/**
	 * Allow to ignore field from input object during the mapping
	 * @param inputsToIgnore set of the field name or alias names to ignore 
	 * @return the current istance
	 */
	public ClassMapper<T,U> ignoreInputs(String... inputsToIgnore) {
		this.inputsToIgnore.addAll(Arrays.asList(inputsToIgnore));
		isDirty = true;
		return this;
	}
	/**
	 * Allow to ignore field from destination object during the mapping
	 * @param outputsToIgnore set of the field name or alias names to ignore 
	 * @return the current istance
	 */
	public ClassMapper<T,U> ignoreOutputs(String... outputsToIgnore) {
		this.outputsToIgnore.addAll(Arrays.asList(outputsToIgnore));
		isDirty = true;
		return this;
	}
	
	
	// private methods
	private void createDefaultMappings() {
		createDefaultMappingForFields();
		this.isDirty = true;
	}
	
	private void createDefaultMappingForFields() {
		Map<String,FieldHolder> fieldsSrc = mapper.getFieldsHolderFromCache(from);
		Map<String,FieldHolder> fieldsDest = mapper.getFieldsHolderFromCache(to);
		for(String fieldName : fieldsSrc.keySet()) {
			if(fieldsDest.containsKey(fieldName)) {
				FieldHolder fieldHolderFrom = fieldsSrc.get(fieldName);
				FieldHolder fieldHolderTo = fieldsDest.get(fieldName);
				if(fieldHolderFrom.ignoreField() || fieldHolderTo.ignoreField()) {
					continue;
				}
				Class<?> srcFieldType = fieldHolderFrom.getType();
				Class<?> destFieldType = fieldHolderTo.getType();
				if(srcFieldType.isArray() && destFieldType.isArray()) {
					arrayCase(fieldHolderFrom, srcFieldType.getComponentType(), fieldHolderTo, destFieldType.getComponentType());
				}
				Class<Object> effectiveGenericTypFrom = MapperUtil.getGenericType(fieldHolderFrom.getGenericType());
				Class<Object> effectiveGenericTypDest = MapperUtil.getGenericType(fieldHolderTo.getGenericType());
				if(Collection.class.isAssignableFrom(srcFieldType) && Collection.class.isAssignableFrom(destFieldType)) {
					collectionCase(fieldHolderFrom, fieldHolderFrom.getType(), effectiveGenericTypFrom, fieldHolderTo, fieldHolderTo.getType(), effectiveGenericTypDest);
					continue;
				}
				
				Set<DirectMapper<?,?>> converters = new LinkedHashSet<>();
				converters.addAll(fieldHolderFrom.getConverters());
				converters.addAll(fieldHolderTo.getConverters());
				DirectMapper<?,?> converter = converters
							   .stream()
							   .filter(dm->dm.fromClass().isAssignableFrom(fieldHolderFrom.getType()))
							   .filter(dm->dm.toClass().isAssignableFrom(fieldHolderTo.getType()))
							   .findFirst().orElse(null);
				if(converter!=null) {
					mapFieldWithConverter(fieldName, fieldHolderFrom, fieldHolderTo, converter);
				}
				else if(mapper.hasMappingBetween(srcFieldType,destFieldType)) {
					mapFieldWithTranformation(fieldName, fieldHolderFrom, fieldHolderTo);
				}
				else if(destFieldType.isAssignableFrom(srcFieldType)) {
					Class<Object> typeFrom = effectiveGenericTypFrom;
					Class<Object> typeTo = effectiveGenericTypDest;
					if(typeFrom!=null && typeTo!=null && mapper.hasMappingBetween(typeFrom,typeTo)) {
						mapFieldWithTranformation(fieldName, fieldHolderFrom, fieldHolderTo);
					}
					Getter<T, Object> getter = Factory.getter(fieldHolderFrom);
					Setter<U, Object> setter = Factory.setter(fieldHolderTo);
					addElementMapper(Factory.element(getter,setter),true);
				}
			}
		}
	}

	private <TMP1,TMP2> void arrayCase(FieldHolder srcField, Class<TMP1> srcClass, FieldHolder destField, Class<TMP2> destClass) {
		Getter<T,TMP1[]> getter = Factory.getter(srcField);
		Function<TMP1[],TMP2[]> transformer = in->mapper.mapArray(in,destClass);
		Setter<U,TMP2[]> setter = Factory.setter(destField);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
	private <TMP1, TMP2> void collectionCase(FieldHolder srcField, Class<?> srcClass, Class<TMP1> srcGenericType,
											 FieldHolder destField, Class<?> destClass, Class<TMP2> destGenericType) {
		Getter<T,Collection<TMP1>> getter = Factory.getter(srcField);
		Function<Collection<TMP1>,Collection<TMP2>> transformer =
				in->mapper.mapCollection(in,
										 Factory.<TMP1,TMP2>collection(in.getClass(),destField.getCollectionType()),
										 destGenericType);
		Setter<U,Collection<TMP2>> setter = Factory.setter(destField);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}	
	private <TMP1,TMP2> void mapFieldWithTranformation(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder) {
		Getter<T, TMP1> getter = Factory.getter(fieldName,srcFieldHolder);
		@SuppressWarnings("unchecked")
		Function<TMP1,TMP2> transformer = in->mapper.mapOrNull(in,(Class<TMP2>)destFieldHolder.getType());
		Setter<U, TMP2> setter = Factory.setter(fieldName,destFieldHolder);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
	private <TMP1,TMP2> void mapFieldWithConverter(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder, DirectMapper<TMP1,TMP2> converter) {
		Getter<T, TMP1> getter = Factory.getter(fieldName,srcFieldHolder);
		Function<TMP1,TMP2> transformer = in->converter.mapOrNull(in);
		Setter<U, TMP2> setter = Factory.setter(fieldName,destFieldHolder);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
    
    private <T1,T2> ClassMapper<T,U> addElementMapper(ElementMapper<T,T1,T2,U> elementMapper, boolean isCalledDuringConstruction) {
    	if(isCalledDuringConstruction) {
    		this.fieldMappings.put(elementMapper.getFromValue(), elementMapper.getDestValue(),elementMapper);
    	}
    	else {
    		this.customMappings.put(elementMapper.getFromValue(), elementMapper.getDestValue(),elementMapper);
    	}
		this.isDirty = true;
		return this;
    }
    
    private List<ElementMapper<T,?,?,U>> getElementMappings() {
    	if(this.isDirty) {
    		TwoKeyMap<String,String, ElementMapper<T,?,?,U>> tmpTwoKeyMap = new TwoKeyMap<>();
    		tmpTwoKeyMap.putAll(fieldMappings);
    		tmpTwoKeyMap.putAll(customMappings);
    		Collection<ElementMapper<T,?,?,U>> tmpList = tmpTwoKeyMap.values();
    		tmpList.removeIf(em->inputsToIgnore.contains(em.getFromValue()));
    		tmpList.removeIf(em->outputsToIgnore.contains(em.getDestValue()));
    		elementMappings = new ArrayList<>(tmpList);
    		isDirty = false;
    	}
    	return elementMappings;
    }
    
}
