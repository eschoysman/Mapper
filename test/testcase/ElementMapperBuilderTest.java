//package testcase;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.exception.MappingException;
//import es.utils.mapper.factory.builderOld.Default;
//import es.utils.mapper.factory.builderOld.ElementMapperBuilder;
//import es.utils.mapper.factory.builderOld.From;
//import es.utils.mapper.factory.builderOld.To;
//import es.utils.mapper.factory.builderOld.Transformer;
//import es.utils.mapper.holder.FieldHolder;
//import es.utils.mapper.impl.element.ElementMapper;
//import es.utils.mapper.impl.element.Getter;
//import es.utils.mapper.impl.element.Setter;
//import es.utils.mapper.impl.object.ClassMapper;
//import from.ClassMapperFromTest;
//import to.ClassMapperToTest;
//
//public class ElementMapperBuilderTest {
//
//	@Test
//	public void shouldCreateBuilderStepFrom() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		From<ClassMapperFromTest,ClassMapperToTest> from = mapping.addMapping();
//		assertThat(from).isNotNull()
//		 				.isExactlyInstanceOf(From.class)
//						.hasNoNullFieldsOrProperties()
//						.hasFieldOrPropertyWithValue("mapping",mapping)
//						.hasFieldOrPropertyWithValue("mapper",mapper);
//	}
//	@Test
//	public void shouldCreateBuilderStepTransformerFromFieldName() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		Transformer<ClassMapperFromTest,String,ClassMapperToTest> transform = mapping.addMapping().from("name");
//		assertThat(transform).isNotNull()
//							 .isInstanceOf(To.class)
//							 .isExactlyInstanceOf(Transformer.class)
//							 .hasNoNullFieldsOrProperties()
//							 .hasFieldOrPropertyWithValue("mapping",mapping)
//							 .hasFieldOrPropertyWithValue("mapper",mapper)
//							 .hasFieldOrProperty("getter")
//							 .hasFieldOrProperty("transformer");
//	}
//	@Test
//	public void shouldCreateBuilderStepTransformerFromFieldHolder() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		FieldHolder fieldHolder = mapper.getFieldsHolderFromCache(ClassMapperFromTest.class).get("name");
//		Transformer<ClassMapperFromTest,String,ClassMapperToTest> transform = mapping.addMapping().from(fieldHolder);
//		assertThat(transform).isNotNull()
//							 .isInstanceOf(To.class)
//							 .isExactlyInstanceOf(Transformer.class)
//							 .hasNoNullFieldsOrProperties()
//							 .hasFieldOrPropertyWithValue("mapping",mapping)
//							 .hasFieldOrPropertyWithValue("mapper",mapper)
//							 .hasFieldOrProperty("getter")
//							 .hasFieldOrProperty("transformer");
//	}
//	@Test
//	public void shouldCreateBuilderStepTransformerFromDefaultValue() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		To<ClassMapperFromTest,Void,String,ClassMapperToTest> transform = mapping.addMapping().defaultValue("pippo");
//		assertThat(transform).isNotNull()
//							 .isExactlyInstanceOf(To.class)
//							 .hasNoNullFieldsOrProperties()
//							 .hasFieldOrPropertyWithValue("mapping",mapping)
//							 .hasFieldOrPropertyWithValue("mapper",mapper)
//							 .hasFieldOrProperty("getter")
//							 .hasFieldOrProperty("transformer");
//	}
//	@Test
//	public void shouldCreateLastBuilderStepFromSetter() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		Setter<ClassMapperToTest,String> setter = new Setter<>("test",ClassMapperToTest::setNameTo);
//		Default<ClassMapperFromTest,String,String,ClassMapperToTest> emb = mapping.addMapping().from("name",in->in.getNameFrom()).noTransform().to(setter);
//		assertThat(emb).isNotNull()
//						.isExactlyInstanceOf(Default.class)
//						.hasNoNullFieldsOrPropertiesExcept("defaultValue")
//						.hasFieldOrPropertyWithValue("mapping",mapping)
//						.hasFieldOrProperty("elementMapper");
//	}
//	@Test
//	public void shouldCreateLastBuilderStepFromFieldName() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		Default<ClassMapperFromTest,String,String,ClassMapperToTest> emb = mapping.addMapping().from("name",in->in.getNameFrom()).noTransform().to("surnameTo");
//		assertThat(emb).isNotNull()
//						.isExactlyInstanceOf(Default.class)
//						.hasNoNullFieldsOrPropertiesExcept("defaultValue")
//						.hasFieldOrPropertyWithValue("mapping",mapping)
//						.hasFieldOrProperty("elementMapper");
//	}
//	@Test
//	public void shouldCreateEmptyLastBuilderStepFromFieldName() throws MappingException {
//		Mapper mapper = new Mapper();
//		ClassMapper<ClassMapperFromTest,ClassMapperToTest> mapping = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class);
//		ElementMapperBuilder<ClassMapperFromTest,String,Void,ClassMapperToTest> emb = mapping.addMapping().from("name",in->in.getNameFrom()).consume(s->System.out.println(s));
//		assertThat(emb).isNotNull()
//						.isInstanceOf(ElementMapperBuilder.class)
//						.hasNoNullFieldsOrPropertiesExcept("defaultValue")
//						.hasFieldOrPropertyWithValue("mapping",mapping)
//						.hasFieldOrProperty("elementMapper");
//	}
//
//	@Test
//	public void shouldReturnStringRappresentation() throws MappingException {
//		Getter<Object,String> getter = new Getter<>("getterName",Object::toString);
//		Setter<Object,String> setter = new Setter<>("setterName",(a,b)->{});
//		Mapper mapper = new Mapper();
//		ClassMapper<Object,Object> classMapper = new ClassMapper<>(Object.class,Object.class);
//		ElementMapper<Object,String,String,Object> em = From.using(mapper,classMapper).from(getter).to(setter).getElementMapper();
//		assertThat(em.toString()).isEqualTo("ElementMapper[Getter[name=getterName],Setter[name=setterName]]");
//	}
//	@Test
//	public void shouldReturnElementMapper() throws MappingException {
//		Getter<Object,String> getter = new Getter<>("getterName",Object::toString);
//		Setter<Object,String> setter = new Setter<>("setterName",(a,b)->{});
//		Mapper mapper = new Mapper();
//		ClassMapper<Object,Object> classMapper = mapper.addForClass(Object.class,Object.class).addMapping().from(getter).to(setter).create();
//		Optional<ElementMapper<Object,String,String,Object>> em = classMapper.getMapping("getterName","setterName");
//		assertThat(em.isPresent()).isTrue();
//		assertThat(em.get().toString()).isEqualTo("ElementMapper[Getter[name=getterName],Setter[name=setterName]]");
//	}
//	
//}
