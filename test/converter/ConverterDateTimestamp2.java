package converter;

import es.utils.mapper.converter.AbstractConverter;

import java.sql.Timestamp;
import java.util.Date;

public class ConverterDateTimestamp2 extends AbstractConverter<Date,Timestamp> {

	private ConverterDateTimestamp2() {
		super(Date.class, Timestamp.class);
	}

	@Override
	public Timestamp convert(Date input) {
		return new Timestamp(input.getTime());
	}
	
}
