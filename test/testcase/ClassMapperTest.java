package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.Factory;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.setter.Setter;
import from.ClassMapperFromTest;
import from.From;
import to.ClassMapperToTest;
import to.To;

public class ClassMapperTest {
	
	@Test
	public void shouldReturnFromClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From, To> mapping = mapper.addForClass(From.class, To.class);
		mapper.build();
		assertThat(mapping.fromClass()).isEqualTo(From.class);
	}
	@Test
	public void shouldReturnToClass() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From, To> mapping = mapper.addForClass(From.class, To.class);
		mapper.build();
		assertThat(mapping.toClass()).isEqualTo(To.class);
	}

	@Test
	public void shouldAddElementMapper() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();
		
		Getter<ClassMapperFromTest, String> getter = Factory.getter("fullNameFrom", f->f.getNameFrom()+" "+f.getSurnameFrom());
		Setter<ClassMapperToTest, String> setter = Factory.setter("fullNameTo", (t, value)->t.setFullName(value));
		
		mapping.addElementMapper(Factory.element(getter,s->s+"!",setter));
		
		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isEqualTo(from.getNameFrom()+" "+from.getSurnameFrom()+"!");
	}
	@Test
	public void shouldAddElementMapperWithoutTransformer() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();
		
		Getter<ClassMapperFromTest, String> getter = Factory.getter("fullNameFrom", "Name not Found");
		Setter<ClassMapperToTest, String> setter = Factory.setter("fullNameTo", (t, value)->t.setFullName(value));
		
		mapping.addElementMapper(getter,setter);
		
		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isEqualTo("Name not Found");
	}
	@Test
	public void shouldAddElementMapperWithTransformer() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();
		
		Getter<ClassMapperFromTest, String> getter = Factory.getter("fullNameFrom", f->f.getNameFrom()+" "+f.getSurnameFrom());
		Setter<ClassMapperToTest, String> setter = Factory.setter("fullNameTo", (t, value)->t.setFullName(value));
		
		mapping.addElementMapper(getter,s->s+"!",setter);
		
		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isEqualTo(from.getNameFrom()+" "+from.getSurnameFrom()+"!");
	}
	@Test
	public void shouldAddFieldMapperWithoutTransformer() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addCustomFieldMapper("nameFrom", in->in.getNameFrom(),
									 "surnameTo", (t,value)->t.setSurnameTo(value));
		
		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getFullName()).isNull();
	}
	@Test
	public void shouldAddFieldMapperWithTransformer() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addCustomFieldMapper("nameFrom", in->in.getNameFrom(),
									 s->s+"!",
									 "surnameTo", (t,value)->t.setSurnameTo(value));
		
		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getNameFrom()+"!");
		assertThat(to.getFullName()).isNull();
	}
//	@Test
//	public void shouldAddCustomTransformer() throws MappingNotFoundException, MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
//				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
//		mapper.build();
//		ClassMapperFromTest from = new ClassMapperFromTest();
//		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
//		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
//		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
//		assertThat(to.getFullName()).isNull();
//
//		mapping.addCustomFieldMapper("name", s->s+"!");
//		
//		to = mapper.map(from, ClassMapperToTest.class);
//		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
//		assertThat(to.getSurnameTo()).isEqualTo(from.getNameFrom()+"!");
//		assertThat(to.getFullName()).isNull();
//	}
	@Test
	public void shouldMapDefautlValue() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addDefaultValue("fullNameTo", (t,value)->t.setFullName("Default Full Name"));

		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isEqualTo("Default Full Name");
	}

	@Test
	public void shouldIgnoreField() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addDefaultValue("fullNameTo", (t,value)->t.setFullName("Default Full Name"));
		mapping.ignore("nameFrom","surnameTo");

		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isNull();
		assertThat(to.getSurnameTo()).isNull();
		assertThat(to.getFullName()).isEqualTo("Default Full Name");
	}
	@Test
	public void shouldIgnoreInputsField() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addDefaultValue("fullNameTo", (t,value)->t.setFullName("Default Full Name"));
		mapping.ignoreInputs("nameFrom","surnameTo");

		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isNull();
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isEqualTo("Default Full Name");
	}
	@Test
	public void shouldIgnoreOutputsField() throws MappingNotFoundException, MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest, ClassMapperToTest> mapping = mapper
				.addForClass(ClassMapperFromTest.class, ClassMapperToTest.class);
		mapper.build();
		ClassMapperFromTest from = new ClassMapperFromTest();
		ClassMapperToTest to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isEqualTo(from.getSurnameFrom());
		assertThat(to.getFullName()).isNull();

		mapping.addDefaultValue("fullNameTo", (t,value)->t.setFullName("Default Full Name"));
		mapping.ignoreOutputs("nameFrom","surnameTo");

		to = mapper.map(from, ClassMapperToTest.class);
		assertThat(to.getNameTo()).isEqualTo(from.getNameFrom());
		assertThat(to.getSurnameTo()).isNull();
		assertThat(to.getFullName()).isEqualTo("Default Full Name");
	}

	@Test
	public void shouldReturnDefaultValue() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From,To> mapping = mapper.addForClass(From.class, To.class);
		To to = new To();
		assertThat(mapping.map(null,to)).isEqualTo(to);
	}
	
}
