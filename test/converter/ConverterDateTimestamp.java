package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.converter.AbstractConverter;

public class ConverterDateTimestamp extends AbstractConverter<Date,Timestamp> {

	public ConverterDateTimestamp() {
		super(Date.class,Timestamp.class);
	}

	@Override
	public Timestamp convert(Date input) {
		return new Timestamp(input.getTime());
	}

}
