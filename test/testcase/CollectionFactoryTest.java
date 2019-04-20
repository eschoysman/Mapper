package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.factory.CollectionFactory;

public class CollectionFactoryTest {

    @Test
	public void shouldCreateCollectionOfInputType() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(TreeSet.class,null);
    	assertThat(newCollection).isInstanceOf(TreeSet.class);
	}
    @Test
	public void shouldCreateArrayList() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),null);
    	assertThat(newCollection).isInstanceOf(ArrayList.class);
	}
    @Test
	public void shouldCreateCollectionOfGivenType() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),LinkedList.class);
    	assertThat(newCollection).isInstanceOf(LinkedList.class);
	}
    @Test
	public void shouldReturnNullAndPrintErrorInCreation() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),List.class);
    	assertThat(newCollection).isNull();
	}

}