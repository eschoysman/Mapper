package es.utils.mapper.converter;

import es.utils.mapper.annotation.Converter;
import es.utils.mapper.impl.object.DirectMapper;

/**
 * This class is used to specify a converter over a single field in a mapping inside the {@code Converter} annotation.<br>
 * Your custom converter must have a empty constructor that will be used to instantiate your converter.<br>
 * The converter is apply if specified in the {@code Converter} annotation of the origin field or (if not found) on the destination field
 * <br><br>
 * Example of {@code AbstractConverter} that convert a Timestamp into a Date:
 * <pre>
public class ConverterDateTimestamp extends AbstractConverter&lt;Date,Timestamp&gt; {
    public ConverterDateTimestamp() {
        super(Date.class, Timestamp.class, date-&gt;new Timestamp(date.getTime()));
    }
}
 * </pre>
 * And on the field to be mapped:
 * <pre>@Converter(ConverterDateTimestamp.class)</pre>
 * 
 * @author eschoysman
 *
 * @param <IN> type of the object to convert
 * @param <OUT> type of the object converted
 * 
 * @see Converter
 */
public abstract class AbstractConverter<IN,OUT> extends DirectMapper<IN,OUT> {

	/**
	 * Create a DirectMapper from type {@code from} to type {@code to} with no transformation.
	 * The conversion is done calling the abstract method {@code convert}.
	 * @param from source type
	 * @param to destination type
	 */
	protected AbstractConverter(Class<IN> from, Class<OUT> to) {
		super(from,to,null);
	}
	
	/**
	 * The conversion implementation for the current {@code DirectMapper} implementation
	 * @param input input object
	 * @return the input object converted
	 * @throws Exception allow the implementation to throw any kind of exception 
	 */
	public abstract OUT convert(IN input) throws Exception;
	
}
