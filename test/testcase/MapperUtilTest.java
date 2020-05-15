package testcase;

import es.utils.mapper.utils.MapperUtil;
import from.SpecificTestCaseFrom;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperUtilTest {

	@Test
	public void shouldReturnNullForMapType() throws NoSuchFieldException, SecurityException {
		Type genericType = SpecificTestCaseFrom.class.getDeclaredField("map").getGenericType();
		assertThat(MapperUtil.getGenericType(genericType)).isNull();
	}
	@Test
	public void shouldReturnNull() {
		assertThat(MapperUtil.getGenericType((Type)List.class)).isNull();
	}

}
