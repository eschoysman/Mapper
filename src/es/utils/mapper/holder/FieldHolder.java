package es.utils.mapper.holder;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.Converter;
import es.utils.mapper.annotation.IgnoreField;
import es.utils.mapper.configuration.Configuration;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.defaultvalue.DefaultValueStrategy;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.utils.MapperUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

/**
 * A wrapper object to hold a Field and its information (name, aliases, type etc...).
 * @author eschoysman
 *
 */
public class FieldHolder {

	private static final String FIELD_NAME_REGEX = "[a-zA-Z_$][a-zA-Z_$0-9]*";
	
	private final Field field;
	private final String fieldName;
	private final Set<String> aliases;
	private final Set<DirectMapper<?,?>> converters;
	private Supplier<?> defaultValue;
	private final Class<?> rawType;
	private final Class<?> wrappedType;
	private final Type genericType;
	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> collectionType;
	private boolean ignoreField;
	
	/**
	 * @param field the field used to hold
	 * @param config the configuration of the belonging Mapper
	 */
	public FieldHolder(Field field, Configuration config) {
		this.field = Objects.requireNonNull(field);
		Objects.requireNonNull(config);
		this.fieldName = field.getName();
		this.rawType = field.getType();
		this.wrappedType = MapperUtil.getWrapType(field.getType());
		this.genericType = field.getGenericType();
		this.aliases = new TreeSet<>();
		this.converters = new LinkedHashSet<>();
		this.defaultValue = ()->null;
		processAnnotations(config);
	}
	
	private void processAnnotations(Configuration config) {
		processIgnoreField();
		if(!this.ignoreField) {
			processAliases(config);
			processConverters(config);
			processDefaultValue(config);
			processCollectionType();
		}
	}

	private void processIgnoreField() {
		this.ignoreField = this.field.getAnnotation(IgnoreField.class)!=null;
		if(this.ignoreField) {
			return;
		}
		IgnoreField classLevelAnnotation = this.field.getDeclaringClass().getAnnotation(IgnoreField.class);
		if(classLevelAnnotation!=null) {
			this.ignoreField = Arrays.asList(classLevelAnnotation.value()).contains(this.fieldName);
		}
	}
	private void processAliases(Configuration config) {
		Annotation[] annotations = field.getAnnotations();
		for(Annotation annotation : annotations) {
			String name = config.getAnnotationField(annotation.annotationType());
			if(name!=null) {
				try {
					Method method = annotation.annotationType().getMethod(name);
					Object value = method.invoke(annotation);
					if(value.getClass().isArray()) {
						for(String aliasName : (String[])value) {
							addAlias(aliasName);
						}
					}
					else {
						addAlias((String)value);
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}
		}
	}
	private void processConverters(Configuration config) {
		Converter[] converter = this.field.getAnnotationsByType(Converter.class);
		for(Converter conv : converter) {
			@SuppressWarnings("unchecked")
			Class<? extends AbstractConverter<Object,Object>> converterCasted = (Class<? extends AbstractConverter<Object,Object>>)conv.value();
			DirectMapper<?,?> converterInstance = MapperUtil.createFromConverter(converterCasted,config.getMapper());
			if(converterInstance!=null)
				converters.add(converterInstance);
		}
	}
	private void processDefaultValue(Configuration config) {
		this.defaultValue = new DefaultValueManager(config,this).getSupplier();
	}
	private void processCollectionType() {
		CollectionType annotationCollectionType = this.field.getAnnotation(CollectionType.class);
		if(annotationCollectionType!=null) {
			this.collectionType = annotationCollectionType.value();
		}
	}
	
	private void addAlias(String aliasName) {
		if(aliasName.matches(FIELD_NAME_REGEX)) {
			aliases.add(aliasName);
		}
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the aliases of the field
	 * @see AliasNames
	 * @see Configuration#useAnnotation(Class, String)
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	/**
	 * @return the aliases of the field
	 * @see Converter
	 */
	public Set<DirectMapper<?,?>> getConverters() {
		return converters;
	}
	/**
	 * @param <TYPE> the type of the value supplied
	 * @return the default value supplier of the field
	 * @see DefaultValueStrategy
	 */
	public <TYPE> Supplier<TYPE> getDefautValueSupplier() {
		@SuppressWarnings("unchecked")
		Supplier<TYPE> result = (Supplier<TYPE>)defaultValue;
		return result;
	}
	/**
	 * @return the name and all the aliases of the field
	 * @see AliasNames
	 * @see Configuration#useAnnotation(Class, String)
	 */
	public Set<String> getAllNames() {
		Set<String> allNames = new TreeSet<>(getAliases());
		allNames.add(fieldName);
		return allNames;
	}

	/**
	 * @return the type of the field. Only if the raw type is primitive, the type will be wrapped.
	 */
	public Class<?> getType() {
		return wrappedType;
	}

	/**
	 * @return the generic type of the field
	 */
	public Type getGenericType() {
		return genericType;
	}

	/**
	 * @return {@code true} if the field must be ignored
	 * @see IgnoreField
	 */
	public boolean ignoreField() {
		return this.ignoreField;
	}
	
	/**
	 * @return the sub-collection type
	 * @see CollectionType
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends Collection> getCollectionType() {
		return collectionType;
	}
	
}
