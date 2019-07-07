package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.From;
import to.To;

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
	
}
