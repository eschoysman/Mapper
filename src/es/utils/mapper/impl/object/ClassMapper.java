package es.utils.mapper.impl.object;

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
		U dest = null;
		try {
			dest = mapper.createNewInstance(to);
		} catch(NullPointerException e) {
			throw new MappingException("The build method on the belonging Mapper has not been invoked yet.", e);
		}
		return mapValue(from,dest);
	}
	protected U mapValue(T from, U to) {
		Objects.requireNonNull(to);
		getElementMappings().forEach(me->me.apply(from,to,mapper.getConfig()));
		return to;
    }
	
	public void activate(Mapper mapper) {
		super.activate(mapper);
		createDefaultMappings();
	}


	// add methods
	/**
	 * @param <GETTER_OUT> inner type of the {@code elementMapper}, is the type returned by the getter logic
	 * @param <SETTER_IN> inner type of the {@code elementMapper}, is the type required by the setter logic
	 * Allow to add a custom {@code ElementMapper} into the mapping between type {@code T} and {@code U}.
	 * @param elementMapper the mapper for a single element of this {@code ClassMapper}
     * @return this instance
	 * @see ElementMapper
	 */
    public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addElementMapper(ElementMapper<T,GETTER_OUT,SETTER_IN,U> elementMapper) {
    	return addElementMapper(elementMapper,false);
    }
    /**
	 * Allow to add a custom mapping into the mapping between type {@code T} and {@code U} using a {@code Getter} and a {@code Setter}.
	 * @param <TYPE> the type of the result of the {@code getter} and of the input of the {@code setter}.
     * @param getter {@code Getter} instance to retrieve the value to map
     * @param setter {@code Setter} instance to set the mapped value
     * @return the current instance
     * @see Factory#element(Getter, Setter)
     */
    public <TYPE> ClassMapper<T,U> addElementMapper(Getter<T,TYPE> getter, Setter<U,TYPE> setter) {
    	return addElementMapper(Factory.element(getter,setter),false);
    }
    /**
	 * Allow to add a custom mapping into the mapping between type {@code T} and {@code U} using a {@code Getter}, a transformer and a {@code Setter}.
	 * @param <GETTER_OUT> the type of the origin object
	 * @param <SETTER_IN> the type of the destination object
	 * @param getter {@code Getter} instance to retrieve the value to map
     * @param transformer the function to map the result of the {@code getter} into the type required by the {@code setter}
     * @param setter {@code Setter} instance to set the mapped value
     * @return the current instance
     * @see Factory#element(Getter, Function, Setter)
     */
    public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addElementMapper(Getter<T,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transformer, Setter<U,SETTER_IN> setter) {
    	return addElementMapper(Factory.element(getter,transformer,setter),false);
    }
    /**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <TYPE> the type of the result of the {@code getter} and of the input of the {@code setter}.
	 * @param fieldFrom the name identifier of the getter operation
	 * @param getter the getter operation
	 * @param fieldTo the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current instance
     * @see Factory#element(String, String, Function, BiConsumer)
     */
	public <TYPE> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<T,TYPE> getter, String fieldTo, BiConsumer<U,TYPE> setter) {
		return addElementMapper(Factory.element(fieldFrom,fieldTo,getter,setter),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <GETTER_OUT> the type of the origin object
	 * @param <SETTER_IN> the type of the destination object
	 * @param name the name identifier for both getter and setter operations used to create a default {@code FieldGetter} and {@code FieldSetter}
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
     * @return the current instance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
     * @see Factory
	 */
	public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addCustomFieldMapper(String name, Function<GETTER_OUT,SETTER_IN> transfom) {
		return addElementMapper(Factory.element(Factory.getter(name,from,name),transfom,Factory.setter(name,to,name)),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <GETTER_OUT> the type of the origin object
	 * @param <SETTER_IN> the type of the destination object
	 * @param fieldFrom the name identifier of the getter operation used to create a default {@code FieldGetter}
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param fieldTo the name identifier of the setter operation used to create a default {@code FieldSetter}
     * @return the current instance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
     * @see Factory
	 */
	public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<GETTER_OUT,SETTER_IN> transfom, String fieldTo) {
		return addElementMapper(Factory.element(Factory.getter(fieldFrom,from,fieldFrom),transfom,Factory.setter(fieldTo,to,fieldTo)),false);
	}
	/**
	 * Allow to add a custom {@code ElementMapper} between type {@code T} and {@code U}.
	 * @param <GETTER_OUT> the type of the origin object
	 * @param <SETTER_IN> the type of the destination object
	 * @param fieldFrom the name identifier of the getter operation
	 * @param getter the getter operation
	 * @param transfom a function that maps the result of the {@code getter} into the correct type for the {@code setter}
	 * @param fieldTo the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current instance
     * @see Factory#element(String, String, Function, Function, BiConsumer)
	 */
	public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addCustomFieldMapper(String fieldFrom, Function<T,GETTER_OUT> getter, Function<GETTER_OUT,SETTER_IN> transfom, String fieldTo, BiConsumer<U,SETTER_IN> setter) {
		return addElementMapper(Factory.element(fieldFrom,fieldTo,getter,transfom,setter),false);
	}
	/**
	 * Allow to add a default value in the destination object.
	 * @param <SETTER_IN> the type of the destination object
	 * @param name  the name identifier of the setter operation
	 * @param setter the setter operation
     * @return the current instance
     * @see Factory#element(Getter, Setter)
	 */
	public <SETTER_IN> ClassMapper<T,U> addDefaultValue(String name, BiConsumer<U,SETTER_IN> setter) {
		return addElementMapper(Factory.element(Getter.empty(),Factory.setter(name,setter)),false);
	}
	/**
	 * Allow to add a default value in the destination object.
	 * @param <SETTER_IN> the type of the destination object
	 * @param name  the name identifier of the setter operation
	 * @param value the default value to set
	 * @param setter the setter operation
     * @return the current instance
     * @see Factory#element(Getter, Function, Setter)
	 */
	public <SETTER_IN> ClassMapper<T,U> addDefaultValue(String name, SETTER_IN value, BiConsumer<U,SETTER_IN> setter) {
		return addElementMapper(Factory.element(Getter.empty(),$->value,Factory.setter(name,setter)),false);
	}
	
	// ignore methods
	/**
	 * Allow to ignore field (both origin and destination object) during the mapping
	 * @param valuesToIgnore set of the field name or alias names to ignore 
	 * @return the current instance
	 */
	public ClassMapper<T,U> ignore(String... valuesToIgnore) {
		ignoreInputs(valuesToIgnore);
		ignoreOutputs(valuesToIgnore);
		return this;
	}
	/**
	 * Allow to ignore field from input object during the mapping
	 * @param inputsToIgnore set of the field name or alias names to ignore 
	 * @return the current instance
	 */
	public ClassMapper<T,U> ignoreInputs(String... inputsToIgnore) {
		this.inputsToIgnore.addAll(Arrays.asList(inputsToIgnore));
		isDirty = true;
		return this;
	}
	/**
	 * Allow to ignore field from destination object during the mapping
	 * @param outputsToIgnore set of the field name or alias names to ignore 
	 * @return the current instance
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

	private <GETTER_OUT,SETTER_IN> void arrayCase(FieldHolder srcField, Class<GETTER_OUT> srcClass, FieldHolder destField, Class<SETTER_IN> destClass) {
		Getter<T,GETTER_OUT[]> getter = Factory.getter(srcField);
		Function<GETTER_OUT[],SETTER_IN[]> transformer = in->mapper.mapArray(in,destClass);
		Setter<U,SETTER_IN[]> setter = Factory.setter(destField);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
	private <GETTER_OUT, SETTER_IN> void collectionCase(FieldHolder srcField, Class<?> srcClass, Class<GETTER_OUT> srcGenericType,
											 FieldHolder destField, Class<?> destClass, Class<SETTER_IN> destGenericType) {
		Getter<T,Collection<GETTER_OUT>> getter = Factory.getter(srcField);
		Function<Collection<GETTER_OUT>,Collection<SETTER_IN>> transformer =
				in->mapper.mapCollection(in,
										 Factory.<GETTER_OUT,SETTER_IN>collection(in.getClass(),destField.getCollectionType()),
										 destGenericType);
		Setter<U,Collection<SETTER_IN>> setter = Factory.setter(destField);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}	
	private <GETTER_OUT,SETTER_IN> void mapFieldWithTranformation(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder) {
		Getter<T, GETTER_OUT> getter = Factory.getter(fieldName,srcFieldHolder);
		@SuppressWarnings("unchecked")
		Function<GETTER_OUT,SETTER_IN> transformer = in->mapper.mapOrNull(in,(Class<SETTER_IN>)destFieldHolder.getType());
		Setter<U, SETTER_IN> setter = Factory.setter(fieldName,destFieldHolder);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
	private <GETTER_OUT,SETTER_IN> void mapFieldWithConverter(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder, DirectMapper<GETTER_OUT,SETTER_IN> converter) {
		Getter<T, GETTER_OUT> getter = Factory.getter(fieldName,srcFieldHolder);
		Function<GETTER_OUT,SETTER_IN> transformer = converter::mapOrNull;
		Setter<U, SETTER_IN> setter = Factory.setter(fieldName,destFieldHolder);
		addElementMapper(Factory.element(getter,transformer,setter),true);
	}
    
    private <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addElementMapper(ElementMapper<T,GETTER_OUT,SETTER_IN,U> elementMapper, boolean isCalledDuringConstruction) {
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
