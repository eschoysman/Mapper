package es.utils.mapper.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CollectionFactory {
	
    private static final Class<?> ARRAY_AS_LIST_CLASS = Arrays.asList().getClass();

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
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static <IN,OUT> Collection<OUT> create(Class<? extends Collection> inputCollectionType, Class<? extends Collection> collectionType, int size) {
//		inputCollectionType = getClassType(inputCollectionType,collectionType);
//		try {
//			return (Collection<OUT>)inputCollectionType.getDeclaredConstructor(int.class).newInstance(size);
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			System.err.println("Error during the creation of the destination collection. Returning null value.");
//			return null;
//		}
//	}
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static <IN,OUT> Collection<OUT> createAndFill(Collection<IN> inputCollection, Class<? extends Collection> collectionType) {
//		if(inputCollection==null)
//			return null;
//		Class<? extends Collection> inputCollectionType = getClassType(inputCollection.getClass(),collectionType);
//		try {
//			return (Collection<OUT>)inputCollectionType.getDeclaredConstructor(Collection.class).newInstance(inputCollection);
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			System.err.println("Error during the creation of the destination collection. Returning null value.");
//			return null;
//		}
//	}
	
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
