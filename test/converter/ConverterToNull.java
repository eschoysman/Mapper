package converter;
import es.utils.mapper.converter.AbstractConverter;

public class ConverterToNull extends AbstractConverter<String,Integer> {

	public ConverterToNull() {
		super(String.class,Integer.class);
	}

	@Override
	public Integer convert(String input) {
		return null;
	}

}
