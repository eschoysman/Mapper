package testcase;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.object.EnumMapper;

public class EnumMapperTest {

	@Test
	public void shouldReturnFromClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		EnumMapper<TimeUnit, ChronoUnit> mapping = mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		mapper.build();
		assertThat(mapping.fromClass()).isEqualTo(TimeUnit.class);
	}
	@Test
	public void shouldReturnToClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		EnumMapper<TimeUnit, ChronoUnit> mapping = mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		mapper.build();
		assertThat(mapping.toClass()).isEqualTo(ChronoUnit.class);
	}
	@Test
	public void shouldAddNewEnumMapping() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		EnumMapper<TimeUnit, ChronoUnit> mapping = mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		mapper.build();
		assertThat(mapper.map(TimeUnit.NANOSECONDS, ChronoUnit.class)).isNull();
		mapping.add(TimeUnit.NANOSECONDS, ChronoUnit.NANOS);
		assertThat(mapper.map(TimeUnit.NANOSECONDS, ChronoUnit.class)).isEqualTo(ChronoUnit.NANOS);
	}
	@Test
	public void shouldIgnoreEnumMapping() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		EnumMapper<TimeUnit, ChronoUnit> mapping = mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		mapper.build();
		assertThat(mapper.map(TimeUnit.DAYS, ChronoUnit.class)).isEqualTo(ChronoUnit.DAYS);
		assertThat(mapper.map(TimeUnit.MINUTES, ChronoUnit.class)).isEqualTo(ChronoUnit.MINUTES);
		mapping.ignore(TimeUnit.DAYS,TimeUnit.MINUTES);
		assertThat(mapper.map(TimeUnit.DAYS, ChronoUnit.class)).isNull();
		assertThat(mapper.map(TimeUnit.MINUTES, ChronoUnit.class)).isNull();
	}
	@Test
	public void shouldReturnDefaultEnumValue() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		EnumMapper<TimeUnit, ChronoUnit> mapping = mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		mapper.build();
		assertThat(mapper.map(TimeUnit.NANOSECONDS, ChronoUnit.class)).isNull();
		mapping.setDefaultDestinationEnumValue(ChronoUnit.FOREVER);
		assertThat(mapper.map(TimeUnit.NANOSECONDS, ChronoUnit.class)).isEqualTo(ChronoUnit.FOREVER);
	}

}
