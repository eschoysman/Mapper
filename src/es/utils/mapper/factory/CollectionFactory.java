package es.utils.mapper.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class lets you instantiate a collection based on destination class type.
 * The destination class type should be a concrete class (e.g. subclasses of Collection), otherwise
 * an exception is thrown.
 * If the destination class type is an array, an ArrayList is created.
 * @author eschoysman
 */
public class CollectionFactory {
	
    private static final Class<?> ARRAY_AS_LIST_CLASS = Arrays.asList().getClass();

    /**
     * This method instantiate a new collection based on collectionType.
     * @param <IN> the type of the objects in the origin collection
     * @param <OUT> the type of the objects in the destination collection
     * @param inputCollectionType the class of input. It must extend Collection.
     * @param collectionType the class of input. It must extend Collection.
     * @return a new instance of collectionType.
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
