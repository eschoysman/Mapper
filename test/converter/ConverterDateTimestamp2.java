package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.converter.AbstractConverter;

public class ConverterDateTimestamp2 extends AbstractConverter<Date,Timestamp> {

	private ConverterDateTimestamp2() {
		super(Date.class, Timestamp.class, d->new Timestamp(d.getTime()));
	}

}
