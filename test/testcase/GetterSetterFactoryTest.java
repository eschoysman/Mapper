package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.Factory;
import es.utils.mapper.getter.Getter;
import es.utils.mapper.setter.Setter;
import es.utils.mapper.utils.MapperUtil;
import factory.FactoryObject;

public class GetterSetterFactoryTest {

    @Test
	public void shouldValorizeFieldWithAliasName() throws MappingNotFoundException, MappingException {
        FactoryObject fo = new FactoryObject();
        Getter<FactoryObject,String> getter = Factory.getter(FactoryObject.class,"campo");
        Setter<FactoryObject,String> setter = Factory.setter(FactoryObject.class,"campo");
        assertThat(getter.apply(fo)).isNull();
        setter.apply(fo,"Pippo");
        assertThat(getter.apply(fo)).isEqualTo("Pippo");
	}
    @Test
	public void shouldValorizeFieldWithDefaultName() throws MappingNotFoundException, MappingException {
        FactoryObject fo = new FactoryObject();
        Getter<FactoryObject,String> getter = Factory.getter(FactoryObject.class,"field");
        Setter<FactoryObject,String> setter = Factory.setter(FactoryObject.class,"field");
        assertThat(getter.apply(fo)).isNull();
        setter.apply(fo,"Pippo");
        assertThat(getter.apply(fo)).isEqualTo("Pippo");
	}

    @Test
	public void shouldValorizeFieldWithCustomName() throws MappingNotFoundException, MappingException {
        FactoryObject fo = new FactoryObject();
        Getter<FactoryObject,String> getter = Factory.getter("paperino",FactoryObject.class,"field");
        Setter<FactoryObject,String> setter = Factory.setter("paperino",FactoryObject.class,"field");
        assertThat(getter.apply(fo)).isNull();
        setter.apply(fo,"Paperino");
        assertThat(getter.apply(fo)).isEqualTo("Paperino");
	}

    @Test
	public void shouldValorizeFieldWithSuppliedValue() throws MappingNotFoundException, MappingException {
        FactoryObject fo = new FactoryObject();
        Getter<FactoryObject,String> getter = Factory.getter("paperino",()->"Sora");
        Setter<FactoryObject,String> setter = Factory.setter("paperino",FactoryObject.class,"field");
        assertThat(getter.apply(fo)).isEqualTo("Sora");
        setter.apply(fo,"Sora");
        assertThat(getter.apply(fo)).isEqualTo("Sora");
	}

    @Test
	public void shouldValorizeField() throws MappingNotFoundException, MappingException {
        FactoryObject fo = new FactoryObject();
        Field field = MapperUtil.getField(FactoryObject.class,"field");
        Getter<FactoryObject,String> getter = Factory.getter(field);
        Setter<FactoryObject,String> setter1 = Factory.setter(field);
        Setter<FactoryObject,String> setter2 = Factory.setter("name",field);
        assertThat(getter.apply(fo)).isNull();
        setter1.apply(fo,"Sora1");
        assertThat(getter.apply(fo)).isEqualTo("Sora1");
        setter2.apply(fo,"Sora2");
        assertThat(getter.apply(fo)).isEqualTo("Sora2");
	}

}