package es.utils.mapper.factory.builder;

import java.util.function.Supplier;

import es.utils.mapper.configuration.Configuration;

/**
 * Optional step of the EMBuilder that allow to specify a default value if the result of the {@code Transformer} step is null.<br>
 * Previous step: {@link Transformer}<br>
 * Next step: {@link To}
 * @author eschoysman
 *
 * @param <IN> the type of the origin object
 * @param <GETTER_OUT> the resulting type of the getter operation
 * @param <SETTER_IN> the input type of the setter operation
 * @param <OUT> the type of the destination object
 * @see <a href="package-summary.html">builder package</a>
 */
public interface DefaultOutput<IN,GETTER_OUT,SETTER_IN,OUT> extends To<IN,GETTER_OUT,SETTER_IN,OUT>, Consume<IN,GETTER_OUT,SETTER_IN,OUT> {

	/**
	 * Allow to specify a default value using a {@link Supplier}.
	 * @param defaultOutput a supplier used to obtain the default value to use.
	 * @return the next (mandatory) step of the EMBuilder: {@link To}
	 * @see To
	 */
	public To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutput(Supplier<SETTER_IN> defaultOutput);

	/**
	 * Allow to specify a default value using a {@link Class} and the related supplier setted in the {@link Configuration}.
	 * @param defaultValueType the class used to retrieve from the configuration the related supplier.
	 * @return the next (mandatory) step of the EMBuilder: {@link To}
	 * @see Configuration#addDefaultValueSupplier(Class,Supplier)
	 * @see To
	 */
	public To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutputFor(Class<SETTER_IN> defaultValueType);

	/**
	 * Allow to specify a default value.
	 * @param defaultOutput a fixed value to use as default.
	 * @return the next (mandatory) step of the EMBuilder: {@link To}
	 * @see To
	 */
	public default To<IN,GETTER_OUT,SETTER_IN,OUT> defaultOutput(SETTER_IN defaultOutput) {
		return defaultOutput(()->defaultOutput);
	}
	
}
