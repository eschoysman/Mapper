package testcase.emb;

import converter.ConverterDateTimestamp;
import converter.ConverterDateTimestamp2;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.builder.*;
import es.utils.mapper.impl.object.ClassMapper;
import exception.ExampleException;
import from.ClassMapperFromTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import to.ClassMapperToTest;
import utils.AlternativeConsole;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EMB_TransformerStep {
	
	private Mapper mapper;
	private ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping;
	private DefaultInput<ClassMapperFromTest,String,String,ClassMapperToTest> prev;
	
	@BeforeEach
	public void beforeEach() throws MappingException {
		mapper = new Mapper();
		mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		prev = EMBuilder.using(mapper,mapping).from("name",ClassMapperFromTest::getNameFrom);
	}
	
	@Test
	public void shouldCreate_TransformerStep_Transformer_cast() {
		Transformer<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.cast(String.class);
		assertThat(step).isNotNull()
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","transformer");
	}
	@Test
	public void shouldCreate_TransformerStep_Transform_Function_And_Class() throws MappingException {
		mapper.config().addDefaultValueSupplier(String.class, ()->"{no value}");
		Transformer<ClassMapperFromTest,String,Timestamp,ClassMapperToTest> step = prev.transform(s->new Date()).transform(ConverterDateTimestamp.class);
		assertThat(step).isNotNull()
				.isInstanceOf(Transformer.class)
				.isInstanceOf(DefaultOutput.class)
				.isInstanceOf(To.class)
				.isExactlyInstanceOf(EMBuilder.class)
				.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","transformer");
	}
	@Test
	public void shouldCreate_TransformerStep_Transform_Function_And_ClassKO() throws MappingException {
		mapper.config().addDefaultValueSupplier(String.class, ()->"{no value}");
		AlternativeConsole console = new AlternativeConsole();
		assertThrows(MappingException.class,()->prev.transform(s->new Date()).transform(ConverterDateTimestamp2.class));
		String message = console.getOutString();
		console.reset();
		assertThat(message.trim()).isEqualTo("WARNING - The converter for class converter.ConverterDateTimestamp2 does not have a empty public contructor or a constructor accepting a Mapper instance; the converter is ignored.");
	}
	@Test
	public void shouldCreate_TransformerStep_Transform_Predicate_Function() throws MappingException, MappingNotFoundException {
		Transformer<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.transform(Objects::isNull,$->"{no value}");
		assertThat(step).isNotNull()
				.isInstanceOf(Transformer.class)
				.isInstanceOf(DefaultOutput.class)
				.isInstanceOf(To.class)
				.isExactlyInstanceOf(EMBuilder.class)
				.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","transformer");
		step.to("nameTo",ClassMapperToTest::setNameTo).create();
		ClassMapperFromTest from = ClassMapperFromTest.empty();
		ClassMapperToTest to = mapper.map(from,ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo("{no value}");
		assertThat(to.getSurnameTo()).isNull();
	}
	@Test
	public void shouldCreate_TransformerStep_Transform_Predicate_Function_Function() throws MappingException, MappingNotFoundException {
		Transformer<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.transform(Objects::isNull,$->"{no value}",$->"value");
		assertThat(step).isNotNull()
				.isInstanceOf(Transformer.class)
				.isInstanceOf(DefaultOutput.class)
				.isInstanceOf(To.class)
				.isExactlyInstanceOf(EMBuilder.class)
				.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","transformer");
		step.to("nameTo",ClassMapperToTest::setNameTo).create();
		ClassMapperFromTest from = ClassMapperFromTest.empty();
		ClassMapperToTest to = mapper.map(from,ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo("{no value}");
		assertThat(to.getSurnameTo()).isNull();
		from.setNameFrom("pippo");
		to = mapper.map(from,ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo("value");
		assertThat(to.getSurnameTo()).isNull();
	}

}
