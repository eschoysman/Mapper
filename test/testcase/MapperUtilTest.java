package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

import com.sun.javafx.collections.MappingChange.Map;

import es.utils.mapper.utils.MapperUtil;

public class MapperUtilTest {

	@Test
	public void shouldReturnNull() {
		assertThat(MapperUtil.getGenericType((Type)Map.class)).isNull();
	}

}
