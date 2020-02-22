package testcase.emb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.Builder;
import es.utils.mapper.factory.builder.Consume;
import es.utils.mapper.factory.builder.DefaultInput;
import es.utils.mapper.factory.builder.DefaultOutput;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.factory.builder.From;
import es.utils.mapper.factory.builder.To;
import es.utils.mapper.factory.builder.Transformer;
import es.utils.mapper.holder.FieldHolder;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;

public class EMB_FromStep {
	
	private Mapper mapper;
	private ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping;
	private From<ClassMapperFromTest,ClassMapperToTest> prev;
	
	@BeforeEach
	public void beforeEach() throws MappingException {
		mapper = new Mapper();
		mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		prev = EMBuilder.using(mapper,mapping);
	}

	// EmptyFrom.class
	@Test
	public void shouldCreate_BuilderStep_EmptyFrom_DefaultOutput() {
		Builder<ClassMapperFromTest,Void,String,ClassMapperToTest> step = prev.defaultOutput("fullNameTo",(t,value)->t.setFullName("Default Full Name"));
		assertThat(step).isNotNull()
		 				.isInstanceOf(Builder.class)
		 				.isExactlyInstanceOf(EMBuilder.class)
						.hasNoNullFieldsOrPropertiesExcept("_elementMapper","defaultInput");
	}
	@Test
	public void shouldCreate_BuilderStep_EmptyFrom_DefaultOutputValue() {
		To<ClassMapperFromTest,Void,String,ClassMapperToTest> step = prev.defaultOutputValue("pippo");
		assertThat(step).isNotNull()
		 				.isInstanceOf(To.class)
		 				.isExactlyInstanceOf(EMBuilder.class)
						.hasNoNullFieldsOrPropertiesExcept("_elementMapper","defaultInput","setter");
	}
	@Test
	public void shouldCreate_BuilderStep_EmptyFrom_FromEmpty() {
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.fromEmpty();
		assertThat(step).isNotNull()
		 				.isInstanceOf(DefaultInput.class)
		 				.isInstanceOf(Transformer.class)
		 				.isInstanceOf(DefaultOutput.class)
		 				.isInstanceOf(To.class)
		 				.isInstanceOf(Consume.class)
		 				.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}

	// From.class
	@Test
	public void shouldCreate_BuilderStep_From_From_String() {
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.from("name");
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}
	@Test
	public void shouldCreate_BuilderStep_From_From_String_NPE() {
		assertThrows(NullPointerException.class, ()->prev.from("pippo"));
	}
	@Test
	public void shouldCreate_BuilderStep_From_From_String2() {
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.from("publicField");
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}
	@Test
	public void shouldCreate_BuilderStep_From_From_String_Function() {
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.from("name",ClassMapperFromTest::getNameFrom);
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}
	@Test
	public void shouldCreate_BuilderStep_From_From_FieldHolder() {
		FieldHolder fieldHolder = mapper.getFieldsHolderFromCache(ClassMapperFromTest.class).get("name");
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.from(fieldHolder);
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}
	@Test
	public void shouldCreate_BuilderStep_From_From_String_Supplier() {
		DefaultInput<ClassMapperFromTest,Object,Object,ClassMapperToTest> step = prev.from("name",()->"Pippo");
		assertThat(step).isNotNull()
						.isInstanceOf(DefaultInput.class)
						.isInstanceOf(Transformer.class)
						.isInstanceOf(DefaultOutput.class)
						.isInstanceOf(To.class)
						.isInstanceOf(Consume.class)
						.isExactlyInstanceOf(EMBuilder.class)
						.hasAllNullFieldsOrPropertiesExcept("mapper","mapping","getter");
	}
	
}
