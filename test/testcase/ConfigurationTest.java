package testcase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import annotation.TestAnnotation;
import es.utils.mapper.Mapper;
import es.utils.mapper.configuration.Configuration;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.object.ClassMapper;
import from.FromWithAnnotation;
import to.ToWithAnnotation;
import to.ToWithNoEmptyConstructor;

public class ConfigurationTest {

	@Test
	public void shouldCreateMapper() throws MappingException {
		Mapper mapper = new Mapper();
		Supplier<ToWithNoEmptyConstructor> supplier = ()->ToWithNoEmptyConstructor.withName("Pippo");
		mapper.config().addSupplier(ToWithNoEmptyConstructor.class, supplier);
		ToWithNoEmptyConstructor newInstance = mapper.createNewInstance(ToWithNoEmptyConstructor.class);
		assertThat(newInstance).isNotNull();
		assertThat(newInstance.getName()).isEqualTo("Pippo");
	}

	@Test
	public void shouldReadCustomAnnotation() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(FromWithAnnotation.class,ToWithAnnotation.class);
		mapper.config().useAnnotation(TestAnnotation.class, "name");
		FromWithAnnotation from = new FromWithAnnotation();
		ToWithAnnotation to = mapper.map(from);
		assertThat(to).isNotNull();
		assertThat(to.getName()).isEqualTo("Pippo");
	}
	@Test
	public void shouldThrowMappingExceptionForWrongAnnotationField() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(FromWithAnnotation.class,ToWithAnnotation.class);
		MappingException exception = assertThrows(MappingException.class,()->mapper.config().useAnnotation(TestAnnotation.class, "age"));
		assertThat(exception.getMessage()).isEqualTo("The fieldName of the given annotation does not return String or String[].");
	}
	
	@Test
	public void shouldMakeDeepCopy() throws MappingException, MappingNotFoundException {
		UnaryOperator<String> clonerFnz = s->new String(s.getBytes());
		Mapper mapper = new Mapper();
		mapper.add(FromWithAnnotation.class, ToWithAnnotation.class);
		
		Configuration configurations = mapper.config();
		configurations.setCloner(clonerFnz);
		configurations.useAnnotation(TestAnnotation.class, "name");
		
		FromWithAnnotation from = new FromWithAnnotation();
		ToWithAnnotation to = mapper.map(from);
		assertThat(to).isNotNull();
		assertThat(to.getName()==from.getName()).isFalse();
		assertThat(to.getName()).isEqualTo(from.getName());
	}
	
	@Test
	public void shouldUseDefaultValueSupplier() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		ClassMapper<FromWithAnnotation,ToWithAnnotation> classMapper = mapper.addForClass(FromWithAnnotation.class,ToWithAnnotation.class);
		Configuration configurations = mapper.config();
		configurations.useAnnotation(TestAnnotation.class, "name");
		configurations.addDefaultValueSupplier(CharSequence.class, ()->"<No value>");
		
		FromWithAnnotation from = new FromWithAnnotation(null);
		ToWithAnnotation to1 = mapper.map(from);
		assertThat(to1).isNotNull();
		assertThat(to1.getName()).isEqualTo("<No value>");
		classMapper.addMapping().from("name",FromWithAnnotation::getName).transform(s->(CharSequence)s).to("field",ToWithAnnotation::setField).defaultValueFor(CharSequence.class).create();
		assertThat(classMapper.<String,CharSequence>getMapping("name").isPresent()).isFalse();
		classMapper.<String,CharSequence>getMapping("name","field").get().setDefaultValue(CharSequence.class);
		ToWithAnnotation to2 = mapper.map(from);
		assertThat(to2).isNotNull();
		assertThat(to2.getName()).isEqualTo("<No value>");
	}
	
}
