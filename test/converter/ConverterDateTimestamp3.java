package converter;
import java.sql.Timestamp;
import java.util.Date;

import es.utils.mapper.Mapper;
import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;

public class ConverterDateTimestamp3 extends AbstractConverter<Date,Timestamp> {

	private Mapper myMapper;
	
	public ConverterDateTimestamp3(Mapper mapper) {
		super(Date.class,Timestamp.class);
		this.myMapper = mapper;
	}

	@Override
	public Timestamp convert(Date input) throws MappingNotFoundException, MappingException {
		return myMapper.map(input,Timestamp.class);
	}

}
