package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.ElementMapperBuilder;
import es.utils.mapper.factory.builder.From;
import es.utils.mapper.factory.builder.To;
import es.utils.mapper.factory.builder.Transformer;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;

public class ElementMapperBuilderTest {

	@Test
	public void shouldCreateBuilderStepFrom() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		From<ClassMapperFromTest,ClassMapperToTest> from = mapping.createElementMapper();
		assertThat(from).isNotNull()
		 				.isExactlyInstanceOf(From.class)
						.hasNoNullFieldsOrProperties()
						.hasFieldOrPropertyWithValue("mapping",mapping)
						.hasFieldOrPropertyWithValue("mapper",mapper);
	}
	@Test
	public void shouldCreateBuilderStepTransformerFromFieldName() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		Transformer<ClassMapperFromTest,String,ClassMapperToTest> transform = mapping.createElementMapper().from("name");
		assertThat(transform).isNotNull()
							 .isInstanceOf(To.class)
							 .isExactlyInstanceOf(Transformer.class)
							 .hasNoNullFieldsOrProperties()
							 .hasFieldOrPropertyWithValue("mapping",mapping)
							 .hasFieldOrPropertyWithValue("mapper",mapper)
							 .hasFieldOrProperty("getter")
							 .hasFieldOrProperty("transformer");
	}
	@Test
	public void shouldCreateBuilderStepTransformerFromFieldHolder() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		FieldHolder fieldHolder = mapper.getFieldsHolderFromCache(ClassMapperFromTest.class).get("name");
		Transformer<ClassMapperFromTest,String,ClassMapperToTest> transform = mapping.createElementMapper().from(fieldHolder);
		assertThat(transform).isNotNull()
							 .isInstanceOf(To.class)
							 .isExactlyInstanceOf(Transformer.class)
							 .hasNoNullFieldsOrProperties()
							 .hasFieldOrPropertyWithValue("mapping",mapping)
							 .hasFieldOrPropertyWithValue("mapper",mapper)
							 .hasFieldOrProperty("getter")
							 .hasFieldOrProperty("transformer");
	}
	@Test
	public void shouldCreateBuilderStepTransformerFromDefaultValue() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		To<ClassMapperFromTest,Void,String,ClassMapperToTest> transform = mapping.createElementMapper().defaultValue("pippo");
		assertThat(transform).isNotNull()
							 .isExactlyInstanceOf(To.class)
							 .hasNoNullFieldsOrProperties()
							 .hasFieldOrPropertyWithValue("mapping",mapping)
							 .hasFieldOrPropertyWithValue("mapper",mapper)
							 .hasFieldOrProperty("getter")
							 .hasFieldOrProperty("transformer");
	}
	@Test
	public void shouldCreateLastBuilderStepFromSetter() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		Setter<ClassMapperToTest,String> setter = new Setter<>("test",ClassMapperToTest::setNameTo);
		ElementMapperBuilder<ClassMapperFromTest,String,String,ClassMapperToTest> emb = mapping.createElementMapper().from("name",in->in.getNameFrom()).noTransform().to(setter);
		assertThat(emb).isNotNull()
						.isExactlyInstanceOf(ElementMapperBuilder.class)
						.hasNoNullFieldsOrProperties()
						.hasFieldOrPropertyWithValue("mapping",mapping)
						.hasFieldOrProperty("elementMapper");
	}
	@Test
	public void shouldCreateLastBuilderStepFromFieldName() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		ElementMapperBuilder<ClassMapperFromTest,String,String,ClassMapperToTest> emb = mapping.createElementMapper().from("name",in->in.getNameFrom()).noTransform().to("surnameTo");
		assertThat(emb).isNotNull()
						.isExactlyInstanceOf(ElementMapperBuilder.class)
						.hasNoNullFieldsOrProperties()
						.hasFieldOrPropertyWithValue("mapping",mapping)
						.hasFieldOrProperty("elementMapper");
	}
	
}
