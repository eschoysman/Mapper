package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.utils.mapper.utils.MapperUtil;
import from.SpecificTestCaseFrom;

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
