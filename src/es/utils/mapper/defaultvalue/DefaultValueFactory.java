package es.utils.mapper.defaultvalue;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.Default;

import java.util.function.Supplier;

/**
 * This class is used to specify a supplier over a single field in a mapping inside the {@code Default} annotation.<br>
 * Your custom factory must have a constructor taking a {@link Mapper} instance that will be used to instantiate your DefaultValueFactory.<br>
 * The factory is apply if specified in the {@code Default} annotation on the field to be set with a default value.
 * <br><br>
 * Example of {@code DefaultValueFactory} that generate a Date:
 * <pre>
public class DateFactory extends DefaultValueFactory&lt;Date&gt; {
    public DateFactory(Mapper mapper) {
        super(mapper);    // in this example the mapper is not used, is possible to call "super(null);"
    }
    public Supplier&lt;Date&gt; getSupplier(String input, String... parameters) {
        DateFormat format = new SimpleDateFormat(parameters[0]);
        return () -&gt; {
            try {
                return format.parse(input);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
 * </pre>
 * And on the annotated field:
 * <pre>@Default(factory=DateFactory.class, value="01/01/2020",parameters="dd/MM/yyyy")</pre>
 * 
 * @author eschoysman
 * @param <T> type of the object to be supplied
 * @see Default
 */
public abstract class DefaultValueFactory<T> {

	/**
	 * The {@link Mapper} instance that can be used in the sub-class
	 */
	protected Mapper mapper;

	/**
	 * Create a new instance that can use the diven  {@code mapper}
	 * @param mapper the mapper instance injected during the instance creation
	 */
	public DefaultValueFactory(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Create a {@code Supplier} for the desired type using a input string and optional parameters.
	 * @param input a required parameter used to instantiate the object object returned by the supplier.
	 * @param parameters optional value that can be used to instantiate a new object object returned by the supplier.
	 * @return a {@code Supplier} for the desired type using a input string and optional parameters.
	 */
	public abstract Supplier<T> getSupplier(String input, String... parameters);
	
}
