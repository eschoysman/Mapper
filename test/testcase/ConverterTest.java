package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import converter.ConverterDateTimestamp2;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.From;
import from.FromWithConverter;
import to.To;
import to.ToWithConverter;

public class ConverterTest {

	@Test
	public void shouldApplyConverterIfPresent() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		mapper.build();
		From from = new From();
		To to = mapper.map(from);
		assertThat(to.getTimestamp1()).isNull();
		assertThat(to.getTimestamp2()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
		assertThat(to.getTimestamp3()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
	}
	
	@Test
	public void shouldThrowExceptioForNoEmptyConstructorConverter() throws MappingNotFoundException, MappingException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));

		Mapper mapper = new Mapper();
		mapper.add(FromWithConverter.class, ToWithConverter.class);
		mapper.build();
		FromWithConverter from = new FromWithConverter();
		ToWithConverter to = mapper.map(from);
		
		String outString = out.toString();
		out.flush();
		out.close();
		System.setOut(originalOut);

		assertThat(to.getName()).isNull();
		assertThat(outString).isEqualTo("WARNING - The converter for "+ConverterDateTimestamp2.class+" does not have a empty public contructor; the converter is ignored."+System.lineSeparator());
	}
	
}
