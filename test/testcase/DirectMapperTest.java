package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.object.DirectMapper;
import from.From;
import to.To;

public class DirectMapperTest {

	@Test
	public void shouldReturnFromClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		mapper.add(String.class,String[].class, s->s.split(" "));
		mapper.build();
		MapperObject<String,String[]> mapping = mapper.getMappingBetween(String.class, String[].class);
		assertThat(mapping.fromClass()).isEqualTo(String.class);
	}
	@Test
	public void shouldReturnToClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		mapper.add(String.class,String[].class, s->s.split(" "));
		mapper.build();
		MapperObject<String,String[]> mapping = mapper.getMappingBetween(String.class, String[].class);
		assertThat(mapping.toClass()).isEqualTo(String[].class);
	}

	@Test
	public void shouldReturnStringRappresentation() throws MappingException {
		DirectMapper<From,To> dm = new DirectMapper<>(From.class,To.class,in->new To());
		assertThat(dm.toString()).isEqualTo("DirectMapper[<class from.From,class to.To>]");
	}
	
}
