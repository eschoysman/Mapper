package from;

import java.util.Date;

import converter.ConverterDateTimestamp3;
import es.utils.mapper.annotation.Converter;

public class ConverterFrom {

	@Converter(ConverterDateTimestamp3.class)
	private Date date = new Date();
	
	public Date getDate() {
		return this.date;
	}
	
}
