package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.converter.AbstractConverter;

public class ConverterDateTimestamp extends AbstractConverter<Date,Timestamp> {

	public ConverterDateTimestamp() {
		super(Date.class, Timestamp.class, d->new Timestamp(d.getTime()));
	}

}
