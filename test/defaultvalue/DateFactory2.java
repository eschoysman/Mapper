package defaultvalue;

import es.utils.mapper.defaultvalue.DefaultValueFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

public class DateFactory2 extends DefaultValueFactory<Date> {

	public DateFactory2() {
		super(null);
	}
	@Override
	public Supplier<Date> getSupplier(String input, String... parameters) {
		DateFormat format = new SimpleDateFormat(parameters[0]);
		Supplier<Date> supplier = ()->{
			try {
				return format.parse(input);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		};
		return supplier;
	}

}
