package testcase.emb;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.factory.builder.Builder;
import es.utils.mapper.factory.builder.EMBuilder;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.element.Getter;
import es.utils.mapper.impl.element.Setter;
import es.utils.mapper.impl.object.ClassMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EMB_BuilderStep {

	@Test
	public void shouldReturnStringRappresentation() throws MappingException {
		Getter<Object,String> getter = new Getter<>("getterName",Object::toString);
		Setter<Object,String> setter = new Setter<>("setterName",(a,b)->{});
		Mapper mapper = new Mapper();
		ClassMapper<Object,Object> classMapper = new ClassMapper<>(Object.class,Object.class);
		Builder<Object, String, String, Object> emb = EMBuilder.using(mapper,classMapper).from(getter).to(setter);
		ElementMapper<Object,String,String,Object> em = emb.getElementMapper();
		ElementMapper<Object,String,String,Object> em2 = emb.getElementMapper();
		assertThat(em.toString()).isEqualTo("ElementMapper[elementMapper_id_110,Getter[id=112,name=getterName],Setter[id=108,name=setterName]]");
		assertThat(em==em2).isTrue();
	}
	@Test
	public void shouldReturnStringRappresentationFromFullBuilder() throws MappingException {
		Getter<Object,String> getter = new Getter<>("getterName",Object::toString);
		Setter<Object,String> setter = new Setter<>("setterName",(a,b)->{});
		Mapper mapper = new Mapper();
		ClassMapper<Object,Object> classMapper = new ClassMapper<>(Object.class,Object.class);
		Builder<Object, String, String, Object> emb = EMBuilder.using(mapper,classMapper).from(getter).defaultInput("no value").transform($->$).defaultOutput("no value").to(setter);
		ElementMapper<Object,String,String,Object> em = emb.getElementMapper();
		assertThat(em.toString()).isEqualTo("ElementMapper[elementMapper_id_111,Getter[id=113,name=getterName],Setter[id=109,name=setterName]]");
	}
	
	@Test
	public void shouldReturnElementMapper() throws MappingException {
		Getter<Object,String> getter = new Getter<>("getterName",Object::toString);
		Setter<Object,String> setter = new Setter<>("setterName",(a,b)->{});
		Mapper mapper = new Mapper();
		ClassMapper<Object,Object> mapping = mapper.addForClass(Object.class,Object.class);
		ClassMapper<Object,Object> classMapper = EMBuilder.using(mapper,mapping).from(getter).to(setter).create();
		Optional<ElementMapper<Object,String,String,Object>> em = classMapper.getMapping("getterName","setterName");
		assertThat(em.isPresent()).isTrue();
		assertThat(em.get().toString()).isEqualTo("ElementMapper[elementMapper_id_109,Getter[id=111,name=getterName],Setter[id=107,name=setterName]]");
	}
	
}
