package es.utils.mapper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import es.utils.mapper.annotation.AliasNames;

/**
 * This class contains a set of method used in the mapping creation.
 * @author eschoysman
 */
public class MapperUtil {

	/**
	 * This method returns the specific field of input class. If no field is found,
	 * an attempt to get the field through the {@code AliasNames} annotation is done.
	 * @param <T> type of the class
	 * @param type The class you're searching the field into.
	 * @param fieldName The field you're searching for.
	 * @return The Field found. If no result is found, null is returned.
	 * @see AliasNames
	 */
	public static <T> Field getField(Class<T> type, String fieldName) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(fieldName);
		// TODO add support to Configuration.addAnnotation
//		Mapper mapper = new Mapper();
//		return mapper.getFieldsHolderFromCache(type).values().stream()
//					 .filter(fieldHolder->fieldHolder.getAliases().contains(fieldName))
//					 .map(FieldHolder::getField)
//					 .findFirst().orElse(null);
		Field result = getDeclaredField(type,fieldName);
		if(result==null) result = getFieldOfClass(type,fieldName);
		if(result==null) result = getFieldOfClassByAnnotation(type,fieldName);
		return result;
	}
	
	/**
	 * This method returns all fields from given class, private and public ones.
	 * @param type The class you should inspect.
	 * @return A Set of Fields of the given class.
	 */
	public static Set<Field> getAllFields(Class<?> type) {
		Field[] fields1 = type.getFields();
		Field[] fields2 = type.getDeclaredFields();
		Set<Field> fields = new LinkedHashSet<>(fields1.length+fields2.length);
		for(Field f : fields1) fields.add(f);
		for(Field f : fields2) fields.add(f);
		return fields;
	}
	
	/**
	 * This method returns the generic class of an input Type with generic. In case of extension,
	 * the superclass Type is returned.
	 * @param <TYPE> generic type returned 
	 * @param input The type you're trying to get the class of.
	 * @return the Class of the given Type. If no generic type is defined, null is returned.
	 */
	public static <TYPE> Class<TYPE> getGenericType(Type input) {
		if(input!=null && input instanceof ParameterizedType) {
		    ParameterizedType paramType = (ParameterizedType)input;
		    Type[] argTypes = paramType.getActualTypeArguments();
		    if(argTypes.length>0) {
		    	String typeName = argTypes[0].getTypeName();
		    	Pattern pattern = Pattern.compile(".+? extends ");
		    	if(pattern.matcher(typeName).find())
		    		typeName = typeName.substring(typeName.indexOf("extends")+8);	// TODO caso "? extends Oggetto<T>"?
		    	try {
			    	@SuppressWarnings("unchecked")
					Class<TYPE> resultClass = (Class<TYPE>)Class.forName(typeName);
					return resultClass;
		    	} catch (Exception e) {
		    		return null;
				}
		    }
		}
		return null;
	}

	private static <T> Field getDeclaredField(Class<T> type, String fieldName) {
		try {
			return type.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}
	private static <T> Field getFieldOfClass(Class<T> type, String fieldName) {
		try {
			return type.getField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}
	private static <T> Field getFieldOfClassByAnnotation(Class<T> type, String fieldName) {
		Field[] fields = type.getFields();
		for(Field field : fields) {
			boolean toReturn =  Optional.ofNullable(field.getAnnotation(AliasNames.class))
										.map(AliasNames::value)
										.map(Arrays::asList)
										.map(l->l.contains(fieldName))
										.orElse(false);
			if(toReturn)  {
				return field;
			}
		}
		return null;
	}
	
}
