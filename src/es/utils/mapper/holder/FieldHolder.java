package es.utils.mapper.holder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.Converter;
import es.utils.mapper.annotation.IgnoreField;
import es.utils.mapper.configuration.Configuration;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.impl.object.DirectMapper;

/**
 * A wrapper object to hold a Field and its information (name, aliases, type etc...).
 * @author eschoysman
 *
 */
public class FieldHolder {

	private static final String FIELD_NAME_REGEX = "[a-zA-Z_$][a-zA-Z_$0-9]*";
	
	private Field field;
	private String fieldName;
	private Set<String> aliases;
	private Set<DirectMapper<?,?>> converters;
	private Class<?> type;
	private Type genericType;
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
		this.type = field.getType();
		this.genericType = field.getGenericType();
		this.aliases = new TreeSet<>();
		this.converters = new LinkedHashSet<>();
		processAnnotations(config);
	}
	
	private void processAnnotations(Configuration config) {
		processIgnoreField();
		if(!this.ignoreField) {
			processAliases(config);
			processConverters();
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
	private void processConverters() {
		Converter converter = this.field.getAnnotation(Converter.class);
		if(converter!=null) {
			for(Class<? extends AbstractConverter<?,?>> conv : converter.value()) {
				try {
					converters.add(conv.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					System.out.println("WARNING - The converter for "+conv+" does not have a empty public contructor; the converter is ignored.");
				}
			}
		}
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
	 * @see Configuration#addAnnotation(Class, String)
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
	 * @return the name and all the aliases of the field
	 * @see AliasNames
	 * @see Configuration#addAnnotation(Class, String)
	 */
	public Set<String> getAllNames() {
		Set<String> allNames = new TreeSet<>(getAliases());
		allNames.add(fieldName);
		return allNames;
	}
	
	/**
	 * @return the type of the field
	 */
	public Class<?> getType() {
		return type;
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
