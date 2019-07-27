package testcase;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.impl.object.ClassMapper;
import from.ClassMapperFromTest;
import to.ClassMapperToTest;

public class BuilderTest {

	@Test
	public void shouldCreateMapper() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
		mapping.createElementMapper().from("name",ClassMapperFromTest::getNameFrom)
									 .transform(s->s.length())
									 .to("name", ClassMapperToTest::setAge)
									 .build()
			   .createElementMapper();
		
	}

}
