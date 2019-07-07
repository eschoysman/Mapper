package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.converter.Converter;

public class ConverterDateTimestamp extends Converter<Date,Timestamp> {

	public ConverterDateTimestamp() {
		super(Date.class, Timestamp.class, d->new Timestamp(d.getTime()));
	}

}
