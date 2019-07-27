package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    	assertThat(newCollection).isInstanceOf(TreeSet.class).isEmpty();
	}
    @Test
	public void shouldCreateArrayList() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),null);
    	assertThat(newCollection).isInstanceOf(ArrayList.class).isEmpty();
	}
    @Test
	public void shouldCreateCollectionOfGivenType() throws MappingNotFoundException, MappingException {
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),LinkedList.class);
    	assertThat(newCollection).isInstanceOf(LinkedList.class).isEmpty();
	}
    @Test
	public void shouldReturnNullAndPrintErrorInCreation() throws MappingNotFoundException, MappingException, IOException {
    	ByteArrayOutputStream err = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		System.setErr(new PrintStream(err));
    	Collection<Object> newCollection = CollectionFactory.create(Arrays.asList().getClass(),List.class);
    	String errString = err.toString();
    	err.flush();
    	err.close();
    	System.setErr(originalErr);
    	assertThat(newCollection).isNull();
    	assertThat(errString).startsWith("java.lang.NoSuchMethodException: java.util.List.<init>()");
	}

}