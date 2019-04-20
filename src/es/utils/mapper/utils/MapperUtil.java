package es.utils.mapper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import es.utils.mapper.annotation.AliasNames;

public class MapperUtil {

	public static <T> Field getField(Class<T> clazz, String fieldName) {
		Objects.requireNonNull(clazz);
		Objects.requireNonNull(fieldName);
		Field result = getDeclaredField(clazz,fieldName);
		if(result==null) result = getFieldOfClass(clazz,fieldName);
		if(result==null) result = getFieldOfClassByAnnotation(clazz,fieldName);
		return result;
	}
	
	public static List<Field> getAllFields(Class<?> clazz) {
		Field[] fields1 = clazz.getFields();
		Field[] fields2 = clazz.getDeclaredFields();
		List<Field> fields = new ArrayList<>(fields1.length+fields2.length);
		for(Field f : fields1) fields.add(f);
		for(Field f : fields2) fields.add(f);
		return fields;
	}
	
	public static <TYPE> Class<TYPE> getGenericType(Type input) {
		if(input!=null && input instanceof ParameterizedType) {
		    ParameterizedType paramType = (ParameterizedType)input;
		    Type[] argTypes = paramType.getActualTypeArguments();
		    if(argTypes.length>0) {
		    	String typeName = argTypes[0].getTypeName();
		    	Pattern pattern = Pattern.compile(".+? extends ");
		    	if(pattern.matcher(typeName).find())
		    		typeName = typeName.substring(typeName.indexOf("extends")+8);
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

	private static <T> Field getDeclaredField(Class<T> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}
	private static <T> Field getFieldOfClass(Class<T> clazz, String fieldName) {
		try {
			return clazz.getField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}
	private static <T> Field getFieldOfClassByAnnotation(Class<T> clazz, String fieldName) {
		Field[] fields = clazz.getFields();
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
