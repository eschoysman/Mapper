package testcase.emb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.factory.builder.From;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;

public class EMB_Step0 {
	
	@Test
	public void shouldCreateEMBuilderStepFrom() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		From<ClassMapperFromTest,ClassMapperToTest> step1 = EMBuilder.using(mapper,mapping);
		assertThat(step1).isNotNull()
						.isExactlyInstanceOf(EMBuilder.class)
		 				.isInstanceOf(From.class)
						.hasFieldOrProperty("mapper")
						.hasFieldOrProperty("mapping")
						.hasFieldOrProperty("getter")
						.hasFieldOrProperty("defaultInput")
						.hasFieldOrProperty("transformer")
						.hasFieldOrProperty("defaultOutput")
						.hasFieldOrProperty("setter")
						.hasFieldOrProperty("_elementMapper")
						.hasFieldOrPropertyWithValue("mapper",mapper)
						.hasFieldOrPropertyWithValue("mapping",mapping)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping");
	}

}
