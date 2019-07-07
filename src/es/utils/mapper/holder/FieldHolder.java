package es.utils.mapper.holder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.IgnoreField;
import es.utils.mapper.converter.Converter;
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
	 * 
	 * @param field the field used to hold
	 */
	public FieldHolder(Field field) {
		this.field = Objects.requireNonNull(field);
		this.fieldName = field.getName();
		this.type = field.getType();
		this.genericType = field.getGenericType();
		readAnnotations();
	}
	
	private void readAnnotations() {
		processIgnoreField();
		processAliasNames();
		processCollectionType();
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
	private void processAliasNames() {
		Set<String> aliases = new TreeSet<>();
		AliasNames alias = this.field.getAnnotation(AliasNames.class);
		if(alias!=null) {
			for(String aliasName : alias.value())
				if(aliasName.matches(FIELD_NAME_REGEX)) {
					aliases.add(aliasName);
				}
		}
		Set<DirectMapper<?,?>> converters = new LinkedHashSet<>();
		AliasNames converter = this.field.getAnnotation(AliasNames.class);
		if(converter!=null) {
			for(Class<? extends Converter<?,?>> conv : converter.converter()) {
				try {
					converters.add(conv.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					System.out.println("WARNING - The converter for "+conv+" does not have a empty contructer. Converter ignored");
				}
			}
		}
		this.aliases = aliases;
		this.converters = converters;
	}
	private void processCollectionType() {
		CollectionType annotationCollectionType = this.field.getAnnotation(CollectionType.class);
		if(annotationCollectionType!=null) {
			this.collectionType = annotationCollectionType.value();
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
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	/**
	 * @return the aliases of the field
	 * @see AliasNames
	 */
	public Set<DirectMapper<?,?>> getConverters() {
		return converters;
	}
	/**
	 * @return the name and all the aliases of the field
	 * @see AliasNames
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
