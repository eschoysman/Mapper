package es.utils.mapper.factory.builder;

import es.utils.mapper.configuration.Configuration;

import java.util.function.Supplier;

/**
 * Optional step of the builder that allow to specify a default value if the result of the {@code Transformer} step is null.<br>
 * Previous mandatory step: {@link From}.<br>
 * Previous optional steps: {@link DefaultInput}, {@link Transformer}.<br>
 * Next mandatory step: {@link To}.
 * @author eschoysman
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface DefaultOutput<IN,GETTER_OUT,SETTER_IN,OUT> extends To<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Allow to specify a default value using a {@link Supplier}.
	 * @param defaultOutput a supplier used to obtain the default value to use.
	 * @return The next (mandatory) step of the builder: {@link To}.
	 * @see To
	 */
	To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutput(Supplier<SETTER_IN> defaultOutput);

	/**
	 * Allow to specify a default value using a {@link Class} and the related supplier setted in the {@link Configuration}.
	 * @param defaultValueType the class used to retrieve from the configuration the related supplier.
	 * @return The next (mandatory) step of the builder: {@link To}.
	 * @see Configuration#addDefaultValueSupplier(Class,Supplier)
	 * @see To
	 */
	To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutputForType(Class<SETTER_IN> defaultValueType);

	/**
	 * Allow to specify a default value.
	 * @param defaultOutput a fixed value to use as default.
	 * @return The next (mandatory) step of the builder: {@link To}.
	 * @see To
	 */
	default To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutput(SETTER_IN defaultOutput) {
		return defaultOutput(()->defaultOutput);
	}
	
}
