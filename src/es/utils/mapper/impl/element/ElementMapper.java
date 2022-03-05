package es.utils.mapper.impl.element;

import es.utils.mapper.Mapper;
import es.utils.mapper.defaultvalue.DefaultValueStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class contains the informations and the logic needed the execute the mapping of an single element:
 * <ul>
 * <li>a <b>Getter</b>: a function that take a {@code IN} type object</li>
 * <li>a <b>Transformer</b>: a function that maps the result of the {@code getter} into the correct type for the {@code setter}</li>
 * <li>a <b>Setter</b>: a operation that assign the result of the {@code transformer} to the destination object</li>
 * </ul>
 * 
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the type of the field (of the origin object) to be mapped into the destination object 
 * @param <SETTER_IN> the type of the field (of the destination object) mapped
 * @param <OUT> the type of the destination object
 * @see Getter
 * @see Setter
 */
@Slf4j
public class ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> {

	private static int id_incr = 0;
	private int id = ++id_incr;

	private Mapper mapper;
	private String name;
	private Getter<IN,GETTER_OUT> getter;
	private Supplier<GETTER_OUT> defaultInput;
	private Function<GETTER_OUT,SETTER_IN> transformer;
	private Supplier<SETTER_IN> defaultOutput;
	private Setter<OUT,SETTER_IN> setter;
	
	/**
	 * @param mapper the mapper of belonging
	 * @param name the name of the {@code ElementMapper}, it is an optional field to set
	 * @param getter an implementation of the getter logic
	 * @param defaultInput supplier for the default input value
	 * @param transformer a function to map the result of the getter into the correct type for the setter
	 * @param defaultOutput supplier for the default output value
	 * @param setter an implementation of the getter logic
	 */
	public ElementMapper(Mapper mapper, String name, Getter<IN,GETTER_OUT> getter,
													 Supplier<GETTER_OUT> defaultInput,
													 Function<GETTER_OUT,SETTER_IN> transformer,
													 Supplier<SETTER_IN> defaultOutput,
													 Setter<OUT,SETTER_IN> setter) {
		this.mapper = Objects.requireNonNull(mapper);
		this.name = Optional.ofNullable(name).orElse("elementMapper_id_"+id);
		this.getter = Objects.requireNonNull(getter);
		this.defaultInput = Objects.requireNonNull(defaultInput);
		this.transformer = Objects.requireNonNull(transformer);
		this.defaultOutput = Objects.requireNonNull(defaultOutput);
		this.setter = Objects.requireNonNull(setter);
	}

	/**
	 * Set a supplier (from the mapper configuration) for the default value to set in the destination if the value in this point is {@code null}.  
	 * @param defaultOutput the type of the supplier for the default value
	 * @return a supplier of the given type
	 */
	public ElementMapper<IN,GETTER_OUT,SETTER_IN,OUT> setDefaultValue(Class<SETTER_IN> defaultOutput) {
		this.defaultOutput = mapper.config().getDefaultValueSupplier(defaultOutput);
		return this;
	}

	/**
	 * The name of the current ElementMapper
	 * @return the name of the current ElementMapper
	 */
	public String getName() {
		return name;
	}

	/**
	 * The unique id identifier of the getter
	 * @return the unique id identifier of the getter
	 */
	public int getFromId() {
		return getter.getId();
	}
	/**
	 * The name identifier of the getter
	 * @return the name identifier of the getter
	 */
	public String getFromName() {
		return getter.getName();
	}
	/**
	 * The unique id identifier of the setter
	 * @return the unique id identifier of the setter
	 */
	public int getDestId() {
		return setter.getId();
	}
	/**
	 * The name identifier of the setter
	 * @return the name identifier of the setter
	 */
	public String getDestName() {
		return setter.getName();
	}

	/**
	 * The logic of the mapping of the current element.
	 * @param in the original object
	 * @param out the destination object
	 */
	public void apply(IN in, OUT out) {
		GETTER_OUT getterResult = getter(in);
		getterResult = defaultValueGetter(getterResult);
		SETTER_IN transformed = transform(getterResult);
		transformed = defaultValueSetter(transformed);
		setter(out,transformed);
	}

	/**
	 * Returns a human readable string of the current {@code ElementMapper}
	 */
	@Override
	public String toString() {
		return "ElementMapper["+name+","+getter+","+setter+"]";
	}
	
	
	// PRIVATE METHODS
	
	private GETTER_OUT getter(IN in) {
		GETTER_OUT getterResult = null;
		// controllo se il cloner è abilitato e presente e in tal caso lo eseguo
		if(mapper.config().isDeepCopyEnabled() && mapper.config().getCloner()!=null) {
			getterResult = mapper.config().<GETTER_OUT>getCloner().apply(getter.apply(in));
		}
		else {
			getterResult = getter.apply(in);
		}
		return getterResult;
	}
	private GETTER_OUT defaultValueGetter(GETTER_OUT current) {
		if(current==null) {
			if(mapper.config().hasStrategy(DefaultValueStrategy.INPUT) && defaultInput!=null) {
				current = defaultInput.get();
			}
		}
		return current;
	}
	private SETTER_IN transform(GETTER_OUT getterResult) {
//		if(mapper.config().isDeepCopyEnabled() && mapper.config().getCloner()!=null) {
//			this.transformer = this.transformer.compose(mapper.config().<GETTER_OUT>getCloner()::apply);
//		}
		SETTER_IN transformed = transformer.apply(getterResult);
		return transformed;
	}
	private SETTER_IN defaultValueSetter(SETTER_IN current) {
		if(current==null) {
			if(mapper.config().hasStrategy(DefaultValueStrategy.OUTPUT) && defaultOutput!=null) {
				current = defaultOutput.get();
			}
		}
		return current;
	}
	private void setter(OUT out, SETTER_IN transformed) {
		//log.trace("Applying setter \""+getDestName()+"\" with input: "+transformed);
		setter.apply(out,transformed);
	}

}
