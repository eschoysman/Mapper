package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.Mapper;
import es.utils.mapper.converter.AbstractConverter;

public class ConverterDateTimestamp3 extends AbstractConverter<Date,Timestamp> {

	public ConverterDateTimestamp3(Mapper mapper) {
		super(Date.class, Timestamp.class, d->mapper.map(d,Timestamp.class));
	}

}
