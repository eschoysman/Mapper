package testcase;

import es.utils.mapper.Mapper;
import es.utils.mapper.defaultvalue.DefaultValueStrategy;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.DefaultValuesFrom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import to.DefaultValuesTo;
import utils.AlternativeConsole;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultValueTest {
	
	private AlternativeConsole console;

	@BeforeEach
	public void beforeEach() {
//		MapperLogger.enabled.put(LogConstant.TYPE.CREATION, LogConstant.LEVEL.MAPPER);
		console = new AlternativeConsole();
	}
	@AfterEach
	public void afterEach() {
		console.reset();
	}
	
	@Test
	public void shouldCreateEmptyObject() {
		DefaultValuesTo to = new DefaultValuesTo();
		assertThat(to.getString()).isNull();
		assertThat(to.getString2()).isNull();
		assertThat(to.getNumByte()).isNull();
		assertThat(to.getNumShort()).isNull();
		assertThat(to.getNumInteger()).isNull();
		assertThat(to.getNumLong()).isNull();
		assertThat(to.getNumFloat()).isNull();
		assertThat(to.getNumDouble()).isNull();
		assertThat(to.getBool()).isNull();
		assertThat(to.getCharacter()).isNull();
		assertThat(to.getType()).isNull();
		assertThat(to.getTimeUnit()).isNull();
		assertThat(to.getDate()).isNull();
		assertThat(to.getDateF()).isNull();
	}

	@Test
	public void shouldCreateEmptyObjectWithDefaultValuesAlways() throws MappingException, MappingNotFoundException, ParseException {
		Mapper mapper = new Mapper();
		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.ALWAYS);
		mapper.add(DefaultValuesFrom.class,DefaultValuesTo.class);
		DefaultValuesFrom from = new DefaultValuesFrom();
		DefaultValuesTo to = mapper.map(from,DefaultValuesTo.class);
		assertThat(to.getString()).isEqualTo("string di default");
		assertThat(to.getString2()).isEqualTo(new String("charset".getBytes(StandardCharsets.UTF_8)));
		assertThat(to.getNumByte()).isEqualTo((byte)42);
		assertThat(to.getNumShort()).isEqualTo((short)42);
		assertThat(to.getNumInteger()).isEqualTo((int)42);
		assertThat(to.getNumLong()).isEqualTo((long)42);
		assertThat(to.getNumFloat()).isEqualTo((float)Math.PI);
		assertThat(to.getNumDouble()).isEqualTo((double)Math.PI);
		assertThat(to.getBool()).isEqualTo(true);
		assertThat(to.getCharacter()).isEqualTo('c');
		assertThat(to.getType()).isEqualTo(DefaultValuesTo.class);
		assertThat(to.getTimeUnit()).isEqualTo(TimeUnit.DAYS);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(2019,Calendar.DECEMBER,24);
		assertThat(to.getDate()).isEqualTo(cal.getTime());
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date parsedDate = format.parse("24/12/2019");
		assertThat(to.getDateF()).isEqualTo(parsedDate);
	}

	@Test
	public void shouldCreateEmptyObjectWithDefaultValuesWithWarningNotLoggedForFactory() throws MappingException, MappingNotFoundException, ParseException {
		Mapper mapper = new Mapper();
		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.ALWAYS);
		mapper.add(DefaultValuesFrom.class,DefaultValuesTo.class);
		DefaultValuesFrom from = new DefaultValuesFrom();
		DefaultValuesTo to = mapper.map(from,DefaultValuesTo.class);
		String out = console.getOutString();
		assertThat(out.trim()).contains("");
		assertThat(to.getDateF2()).isNull();
	}

	@Test
	public void shouldCreateEmptyObjectWithDefaultValuesWithWarningLoggedForFactory() throws MappingException, MappingNotFoundException, ParseException {
		Mapper mapper = new Mapper();
		mapper.add(DefaultValuesFrom.class,DefaultValuesTo.class);
		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.ALWAYS);
		DefaultValuesFrom from = new DefaultValuesFrom();
		DefaultValuesTo to = mapper.map(from,DefaultValuesTo.class);
		String out = console.getOutString();
		assertThat(out.trim()).contains("The factory for class defaultvalue.DateFactory2 does not have a constructor accepting a Mapper instance; the factory is ignored.");
		assertThat(to.getDateF2()).isNull();
	}

	@Test
	public void shouldCreateEmptyObjectWithDefaultValuesCustom() throws MappingException, MappingNotFoundException, ParseException {
		Mapper mapper = new Mapper();
		mapper.add(DefaultValuesFrom.class,DefaultValuesTo.class);
//		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.CUSTOM);
		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.INPUT,DefaultValueStrategy.OUTPUT);
		DefaultValuesFrom from = new DefaultValuesFrom();
		DefaultValuesTo to = mapper.map(from,DefaultValuesTo.class);
		assertThat(to.getString()).isEqualTo("string di default");
		assertThat(to.getString2()).isNull();
		assertThat(to.getNumByte()).isEqualTo((byte)42);
		assertThat(to.getNumShort()).isEqualTo((short)42);
		assertThat(to.getNumInteger()).isEqualTo((int)42);
		assertThat(to.getNumLong()).isEqualTo((long)42);
		assertThat(to.getNumFloat()).isEqualTo((float)Math.PI);
		assertThat(to.getNumDouble()).isEqualTo((double)Math.PI);
		assertThat(to.getBool()).isEqualTo(true);
		assertThat(to.getCharacter()).isEqualTo('c');
		assertThat(to.getType()).isEqualTo(DefaultValuesTo.class);
		assertThat(to.getTimeUnit()).isEqualTo(TimeUnit.DAYS);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(2019,Calendar.DECEMBER,24);
		assertThat(to.getDate()).isEqualTo(cal.getTime());
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date parsedDate = format.parse("24/12/2019");
		assertThat(to.getDateF()).isEqualTo(parsedDate);
	}

	@Test
	public void shouldCreateEmptyObjectWithDefaultValuesNever() throws MappingException, MappingNotFoundException, ParseException {
		Mapper mapper = new Mapper();
		mapper.add(DefaultValuesFrom.class,DefaultValuesTo.class);
		mapper.config().setDefaultValueStrategy(DefaultValueStrategy.NEVER);
		DefaultValuesFrom from = new DefaultValuesFrom();
		DefaultValuesTo to = mapper.map(from,DefaultValuesTo.class);
		assertThat(to).hasAllNullFieldsOrProperties();
	}

}
