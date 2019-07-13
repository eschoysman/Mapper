package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import annotation.TestAnnotation;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import from.FromWithAnnotation;
import to.ToWithAnnotation;
import to.ToWithNoEmptyConstructor;

public class ConfigurationTest {

	@Test
	public void shouldCreateMapper() throws MappingException {
		Mapper mapper = new Mapper();
		Supplier<ToWithNoEmptyConstructor> supplier = ()->ToWithNoEmptyConstructor.withName("Pippo");
		mapper.getConfig().addSuplier(ToWithNoEmptyConstructor.class, supplier);
		ToWithNoEmptyConstructor newInstance = mapper.createNewInstance(ToWithNoEmptyConstructor.class);
		assertThat(newInstance).isNotNull();
		assertThat(newInstance.getName()).isEqualTo("Pippo");
	}
	
	@Test
	public void shouldReadAnnotation() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(FromWithAnnotation.class,ToWithAnnotation.class);
		mapper.getConfig().addAnnotation(TestAnnotation.class, "name");
		FromWithAnnotation from = new FromWithAnnotation();
		ToWithAnnotation to = mapper.map(from);
		assertThat(to).isNotNull();
		assertThat(to.getName()).isEqualTo("Pippo");
	}

}
