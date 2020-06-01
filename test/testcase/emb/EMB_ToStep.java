package testcase.emb;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.*;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import to.ClassMapperToTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EMB_ToStep {
	
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
	public void shouldCreate_BuilderStep_To_To_String() {
		Builder<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.to("name");
		assertThat(step).isNotNull()
						.isInstanceOf(Builder.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","setter");
	}
	@Test
	public void shouldCreate_BuilderStep_To_To_String_NPE() {
		assertThrows(NullPointerException.class, ()->prev.to("pippo"));
	}
	@Test
	public void shouldCreate_BuilderStep_To_To_String2() {
		Builder<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.to("publicField");
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","setter");
	}
	@Test
	public void shouldCreate_BuilderStep_To_To_String_Function() {
		Builder<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.to("name",ClassMapperToTest::setNameTo);
		assertThat(step).isNotNull()
						.isInstanceOf(Builder.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","setter");
	}
	@Test
	public void shouldCreate_BuilderStep_To_To_FieldHolder() {
		FieldHolder fieldHolder = mapper.getFieldsHolderFromCache(ClassMapperToTest.class).get("name");
		Builder<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.to(fieldHolder);
		assertThat(step).isNotNull()
						.isInstanceOf(Builder.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","setter");
	}
	@Test
	public void shouldCreate_BuilderStep_To_ToEmpty() {
		Builder<ClassMapperFromTest,String,String,ClassMapperToTest> step = prev.toEmpty();
		assertThat(step).isNotNull()
						.isInstanceOf(Builder.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter","setter");
	}

}
