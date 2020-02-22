package testcase.emb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import converter.ConverterDateTimestamp;
import converter.ConverterDateTimestamp2;
import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.Consume;
import es.utils.mapper.factory.builder.DefaultInput;
import es.utils.mapper.factory.builder.DefaultOutput;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.factory.builder.To;
import es.utils.mapper.factory.builder.Transformer;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;
import utils.AlternativeConsole;

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
						.isInstanceOf(Consume.class)
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
						.isInstanceOf(Consume.class)
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

}
