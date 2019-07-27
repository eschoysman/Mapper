package from;

import converter.ConverterDateTimestamp2;
import es.utils.mapper.annotation.Converter;

public class FromWithConverter {

	@Converter(ConverterDateTimestamp2.class)
	private String name;
	
	public FromWithConverter() {
		this("Pippo");
	}
	
	public FromWithConverter(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return String.format("FromWithAnnotation [name=%s]", name);
	}

}
