package testcase;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.FromPrimitivesWrap;
import org.junit.jupiter.api.Test;
import to.ToPrimitivesWrap;

import static org.assertj.core.api.Assertions.assertThat;

public class PrimitiveWrapTest {

	@Test
	public void shouldCreateMapper() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(FromPrimitivesWrap.class,ToPrimitivesWrap.class);
		FromPrimitivesWrap from = new FromPrimitivesWrap();
		from.setN1(1);
		from.setN2(2);
		from.setN3(3);
		from.setN4(4);

		ToPrimitivesWrap to = mapper.map(from,ToPrimitivesWrap.class);

		assertThat(to.getN1()).isEqualTo(1);
		assertThat(to.getN2()).isEqualTo(2);
		assertThat(to.getN3()).isEqualTo(3);
		assertThat(to.getN4()).isEqualTo(4);

	}

}
