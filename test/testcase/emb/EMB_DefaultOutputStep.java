package testcase.emb;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.DefaultOutput;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.factory.builder.To;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import to.ClassMapperToTest;

import static org.assertj.core.api.Assertions.assertThat;

public class EMB_DefaultOutputStep {

	private Mapper mapper;
	private ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping;
	private DefaultOutput<ClassMapperFromTest,String,String,ClassMapperToTest> prev;
	
	@BeforeEach
	public void beforeEach() throws MappingException {
		mapper = new Mapper();
		mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		prev = EMBuilder.using(mapper,mapping).from("name",ClassMapperFromTest::getNameFrom);
	}
	
	@Test
	public void shouldCreate_BuilderStep_DefaultInput_DefaultInput_Object() {
		To<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.defaultOutput("No name");
		assertThat(step).isNotNull()
						.isInstanceOf(To.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","defaultOutput");
	}
	@Test
	public void shouldCreate_BuilderStep_DefaultInput_DefaultInput_Class() {
		mapper.config().addDefaultValueSupplier(String.class, ()->"{no value}");
		To<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.defaultOutputFor(String.class);
		assertThat(step).isNotNull()
						.isInstanceOf(To.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","defaultOutput");
	}

}
