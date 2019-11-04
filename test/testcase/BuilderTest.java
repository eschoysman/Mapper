package testcase;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;

public class BuilderTest {

	@Test
	public void shouldCreateMapper() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		ClassMapperFromTest from = new ClassMapperFromTest();
		mapping.addMapping().from("name",ClassMapperFromTest::getNameFrom)
									 .transform(String::toLowerCase)
									 .transform(s->s.length())
									 .to("name", ClassMapperToTest::setAge)
									 .create();
    	ClassMapperToTest to = mapper.map(from);
    	assertThat(to).isNotNull().hasFieldOrPropertyWithValue("nameTo","Pippo")
								  .hasFieldOrPropertyWithValue("surnameTo","Sora")
								  .hasFieldOrPropertyWithValue("age",5);
	}
	@Test
	public void shouldCreateMapperWithConsumerAndNoSetter() throws MappingException, IOException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		ClassMapperFromTest from = new ClassMapperFromTest();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));
    	mapping.addMapping().from("name",ClassMapperFromTest::getNameFrom)
									 .consume(System.out::print)
									 .create();
    	ClassMapperToTest to = mapper.map(from);
    	String outString = out.toString();
    	out.flush();
    	out.close();
    	System.setOut(originalOut);
    	assertThat(outString).isNotNull().isEqualTo(from.getNameFrom());
    	assertThat(to).isNotNull().hasFieldOrPropertyWithValue("nameTo","Pippo")
								  .hasFieldOrPropertyWithValue("surnameTo","Sora")
								  .hasFieldOrPropertyWithValue("age",0);
	}
	@Test
	public void shouldCreateMapperWithTransformAndConsumerAndNoSetter() throws MappingException, IOException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		ClassMapperFromTest from = new ClassMapperFromTest();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));
    	mapping.addMapping().from("name",ClassMapperFromTest::getNameFrom)
    								 .transform(String::toUpperCase)
									 .consume(System.out::print)
									 .create();
    	ClassMapperToTest to = mapper.map(from);
    	String outString = out.toString();
    	out.flush();
    	out.close();
    	System.setOut(originalOut);
    	assertThat(outString).isNotNull().isEqualTo(from.getNameFrom().toUpperCase());
    	assertThat(to).isNotNull().hasFieldOrPropertyWithValue("nameTo","Pippo")
								  .hasFieldOrPropertyWithValue("surnameTo","Sora")
								  .hasFieldOrPropertyWithValue("age",0);
	}
	@Test
	public void shouldThrowExceptionInConsumer() throws MappingException, IOException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		ClassMapperFromTest from = new ClassMapperFromTest();
    	mapping.addMapping().from("name",ClassMapperFromTest::getNameFrom)
									 .<String>transform(n->null)
									 .consume(String::toString)
									 .create();
    	MappingException exception = assertThrows(MappingException.class, ()->mapper.map(from));
    	assertThat(exception.getMessage()).isNotNull().startsWith("java.lang.RuntimeException: java.lang.RuntimeException: java.lang.RuntimeException: java.lang.NullPointerException");
	}

}
