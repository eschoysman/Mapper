package from;

import converter.ConverterDateTimestamp3;
import es.utils.mapper.annotation.Converter;

import java.util.Date;

public class ConverterFrom {

	@Converter(ConverterDateTimestamp3.class)
	private Date date = new Date();
	
	public Date getDate() {
		return this.date;
	}
	
}
