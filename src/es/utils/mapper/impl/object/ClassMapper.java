package es.utils.mapper.impl.object;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import es.utils.mapper.factory.CollectionFactory;
import es.utils.mapper.factory.ElementMapperFactory;
import es.utils.mapper.factory.GetterFactory;
import es.utils.mapper.factory.SetterFactory;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;

public class ClassMapper<T,U> extends MapperObject<T,U> {
	
	private Mapper mapper;
	
	private TwoKeyMap<String,String,ElementMapper<T,?,?,U>> fieldMappings;
	private TwoKeyMap<String,String,ElementMapper<T,?,?,U>> customMappings;
	private Set<String> inputsToIgnore;
	private Set<String> outputsToIgnore;
	
	private List<ElementMapper<T,?,?,U>> elementMappings;
	private boolean isDirty;
	
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
    public <T1,T2> void addElementMapper(ElementMapper<T,T1,T2,U> elementMapper) {
    	addElementMapper(elementMapper,false);
    }
    public <TMP> void addElementMapper(Getter<T,TMP> getter, Setter<U,TMP> setter) {
    	addElementMapper(ElementMapperFactory.create(getter,setter),false);
    }
    public <T1,T2> void addElementMapper(Getter<T,T1> getter, Function<T1,T2> transformer, Setter<U,T2> setter) {
    	addElementMapper(ElementMapperFactory.create(getter,transformer,setter),false);
    }
	public <TMP> void addCustomFieldMapper(String fieldFrom, Function<T,TMP> getter, String fieldTo, BiConsumer<U,TMP> setter) {
		addElementMapper(ElementMapperFactory.create(fieldFrom,fieldTo,getter,setter),false);
	}
	public <TMP1,TMP2> void addCustomFieldMapper(String fieldFrom, Function<T,TMP1> getter, Function<TMP1,TMP2> transfom, String fieldTo, BiConsumer<U,TMP2> setter) {
		addElementMapper(ElementMapperFactory.create(fieldFrom,fieldTo,getter,transfom,setter),false);
	}
	public <TMP1,TMP2> void addDefaultValue(String name, BiConsumer<U,TMP2> setter) {
		addElementMapper(ElementMapperFactory.create(Getter.empty(),SetterFactory.setter(name,setter)),false);
	}
	
	// ignore methods
	public ClassMapper<T,U> ignore(String... valuesToIgnore) {
		ignoreInputs(valuesToIgnore);
		ignoreOutputs(valuesToIgnore);
		return this;
	}
	public ClassMapper<T,U> ignoreInputs(String... inputsToIgnore) {
		this.inputsToIgnore.addAll(Arrays.asList(inputsToIgnore));
		isDirty = true;
		return this;
	}
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
		Map<String,FieldHolder> fieldsSrc = getAllFields(from);
		Map<String,FieldHolder> fieldsDest = getAllFields(to);
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
					Getter<T, Object> getter = GetterFactory.getter(fieldHolderFrom);
					Setter<U, Object> setter = SetterFactory.setter(fieldHolderTo);
					addElementMapper(ElementMapperFactory.create(getter,setter),true);
				}
			}
		}
	}

	private <TMP1,TMP2> void arrayCase(FieldHolder srcField, Class<TMP1> srcClass, FieldHolder destField, Class<TMP2> destClass) {
		Getter<T,TMP1[]> getter = GetterFactory.getter(srcField);
		Function<TMP1[],TMP2[]> transformer = in->mapper.mapArray(in,destClass);
		Setter<U,TMP2[]> setter = SetterFactory.setter(destField);
		addElementMapper(ElementMapperFactory.create(getter,transformer,setter),true);
	}
	private <TMP1, TMP2> void collectionCase(FieldHolder srcField, Class<?> srcClass, Class<TMP1> srcGenericType,
											 FieldHolder destField, Class<?> destClass, Class<TMP2> destGenericType) {
		Getter<T,Collection<TMP1>> getter = GetterFactory.getter(srcField);
		Function<Collection<TMP1>,Collection<TMP2>> transformer =
				in->mapper.mapCollection(in,
										 CollectionFactory.<TMP1,TMP2>create(in.getClass(),destField.getCollectionType()),
										 destGenericType);
		Setter<U,Collection<TMP2>> setter = SetterFactory.setter(destField);
		addElementMapper(ElementMapperFactory.create(getter,transformer,setter),true);
	}	
	private <TMP1,TMP2> void mapFieldWithTranformation(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder) {
		Getter<T, TMP1> getter = GetterFactory.getter(fieldName,srcFieldHolder);
		@SuppressWarnings("unchecked")
		Function<TMP1,TMP2> transformer = in->mapper.mapOrNull(in,(Class<TMP2>)destFieldHolder.getType());
		Setter<U, TMP2> setter = SetterFactory.setter(fieldName,destFieldHolder);
		addElementMapper(ElementMapperFactory.create(getter,transformer,setter),true);
	}

	private Map<String,FieldHolder> getAllFields(Class<?> type) {
    	Map<String,FieldHolder> result = new HashMap<>();
    	for(Field field : MapperUtil.getAllFields(type)) {
    		FieldHolder fieldHolder = new FieldHolder(field);
    		for(String name : fieldHolder.getAllNames()) {
    			FieldHolder prevValue = result.put(name,fieldHolder);
	    		if(prevValue!=null) {
	    			try {
						throw new MappingException("Two Fields have the same name or alias "+fieldHolder.getFieldName());
					} catch (MappingException e) {
						e.printStackTrace();
					}
	    		}
    		}
    	}
    	return result;
    }
    
    private <T1,T2> void addElementMapper(ElementMapper<T,T1,T2,U> elementMapper, boolean isCalledDuringConstruction) {
    	if(isCalledDuringConstruction) {
    		this.fieldMappings.put(elementMapper.getFromValue(), elementMapper.getDestValue(),elementMapper);
    	}
    	else {
    		this.customMappings.put(elementMapper.getFromValue(), elementMapper.getDestValue(),elementMapper);
    	}
		this.isDirty = true;
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
