package testcase.emb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class EMB_DefaultInputStep {
	
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
	public void shouldCreate_BuilderStep_DefaultInput_DefaultInput_Object() {
		Transformer<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.defaultInput("No name");
		assertThat(step).isNotNull()
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","defaultInput");
	}
	@Test
	public void shouldCreate_BuilderStep_DefaultInput_DefaultInput_Class() {
		mapper.config().addDefaultValueSupplier(String.class, ()->"{no value}");
		Transformer<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.defaultInputFor(String.class);
		assertThat(step).isNotNull()
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","defaultInput");
	}

}
