package converter;

import es.utils.mapper.converter.AbstractConverter;

import java.sql.Timestamp;
import java.util.Date;

public class ConverterDateTimestamp extends AbstractConverter<Date,Timestamp> {

	public ConverterDateTimestamp() {
		super(Date.class,Timestamp.class);
	}

	@Override
	public Timestamp convert(Date input) {
		return new Timestamp(input.getTime());
	}

}
