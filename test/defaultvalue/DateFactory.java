package defaultvalue;

import es.utils.mapper.Mapper;
import es.utils.mapper.defaultvalue.DefaultValueFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

public class DateFactory extends DefaultValueFactory<Date> {

	public DateFactory(Mapper mapper) {
		super(mapper);
	}

	@Override
	public Supplier<Date> getSupplier(String input, String... parameters) {
		DateFormat format = new SimpleDateFormat(parameters[0]);
		return ()->{
			try {
				return format.parse(input);
			} catch(ParseException e) {
				throw new RuntimeException(e);
			}
		};
	}

}
