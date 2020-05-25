package es.utils.mapper.utils;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.object.DirectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

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
		Field result = getDeclaredField(type,fieldName);
		if(result==null) result = getFieldOfClass(type,fieldName);
		return result;
	}
	/**
	 * This method returns the specific field of input class. If no field is found,
	 * an attempt to get the field through the {@code AliasNames} annotation is done.
	 * @param <T> type of the class
	 * @param type The class you're searching the field into.
	 * @param fieldName The field you're searching for.
	 * @param mapper the Mapper instance used to find the field with the fieldName as annotation 
	 * @return The Field found. If no result is found, null is returned.
	 * @see AliasNames
	 */
	public static <T> Field getField(Class<T> type, String fieldName, Mapper mapper) {
		Field field = getField(type,fieldName);
		if(field==null && mapper!=null) {
			field = mapper.getFieldsHolderFromCache(type).values().stream()
					 	  .filter(fieldHolder->fieldHolder.getAllNames().contains(fieldName))
					 	  .map(FieldHolder::getField)
					 	  .findFirst().orElse(null);
		}
		return field;
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
		    if(argTypes.length==1) {
		    	String typeName = argTypes[0].getTypeName();
		    	Pattern pattern = Pattern.compile(".+? extends ");
		    	if(pattern.matcher(typeName).find()) {
		    		int startIndex = typeName.indexOf("extends")+8;
					int endIndex = typeName.indexOf("<");
					typeName = typeName.substring(startIndex,endIndex==-1? typeName.length() : endIndex);
				}
		    	try {
			    	@SuppressWarnings("unchecked")
					Class<TYPE> resultClass = (Class<TYPE>)Class.forName(typeName);
					return resultClass;
		    	} catch (Exception e) {}
		    }
		}
		return null;
	}

	/**
	 * Create a mapping using the implementation present in the sub-class of the given {@link AbstractConverter} and {@code mapper} instance.
	 * @param converter the type of the custom {@code AbstractConverter} to use
	 * @param mapper the {@code mapper} instance needed for instantiate the {@code AbstractConverter}
	 * @param <FROM> the type of the origin object
	 * @param <TO> the type of the destination object
	 * @return the newly created mapping if successfully instantiate, otherwise prints a warning and returns {@code null}.
	 */
	public static <FROM,TO> DirectMapper<FROM,TO> createFromConverter(Class<? extends AbstractConverter<FROM,TO>> converter, Mapper mapper) {
		DirectMapper<FROM,TO> result = null;
		try {
			result = (DirectMapper<FROM,TO>)converter.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			try {
				result = (DirectMapper<FROM,TO>)converter.getConstructor(Mapper.class).newInstance(mapper);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				System.out.println("WARNING - The converter for "+converter+" does not have a empty public contructor or a constructor accepting a Mapper instance; the converter is ignored.");
			}
		}
		return result;
	}

	private static Map<Class<?>,Class<?>> wrapConvert = new HashMap<Class<?>,Class<?>>() {{
		put(byte.class,Byte.class);
		put(short.class,Short.class);
		put(int.class,Integer.class);
		put(long.class,Long.class);
		put(float.class,Float.class);
		put(double.class,Double.class);
		put(boolean.class,Boolean.class);
		put(char.class,Character.class);
	}};
	public static <T> Class<T> getWrapType(Class<T> type) {
		@SuppressWarnings("unchecked")
		Class<T> wrapType = (Class<T>) wrapConvert.getOrDefault(type,type);
		return wrapType;
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
	
}
