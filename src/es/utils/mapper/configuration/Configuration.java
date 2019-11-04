package es.utils.mapper.configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.exception.MappingException;

public class Configuration {

	private Mapper mapper;
	private Map<Class<?>,Supplier<?>> suppliers;
	private Map<Class<? extends Annotation>,String> annotations;
	private boolean deepCopyEnabled;
	private UnaryOperator<?> cloner;

	/**
	 * Create a configuration assiciated to the given {@code Mapper} instance.
	 * @param mapper the {@code Mapper} instance associated to this configuration.
	 * @see Mapper
	 */
	public Configuration(Mapper mapper) {
		this.mapper = mapper.setConfig(this);
		this.suppliers = new HashMap<>();
		this.annotations = new HashMap<>();
		initDefaultValues();
	}

	private void initDefaultValues() {
		try {
			addAnnotation(AliasNames.class);
		} catch (MappingException e) {}
		setCloner(null);
	}
	
	/**
	 * @return Returns the {@code Mapper} instance associated to this {@code Configuration} instance
	 */
	public Mapper getMapper() {
		return this.mapper;
	}
	
	/**
	 * Customize how to create a new instance of the given type.
	 * @param <T> the type of the class instance to supply
	 * @param type the type to supply
	 * @param supplier the supplier
	 * @return The current configuration instance
	 */
	public <T> Configuration addSupplier(Class<T> type, Supplier<T> supplier) {
		this.suppliers.put(type,supplier);
		return this;
	}
	/**
	 * @param <T> the type of the class
	 * @param type of the wanted supplier
	 * @return The supplier for the given type if present, {@code null} otherwise
	 */
public <T> Supplier<T> getSupplier(Class<T> type) {
		@SuppressWarnings("unchecked")
		Supplier<T> supplier = (Supplier<T>)suppliers.get(type);
		return supplier;
	}

	/**
	 * Allow to customize the annotation used to specify the alias name of a field using the "value" field of the annotation.<br>
	 * It is equivalent to call {@code addAnnotation(annotationType,null)} or {@code addAnnotation(annotationType,"value")}
	 * @param <T> The generic type of the annotation
	 * @param annotationType The type of the annotation
	 * @return The current configuration instance
	 * @throws MappingException If the fieldName does not exists in the annotation type or if it exists but does not return {@code String} or {@code String[]}.
	 * @see #addAnnotation(Class, String)
	 */
	public <T extends Annotation> Configuration addAnnotation(Class<T> annotationType) throws MappingException {
		return addAnnotation(annotationType,"value");
	}
	/**
	 * Allow to customize the annotation used to specify the alias name of a field using the "fieldName" field of the annotation.<br>
	 * @param <T> The generic type of the annotation
	 * @param annotationType The type of the annotation
	 * @return The current configuration instance
	 * @throws MappingException If the fieldName does not exists in the annotation type or if it exists but does not return {@code String} or {@code String[]}.
	 * @see #addAnnotation(Class)
	 */
	public <T extends Annotation> Configuration addAnnotation(Class<T> annotationType, String fieldName) throws MappingException {
		fieldName = Optional.ofNullable(fieldName).orElse("value");
		try {
			Class<?> returnType = annotationType.getMethod(fieldName).getReturnType();
			if(String.class.equals(returnType.getComponentType()) || returnType.equals(String.class)) {
				this.annotations.put(annotationType,fieldName);
			} else {
				throw new RuntimeException("The fieldName of the given annotation returns "+returnType);
			}
		} catch (RuntimeException | NoSuchMethodException e) {
			throw new MappingException("The fieldName of the given annotation does not return String or String[].",e);
		}
		return this;
	}
	/**
	 * @param <T> The generic type of the annotation
	 * @param annotationType The type of the annotation
	 * @return The field of the annotation to use for the alias name of the annotated field
	 */
	public <T extends Annotation> String getAnnotationField(Class<T> annotationType) {
		return this.annotations.get(annotationType);
	}

	/**
	 * Enable the deep copy of the field
	 * @return The current configuration instance
	 * @see #disableDeepCopy()
	 * @see #isDeepCopyEnabled()
	 */
	public Configuration enableDeepCopy() {
		this.deepCopyEnabled = true;
		return this;
	}
	/**
	 * Disable the deep copy of the field
	 * @return The current configuration instance
	 * @see #enableDeepCopy()
	 * @see #isDeepCopyEnabled()
	 */
	public Configuration disableDeepCopy() {
		this.deepCopyEnabled = false;
		return this;
	}
	/**
	 * @return Returns {@code true} only if deepCopy is enabled, {@code false} otherwise
	 * @see #disableDeepCopy()
	 * @see #enableDeepCopy()
	 */
	public boolean isDeepCopyEnabled() {
		return this.deepCopyEnabled;
	}

	/**
	 * Set to cloner to use if {@code deepCopy} is enabled.<br>
	 * If {@code cloner} is not {@code null}, {@code deepCopy} will be enabled.
	 * @param <T> Generic type of the cloner operation (just for not having Object)
	 * @param cloner The cloning operation
	 * @return The current configuration instance
	 * @see #disableDeepCopy()
	 * @see #enableDeepCopy()
	 * @see #getCloner()
	 */
	public <T> Configuration setCloner(UnaryOperator<T> cloner) {
		this.cloner = cloner;
		if(cloner==null) {
			disableDeepCopy();
		}
		else {
			enableDeepCopy();
		}
		return this;
	}
	/**
	 * @param <T> Generic type of the cloner operation (just for not having Object)
	 * @return Returns a {@link UnaryOperator} that can be used to copy objects. If no cloner was set, it will returns {@code null}.
	 * @see #setCloner(UnaryOperator)
	 */
	public <T> UnaryOperator<T> getCloner() {
		@SuppressWarnings("unchecked")
		UnaryOperator<T> cloner = (UnaryOperator<T>)this.cloner;
		return cloner;
	}
	
}
