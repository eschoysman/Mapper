package es.utils.mapper.configuration;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.defaultvalue.DefaultValueStrategy;
import es.utils.mapper.exception.CustomException;
import es.utils.mapper.exception.MappingException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static es.utils.mapper.defaultvalue.DefaultValueStrategy.*;

/**
 * This class handle the configuration at Mapper level.
 * @author eschoysman
 *
 */
public class Configuration {

	private Mapper mapper;
	private Map<Class<?>,Supplier<?>> suppliers;
	private Map<Class<?>,Supplier<?>> defaultValues;
	private Map<Class<? extends Annotation>,String> annotations;
	private boolean deepCopyEnabled;
	private UnaryOperator<?> cloner;
	private EnumSet<DefaultValueStrategy> defaultValuesStrategy;

	/**
	 * Create a configuration associated to the given {@code Mapper} instance.
	 * @param mapper the {@code Mapper} instance associated to this configuration.
	 * @see Mapper
	 */
	public Configuration(Mapper mapper) {
		this.mapper = mapper.setConfig(this);
		this.suppliers = new HashMap<>();
		this.annotations = new HashMap<>();
		this.defaultValues = new HashMap<>();
		this.defaultValuesStrategy = EnumSet.noneOf(DefaultValueStrategy.class);
		initDefaultValues();
	}

	private void initDefaultValues() {
		try {
			useAnnotation(AliasNames.class);
		} catch (MappingException e) {}
		setCloner(null);
		setDefaultValueStrategy(DEFAULT);
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
	 * Customize how to create a default value for the given type.
	 * @param <T> the type of the class instance to supply
	 * @param type the type default value to supply
	 * @param defaultValue the supplier
	 * @return The current configuration instance
	 */
	public <T> Configuration addDefaultValueSupplier(Class<T> type, Supplier<T> defaultValue) {
		this.defaultValues.put(type,defaultValue);
		return this;
	}
	/**
	 * @param <T> the type of the class
	 * @param type of the wanted default value supplier
	 * @return The supplier for the given type if present, {@code null} otherwise
	 */
	public <T> Supplier<T> getDefaultValueSupplier(Class<T> type) {
		@SuppressWarnings("unchecked")
		Supplier<T> supplier = (Supplier<T>)defaultValues.get(type);
		return supplier;
	}

	/**
	 * Allow to customize the annotation used to specify the alias name of a field using the "value" field of the annotation.<br>
	 * It is equivalent to call {@code addAnnotation(annotationType,null)} or {@code addAnnotation(annotationType,"value")}
	 * @param <T> The generic type of the annotation
	 * @param annotationType The type of the annotation
	 * @return The current configuration instance
	 * @throws MappingException If the fieldName does not exists in the annotation type or if it exists but does not return {@code String} or {@code String[]}.
	 * @see #useAnnotation(Class, String)
	 */
	public <T extends Annotation> Configuration useAnnotation(Class<T> annotationType) throws MappingException {
		return useAnnotation(annotationType,"value");
	}
	/**
	 * Allow to customize the annotation used to specify the alias name of a field using the "fieldName" field of the annotation.<br>
	 * @param <T> The generic type of the annotation
	 * @param annotationType The type of the annotation
	 * @param fieldName method name of the annotation containing the alias value to use
	 * @return The current configuration instance
	 * @throws MappingException If the fieldName does not exists in the annotation type or if it exists but does not return {@code String} or {@code String[]}.
	 * @see #useAnnotation(Class)
	 */
	public <T extends Annotation> Configuration useAnnotation(Class<T> annotationType, String fieldName) throws MappingException {
		if(fieldName==null || fieldName.trim().isEmpty()) {
			fieldName = "value";
		}
		try {
			Class<?> returnType = annotationType.getMethod(fieldName).getReturnType();
			if(String.class.equals(returnType.getComponentType()) || returnType.equals(String.class)) {
				this.annotations.put(annotationType,fieldName);
			} else {
				throw new RuntimeException("The fieldName of the given annotation returns "+returnType);
			}
		} catch (RuntimeException | NoSuchMethodException e) {
			throw CustomException.forType(MappingException.class).message("The fieldName of the given annotation does not return String or String[].").cause(e).build();
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

	/**
	 * Set the logic of the defaultValue strategy.
	 * @param defaultValuesStrategy array of strategies to use
	 * @return The current configuration instance
	 * @see #hasStrategy(DefaultValueStrategy)
	 * @see DefaultValueStrategy
	 */
	public Configuration setDefaultValueStrategy(DefaultValueStrategy... defaultValuesStrategy) {
		EnumSet<DefaultValueStrategy> input = EnumSet.noneOf(DefaultValueStrategy.class);
		Arrays.stream(defaultValuesStrategy).forEach(input::add);
		if(!input.isEmpty() && !input.contains(NEVER) && !input.contains(DEFAULT) && !input.contains(ALWAYS) && !input.contains(CUSTOM)) {
			input.add(CUSTOM);
		}
		if(this.defaultValuesStrategy!=null) {
			this.defaultValuesStrategy.clear();
		}
		if(input.isEmpty() || input.remove(NEVER)) {
			this.defaultValuesStrategy = EnumSet.of(NEVER);
			return this;
		}
		if(input.remove(DEFAULT)) {
			this.defaultValuesStrategy = EnumSet.of(ALWAYS,INPUT,OUTPUT);
			return this;
		}
		input.forEach(this.defaultValuesStrategy::add);
		if((this.defaultValuesStrategy.contains(ALWAYS) || this.defaultValuesStrategy.contains(CUSTOM))
				&& !this.defaultValuesStrategy.contains(INPUT) && !this.defaultValuesStrategy.contains(OUTPUT)) {
			this.defaultValuesStrategy.add(INPUT);
			this.defaultValuesStrategy.add(OUTPUT);
		}
		if(!this.defaultValuesStrategy.contains(ALWAYS)) {
			this.defaultValuesStrategy.add(CUSTOM);
		}
		return this;
	}
	/**
	 * @param strategy the strategy to use
	 * @return Returns {@code true} if the diven defaultValue strategy is set in the configurations, {@code false} otherwise
	 * @see #setDefaultValueStrategy(DefaultValueStrategy...)
	 * @see DefaultValueStrategy
	 */
	public boolean hasStrategy(DefaultValueStrategy strategy) {
		return this.defaultValuesStrategy.contains(strategy);
	}

}
