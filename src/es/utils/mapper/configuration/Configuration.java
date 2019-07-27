package es.utils.mapper.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.exception.MappingException;

public class Configuration {

	private Map<Class<?>,Supplier<?>> suppliers;
	private Map<Class<? extends Annotation>,String> annotations;
	private boolean deepCopyEnable;
	private UnaryOperator<?> cloner;

    public Configuration() {
    	this.suppliers = new HashMap<>();
    	this.annotations = new HashMap<>();
    	initDefaultValues();
    }

    private void initDefaultValues() {
    	try {
			addAnnotation(AliasNames.class,null);
		} catch (MappingException e) {}
    	this.deepCopyEnable = false;
    	this.cloner = obj->obj;	// non fa niente, mette un Cloner di default che restituisce l'input senza toccarlo
    }
    
    public <T> Configuration addSupplier(Class<T> type, Supplier<T> supplier) {
    	this.suppliers.put(type,supplier);
    	return this;
    }
    public <T> Supplier<T> getSupplier(Class<T> type) {
    	@SuppressWarnings("unchecked")
		Supplier<T> supplier = (Supplier<T>)suppliers.get(type);
		return supplier;
    }

    public <T extends Annotation> Configuration addAnnotation(Class<T> type, String fieldName) throws MappingException {
    	fieldName = fieldName==null ? "value" : fieldName;
		try {
			Class<?> returnType = type.getMethod(fieldName).getReturnType();
			if(String.class.equals(returnType.getComponentType()) || returnType.equals(String.class)) {
				this.annotations.put(type,fieldName);
			} else {
				throw new RuntimeException("The fieldName of the given annotation returns "+returnType);
			}
		} catch (RuntimeException | NoSuchMethodException e) {
			throw new MappingException("The fieldName of the given annotation does not return String or String[].",e);
		}
    	return this;
    }
    public <T extends Annotation> String getAnnotationField(Class<T> type) {
    	return this.annotations.get(type);
    }

    public Configuration deepCopy(boolean deepCopyEnable) {
    	this.deepCopyEnable = deepCopyEnable;
    	return this;
    }
    public boolean isDeepCopyEnable() {
    	return this.deepCopyEnable;
    }

    public <T> Configuration setCloner(UnaryOperator<T> cloner) {
    	this.cloner = cloner;
    	deepCopy(cloner!=null);
    	return this;
    }
    public <T> UnaryOperator<T> getCloner() {
    	@SuppressWarnings("unchecked")
		UnaryOperator<T> cloner = (UnaryOperator<T>)this.cloner;
		return cloner;
    }
    
}
