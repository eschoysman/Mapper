package es.utils.mapper.impl.object;

import es.utils.doublekeymap.TwoKeyMap;
import es.utils.mapper.exception.CustomException;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.CollectionFactory;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.factory.builder.From;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.utils.MapperUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
 */
public class ClassMapper<T,U> extends MapperObject<T,U> {
	
	private TwoKeyMap<Integer,Integer,ElementMapper<T,?,?,U>> fieldMappings;
	private TwoKeyMap<Integer,Integer,ElementMapper<T,?,?,U>> customMappings;
	private Set<String> inputsToIgnore;
	private Set<String> outputsToIgnore;
	
	private List<ElementMapper<T,?,?,U>> elementMappings;
	private boolean isDirty;
	
	/**
	 * Create a {@code MapperObject} from type {@code T} to type {@code U}.
	 * @param fromClass The type of the origin object
	 * @param toClass The type of the destination object
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
		U dest = mapper.createNewInstance(to);
		try {
			return mapValue(from,dest);
		} catch (Exception e) {
			throw CustomException.forType(MappingException.class).message("Error mapping input value "+from).cause(e).build();
		}
	}
	protected U mapValue(T from, U to) {
		Objects.requireNonNull(to);
		getElementMappings().forEach(me->me.apply(from,to));
		return to;
    }
	
	public void activate() {
		createDefaultMappings();
	}


	// add methods
	/**
	 * Allow to add a custom {@code ElementMapper} into the mapping between type {@code T} and {@code U}.
	 * @param <GETTER_OUT> inner type of the {@code elementMapper}, is the type returned by the getter logic
	 * @param <SETTER_IN> Inner type of the {@code elementMapper}, is the type required by the setter logic
	 * @param elementMapper The mapper for a single element of this {@code ClassMapper}
     * @return This instance
	 * @see ElementMapper
	 */
    public <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addElementMapper(ElementMapper<T,GETTER_OUT,SETTER_IN,U> elementMapper) {
    	return addElementMapper(elementMapper,false);
    }
	/**
	 * Allow to add a default value in the destination object.
	 * @param <SETTER_IN> The type of the destination object
	 * @param name  The name identifier of the setter operation
	 * @param setter The setter operation
     * @return The current instance
	 */
	public <SETTER_IN> ClassMapper<T,U> addDefaultValue(String name, BiConsumer<U,SETTER_IN> setter) {
		return addMapping().defaultOutput(name,setter).create();
	}
	
	// ignore methods
	/**
	 * Allow to ignore field (both origin and destination object) during the mapping
	 * @param valuesToIgnore Set of the field name or alias names to ignore 
	 * @return The current instance
	 */
	public ClassMapper<T,U> ignore(String... valuesToIgnore) {
		ignoreInputs(valuesToIgnore);
		ignoreOutputs(valuesToIgnore);
		return this;
	}
	/**
	 * Allow to ignore field from input object during the mapping
	 * @param inputsToIgnore Set of the field name or alias names to ignore 
	 * @return The current instance
	 */
	public ClassMapper<T,U> ignoreInputs(String... inputsToIgnore) {
		this.inputsToIgnore.addAll(Arrays.asList(inputsToIgnore));
		isDirty = true;
		return this;
	}
	/**
	 * Allow to ignore field from destination object during the mapping
	 * @param outputsToIgnore Set of the field name or alias names to ignore 
	 * @return The current instance
	 */
	public ClassMapper<T,U> ignoreOutputs(String... outputsToIgnore) {
		this.outputsToIgnore.addAll(Arrays.asList(outputsToIgnore));
		isDirty = true;
		return this;
	}
	
	/**
	 * Create a Builder for the creation of a {@link ElementMapper} for this mapping.
	 * @return The first step of the builder: {@link From}
	 * @see <a href="../../factory/builder/package-summary.html">builder package</a>
	 */
    public From<T,U> addMapping() {
    	return EMBuilder.using(mapper,this);
    }
    /**
     * Returns the {@code ElementMapper} having the same idName as {@code idNameFrom} and {@code idNameTo} if present and the name is not ignored by the mapping.<br>
     * Calling this method is the same as calling {@code getMapping(idNameFromTo,idNameFromTo)}
     * @param <GETTER_OUT> the type returned by the getter
     * @param <SETTER_IN> the type required by the setter
     * @param idNameFromTo the name identifier of the {@code getter} and {@code setter} associated to the {@code ElementMapper} to return
     * @return The {@code ElementMapper} if present and {@code idNameFromTo} is not ignored
     */
    public <GETTER_OUT,SETTER_IN> Optional<ElementMapper<T,GETTER_OUT,SETTER_IN,U>> getMapping(String idNameFromTo) {
    	return getMapping(idNameFromTo,idNameFromTo);
    }
    /**
     * Returns the {@code ElementMapper} between the names {@code idNameFrom} and {@code idNameTo} if present and both names are not ignored by the mapping
     * @param <GETTER_OUT> the type returned by the getter
     * @param <SETTER_IN> the type required by the setter
     * @param idNameFrom the name identifier of the {@code setter} associated to the {@code ElementMapper} to return
     * @param idNameTo the name identifier of the {@code getter} associated to the {@code ElementMapper} to return
     * @return The {@code ElementMapper} if present and {@code from} and {@code to} are not ignored
     */
    public <GETTER_OUT,SETTER_IN> Optional<ElementMapper<T,GETTER_OUT,SETTER_IN,U>> getMapping(String idNameFrom, String idNameTo) {
    	if(inputsToIgnore.contains(idNameFrom) || outputsToIgnore.contains(idNameTo)) {
    		return Optional.empty();
    	}
		LinkedList<ElementMapper<T,?,?,U>> mappings = new LinkedList<>();
		mappings.addAll(fieldMappings.values());
    	mappings.addAll(customMappings.values());
		return mappings.stream()
					   .filter(em->em.getFromName().equals(idNameFrom) && em.getDestName().equals(idNameTo))
					   .map(em->(ElementMapper<T,GETTER_OUT,SETTER_IN,U>)em)
					   .findAny();
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
					arrayCase(fieldHolderFrom, fieldHolderTo, destFieldType.getComponentType());
				}
				Class<Object> effectiveGenericTypeDest = MapperUtil.getGenericType(fieldHolderTo.getGenericType());
				if(Collection.class.isAssignableFrom(srcFieldType) && Collection.class.isAssignableFrom(destFieldType)) {
					collectionCase(fieldHolderFrom, fieldHolderTo, fieldHolderTo.getType(), effectiveGenericTypeDest);
					continue;
				}
				
