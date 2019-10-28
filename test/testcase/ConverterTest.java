package testcase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.Test;

import converter.ConverterDateTimestamp;
import converter.ConverterDateTimestamp2;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.ConverterFrom;
import from.From;
import from.FromWithConverter;
import to.ConverterTo;
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
	public void shouldThrowExceptioForNoEmptyConstructorOrMapperConverter() throws MappingNotFoundException, MappingException, IOException {
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
		assertThat(outString).isEqualTo("WARNING - The converter for "+ConverterDateTimestamp2.class+" does not have a empty public contructor or a constructor accepting a Mapper instance; the converter is ignored."+System.lineSeparator());
	}
	@Test
	public void shouldThrowExceptioForNoEmptyConstructorConverter() throws MappingNotFoundException, MappingException, IOException {
		Mapper mapper = new Mapper();
		mapper.add(ConverterFrom.class, ConverterTo.class);
		mapper.add(Date.class, Timestamp.class, d->new Timestamp(d.getTime()));
		mapper.build();
		ConverterFrom from = new ConverterFrom();
		ConverterTo to = mapper.map(from);

		assertThat(to.getTimeStamp()).isNotNull();
		assertThat(to.getTimeStamp().toInstant().toEpochMilli()).isEqualTo(from.getDate().getTime());
	}
	
	@Test
	public void shouldApplyConverterInBuilder() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		mapper.addForClass(From.class,To.class).addMapping()
			  .<Date>from("data4").transform(ConverterDateTimestamp.class).to("data4")
			  .create();
		mapper.build();
		From from = new From();
		To to = mapper.map(from);
		assertThat(to.getTimestamp1()).isNull();
		assertThat(to.getTimestamp2()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
		assertThat(to.getTimestamp3()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
		assertThat(to.getTimestamp4()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
	}
	
	@Test
	public void shouldApplyConverterInBuilder2() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		mapper.addForClass(From.class, To.class).addMapping()
			  .<Date>from("data4").transform(d->d).transform(ConverterDateTimestamp.class).to("data4")
			  .create();
		mapper.build();
		From from = new From();
		To to = mapper.map(from);
		assertThat(to.getTimestamp1()).isNull();
		assertThat(to.getTimestamp2()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
		assertThat(to.getTimestamp3()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
		assertThat(to.getTimestamp4()).isEqualTo(Timestamp.valueOf("2019-07-07 08:45:36"));
	}
	

	@Test
	public void shouldThrowMappingExceptionInConverterInBuilder() throws MappingNotFoundException, MappingException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));
		Mapper mapper = new Mapper();
		MappingException exception = assertThrows(MappingException.class, ()->
			mapper.addForClass(From.class, To.class).addMapping()
				  .<Date>from("data4").transform(ConverterDateTimestamp2.class).to("data4")
				  .create());
		System.setOut(originalOut);
		assertThat(exception.getMessage()).isEqualTo("Converter of "+ConverterDateTimestamp2.class+" cannot be istanziate.");
	}
	
	@Test
	public void shouldThrowMappingExceptionInConverterInBuilder2() throws MappingNotFoundException, MappingException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));
		Mapper mapper = new Mapper();
		MappingException exception = assertThrows(MappingException.class, ()->
			mapper.addForClass(From.class, To.class).addMapping()
				  .<Date>from("data4").transform(d->d).transform(ConverterDateTimestamp2.class).to("data4")
				  .create());
		mapper.build();
		System.setOut(originalOut);
		assertThat(exception.getMessage()).isEqualTo("Converter of "+ConverterDateTimestamp2.class+" cannot be istanziate.");
	}
	
}
