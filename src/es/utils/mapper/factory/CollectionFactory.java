package es.utils.mapper.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * @author Emmanuel
 *
 */
public class CollectionFactory {
	
    private static final Class<?> ARRAY_AS_LIST_CLASS = Arrays.asList().getClass();

    /**
     * 
     * @param inputCollectionType
     * @param collectionType
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <IN,OUT> Collection<OUT> create(Class<? extends Collection> inputCollectionType, Class<? extends Collection> collectionType) {
		inputCollectionType = getClassType(inputCollectionType,collectionType);
		try {
			return (Collection<OUT>)inputCollectionType.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.err.println("Error during the creation of the destination collection. Returning null value.");
			return null;
		}
	}

	/**
	 * 
	 * @param inputCollectionType
	 * @param collectionType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Class<? extends Collection> getClassType(Class<? extends Collection> inputCollectionType, Class<? extends Collection> collectionType) {
		if(collectionType!=null) {
			inputCollectionType = collectionType;
		}
		if(inputCollectionType.equals(ARRAY_AS_LIST_CLASS)) {
			inputCollectionType = ArrayList.class;
		}
		return inputCollectionType;
	}
	
}