				Set<DirectMapper<?,?>> converters = new LinkedHashSet<>();
				converters.addAll(fieldHolderFrom.getConverters());
				converters.addAll(fieldHolderTo.getConverters());
				DirectMapper<?,?> converter = converters
							   .stream()
							   .filter(dm->dm.fromClass().isAssignableFrom(fieldHolderFrom.getType()))
							   .filter(dm->dm.toClass().isAssignableFrom(fieldHolderTo.getType()))
							   .findFirst()
							   .orElse(null);
				if(converter!=null) {
					mapFieldWithConverter(fieldName, fieldHolderFrom, fieldHolderTo, converter);
				}
				else if(mapper.hasMappingBetween(srcFieldType,destFieldType)) {
					mapFieldWithTranformation(fieldName, fieldHolderFrom, fieldHolderTo);
				}
				else if(destFieldType.isAssignableFrom(srcFieldType)) {
					addElementMapper(addMapping().from(fieldHolderFrom).defaultOutput(fieldHolderTo.getDefautValueSupplier()).to(fieldHolderTo).getElementMapper(),true);
				}
			}
		}
	}

	private <GETTER_OUT,SETTER_IN> void arrayCase(FieldHolder srcField, FieldHolder destField, Class<SETTER_IN> destClass) {
		addElementMapper(addMapping().<GETTER_OUT[]>from(srcField).<SETTER_IN[]>transform(in->mapper.mapArray(in,destClass)).to(destField).getElementMapper(),true);
	}
	private <GETTER_OUT, SETTER_IN> void collectionCase(FieldHolder srcField, FieldHolder destField, Class<?> destClass, Class<SETTER_IN> destGenericType) {
		Function<Collection<GETTER_OUT>,Collection<SETTER_IN>> transformer =
				in->in==null ? null : mapper.mapCollection(in,
										 CollectionFactory.<GETTER_OUT,SETTER_IN>create(in.getClass(),destField.getCollectionType()),
										 destGenericType);
		addElementMapper(addMapping().<Collection<GETTER_OUT>>from(srcField).<Collection<SETTER_IN>>transform(transformer::apply).to(destField).getElementMapper(),true);
	}	
	private <GETTER_OUT,SETTER_IN> void mapFieldWithTranformation(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder) {
		@SuppressWarnings("unchecked")
		Function<GETTER_OUT,SETTER_IN> transformer = in->mapper.mapAsOptional(in,(Class<SETTER_IN>)destFieldHolder.getType()).orElse(null);
		addElementMapper(addMapping().<GETTER_OUT>from(fieldName,srcFieldHolder).<SETTER_IN>transform(transformer::apply).defaultOutput(destFieldHolder.getDefautValueSupplier()).to(fieldName,destFieldHolder).getElementMapper(),true);
	}
	private <GETTER_OUT,SETTER_IN> void mapFieldWithConverter(String fieldName, FieldHolder srcFieldHolder, FieldHolder destFieldHolder, DirectMapper<GETTER_OUT,SETTER_IN> converter) {
		addElementMapper(addMapping().<GETTER_OUT>from(fieldName,srcFieldHolder).transform(converter::mapOrNull).defaultOutput(destFieldHolder.getDefautValueSupplier()).to(fieldName,destFieldHolder).getElementMapper(),true);
	}
    
    private <GETTER_OUT,SETTER_IN> ClassMapper<T,U> addElementMapper(ElementMapper<T,GETTER_OUT,SETTER_IN,U> elementMapper, boolean isCalledDuringConstruction) {
    	if(isCalledDuringConstruction) {
    		this.fieldMappings.put(elementMapper.getFromId(), elementMapper.getDestId(),elementMapper);
    	}
    	else {
    		this.customMappings.put(elementMapper.getFromId(), elementMapper.getDestId(),elementMapper);
    	}
		this.isDirty = true;
		return this;
    }
    
    private List<ElementMapper<T,?,?,U>> getElementMappings() {
    	if(this.isDirty) {
    		TwoKeyMap<Integer,Integer, ElementMapper<T,?,?,U>> tmpTwoKeyMap = new TwoKeyMap<>();
    		tmpTwoKeyMap.putAll(fieldMappings);
    		tmpTwoKeyMap.putAll(customMappings);
    		Collection<ElementMapper<T,?,?,U>> tmpList = tmpTwoKeyMap.values();
    		tmpList.removeIf(em->inputsToIgnore.contains(em.getFromName()));
    		tmpList.removeIf(em->outputsToIgnore.contains(em.getDestName()));
    		elementMappings = new ArrayList<>(tmpList);
    		this.isDirty = false;
    	}
    	return elementMappings;
    }
    
}
