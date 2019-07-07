package es.utils.mapper.converter;

import java.util.function.Function;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.impl.object.DirectMapper;

/**
 * This class is used to specify a converter over a single field in a mapping inside the {@code AliasNames} annotation.<br>
 * Your custom converter must have a empty constructor that will be used to instantiate your converter.<br>
 * The converter is apply if specified in the {@code AliasNames} annotation of the origin field or (if not found) on the destination field
 * <br><br>
 * Example of {@code Converter} that convert a Timestamp into a Date:
 * <pre>
public class ConverterDateTimestamp extends Converter&lt;Timestamp,Date&gt; {
    public ConverterDateTimestamp() {
        super(Timestamp.class, Date.class, t-&gt;new Date(t.getTime()));
    }
}
 * </pre>
 * 
 * @author eschoysman
 *
 * @param <IN> type of the object to convert
 * @param <OUT> type of the object converted
 * 
 * @see AliasNames
 */
public class Converter<IN,OUT> extends DirectMapper<IN,OUT> {

	public Converter(Class<IN> from, Class<OUT> to, Function<IN, OUT> transformer) {
		super(from, to, transformer);
	}
	
}
