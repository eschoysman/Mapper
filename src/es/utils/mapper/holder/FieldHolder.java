package es.utils.mapper.holder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.IgnoreField;

/**
 * 
 * @author Emmanuel
 *
 */
public class FieldHolder {

	private static final String FIELD_NAME_REGEX = "[a-zA-Z_$][a-zA-Z_$0-9]*";
	
	private Field field;
	private String fieldName;
	private Set<String> aliases;
	private Class<?> type;
	private Type genericType;
	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> collectionType;
	private boolean ignoreField;
	
	/**
	 * 
	 * @param field
	 */
	public FieldHolder(Field field) {
		this.field = Objects.requireNonNull(field);
		this.fieldName = field.getName();
//		this.fieldClass = field.getDeclaringClass();
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
		this.aliases = aliases;
	}
	private void processCollectionType() {
		CollectionType annotationCollectionType = this.field.getAnnotation(CollectionType.class);
		if(annotationCollectionType!=null) {
			this.collectionType = annotationCollectionType.value();
		}
	}

	/**
	 * 
	 * @return
	 */
	public Field getField() {
		return field;
	}

	/**
	 * 
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	/**
	 * 
	 * @return
	 */
	public Set<String> getAllNames() {
		Set<String> allNames = new TreeSet<>(getAliases());
		allNames.add(fieldName);
		return allNames;
	}
	
	/**
	 * 
	 * @return
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * 
	 * @return
	 */
	public Type getGenericType() {
		return genericType;
	}

	/**
	 * 
	 * @return
	 */
	public boolean ignoreField() {
		return this.ignoreField;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends Collection> getCollectionType() {
		return collectionType;
	}
	
}
