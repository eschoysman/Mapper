package es.utils.mapper.factory.builder;

import es.utils.mapper.configuration.Configuration;

import java.util.function.Supplier;

/**
 * Optional step of the builder that allow to specify a default value if the getter of the {@code From} step is null.<br>
 * Previous mandatory step: {@link From}<br>
 * Next optional steps: {@link Transformer}, {@link DefaultOutput}.<br>
 * Next mandatory step: {@link To}.
 * @author eschoysman
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface DefaultInput<IN,GETTER_OUT,SETTER_IN,OUT> extends Transformer<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Allow to specify a default value using a {@link Supplier}.
	 * @param defaultInput a supplier used to obtain the default value to use.
	 * @return The next (optional) step of the builder: {@link DefaultInput}.
	 * @see Transformer
	 * @see DefaultOutput
	 * @see To
	 */
	public Transformer<IN,GETTER_OUT,GETTER_OUT,OUT> defaultInput(Supplier<GETTER_OUT> defaultInput);

	/**
	 * Allow to specify a default value using a {@link Class} and the related supplier setted in the {@link Configuration}.
	 * @param defaultValueType the class used to retrieve from the configuration the related supplier.
	 * @return The next (optional) step of the builder: {@link DefaultInput}.
	 * @see Configuration#addDefaultValueSupplier(Class,Supplier)
	 * @see Transformer
	 * @see DefaultOutput
	 * @see To
	 */
	public Transformer<IN,GETTER_OUT,GETTER_OUT,OUT> defaultInputFor(Class<GETTER_OUT> defaultValueType);

	/**
	 * Allow to specify a default value.
	 * @param defaultInput a fixed value to use as default.
	 * @return The next (optional) step of the builder: {@link DefaultInput}.
	 * @see Transformer
	 * @see DefaultOutput
	 * @see To
	 */
	public default Transformer<IN,GETTER_OUT,GETTER_OUT,OUT> defaultInput(GETTER_OUT defaultInput) {
		return defaultInput(()->defaultInput);
	}

}
