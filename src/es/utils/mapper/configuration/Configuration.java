package es.utils.mapper.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import es.utils.mapper.annotation.AliasNames;

public class Configuration {

	private Map<Class<?>,Supplier<?>> suppliers;
	private Map<Class<? extends Annotation>,String> annotations;

    public Configuration() {
    	this.suppliers = new HashMap<>();
    	this.annotations = new HashMap<>();
    	initDefaultValues();
    }

    private void initDefaultValues() {
    	this.annotations.put(AliasNames.class,"value");
    }
    
    public <T> Configuration addSuplier(Class<T> type, Supplier<T> supplier) {
    	this.suppliers.put(type,supplier);
    	return this;
    }
    
    public <T> Supplier<T> getSupplier(Class<T> type) {
    	@SuppressWarnings("unchecked")
		Supplier<T> supplier = (Supplier<T>)suppliers.get(type);
		return supplier;
    }

    public <T extends Annotation> Configuration addAnnotation(Class<T> type, String fieldname) {
    	this.annotations.put(type,fieldname);
    	return this;
    }
    
    public <T extends Annotation> String getValue(Class<T> type) {
    	return this.annotations.get(type);
    }
    
}
