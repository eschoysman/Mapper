package testcase;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.CustomException;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;
import exception.EmptyExampleException;
import exception.ExampleException;
import from.ClassMapperFromTest;
import from.From;
import from.ImplFrom;
import from.SubFrom;
import org.junit.jupiter.api.Test;
import to.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapperTest {

	/* Constructor Method */
	@Test
	public void shouldCreateMapper() {
		Mapper mapper = new Mapper();
		assertThat(mapper.getAllMappings()).isNotNull();
		assertThat(mapper.getAllMappings()).isEmpty();
		assertThat(mapper.getMapperName()).isEqualTo("defaultMapper");
	}
	@Test
	public void shouldCreateMapperWithName() {
		Mapper mapper = new Mapper("CustomMapperName");
		assertThat(mapper.getAllMappings()).isNotNull();
		assertThat(mapper.getAllMappings()).isEmpty();
		assertThat(mapper.getMapperName()).isEqualTo("CustomMapperName");
	}
	
	/* Add Method */
	@Test
	public void shouldCreateClassMapper() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(1);
		
		MapperObject<From, To> mappingBetweenFromTo = mapper.getMappingBetween(From.class, To.class);
		assertThat(mappingBetweenFromTo).isNotNull().isInstanceOf(ClassMapper.class);
		MapperObject<To, From> mappingBetweenToFrom = mapper.getMappingBetween(To.class, From.class);
		assertThat(mappingBetweenToFrom).isNull();
	}
	@Test
	public void shouldAddClassMapper() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From,To> classMapper = new ClassMapper<>(From.class,To.class);
		mapper.add(classMapper);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(1);
		
		MapperObject<From, To> mappingBetweenFromTo = mapper.getMappingBetween(From.class, To.class);
		assertThat(mappingBetweenFromTo).isNotNull().isInstanceOf(ClassMapper.class);
		MapperObject<To, From> mappingBetweenToFrom = mapper.getMappingBetween(To.class, From.class);
		assertThat(mappingBetweenToFrom).isNull();
	}
	@Test
	public void shouldCreateMapperForEnum() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(TimeUnit.class, ChronoUnit.class);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(1);
		MapperObject<TimeUnit,ChronoUnit> mappingBetweenTimeChrono = mapper.getMappingBetween(TimeUnit.class, ChronoUnit.class);
		assertThat(mappingBetweenTimeChrono).isNotNull().isInstanceOf(EnumMapper.class);
		MapperObject<ChronoUnit,TimeUnit> mappingBetweenChronoTime = mapper.getMappingBetween(ChronoUnit.class, TimeUnit.class);
		assertThat(mappingBetweenChronoTime).isNull();
	}
	@Test
	public void shouldCreateEnumMapper() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.addForEnum(TimeUnit.class, ChronoUnit.class);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(1);
		
		MapperObject<TimeUnit,ChronoUnit> mappingBetweenTimeChrono = mapper.getMappingBetween(TimeUnit.class, ChronoUnit.class);
		assertThat(mappingBetweenTimeChrono).isNotNull().isInstanceOf(EnumMapper.class);
		MapperObject<ChronoUnit,TimeUnit> mappingBetweenChronoTime = mapper.getMappingBetween(ChronoUnit.class, TimeUnit.class);
		assertThat(mappingBetweenChronoTime).isNull();
	}
	@Test
	public void shouldReturnNullMapper() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(String.class, ChronoUnit.class);
		assertThat(mapper.getAllMappings()).isEmpty();
		
		MapperObject<String,ChronoUnit> mappingBetween = mapper.getMappingBetween(String.class, ChronoUnit.class);
		assertThat(mappingBetween).isNull();
	}
	@Test
	public void shouldThrowMappingExceptionWithNullMapping() {
		Mapper mapper = new Mapper();
		MappingException exception = null;
		exception = assertThrows(MappingException.class, ()->mapper.add(null, ChronoUnit.class));
		assertThat(exception.getMessage()).isEqualTo("From class is null");
		exception = assertThrows(MappingException.class, ()->mapper.add(ChronoUnit.class, null));
		assertThat(exception.getMessage()).isEqualTo("To class is null");
		exception = assertThrows(MappingException.class, ()->mapper.add(null, null));
		assertThat(exception.getMessage()).isEqualTo("From class and To class are null");
	}
	@Test
	public void shouldThrowMappingExceptionWithInterface() {
		Mapper mapper = new Mapper();
		MappingException exception = null;
		exception = assertThrows(MappingException.class, ()->mapper.add(List.class, ChronoUnit.class));
		assertThat(exception.getMessage()).isEqualTo("From class or To class is an interface. Please provide a concrete class.");
		exception = assertThrows(MappingException.class, ()->mapper.add(ChronoUnit.class, List.class));
		assertThat(exception.getMessage()).isEqualTo("From class or To class is an interface. Please provide a concrete class.");
		exception = assertThrows(MappingException.class, ()->mapper.add(List.class, List.class));
		assertThat(exception.getMessage()).isEqualTo("From class or To class is an interface. Please provide a concrete class.");
	}
	
	@Test
	public void shouldCreateDirectMapper() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(String.class,String[].class, s->new String[]{s});
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(1);
		assertThat(mapper.getMappingBetween(String.class,String[].class)).isNotNull().isInstanceOf(DirectMapper.class);
		assertThat(mapper.getMappingBetween(String[].class,String.class)).isNull();
	}
	
	/* AddBidirectional Method */
	@Test
	public void shouldCreateClassMapperBidirectional() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.addBidirectional(From.class, To.class);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(2);
		
		MapperObject<From, To> mappingBetweenFromTo = mapper.getMappingBetween(From.class, To.class);
		assertThat(mappingBetweenFromTo).isNotNull().isInstanceOf(ClassMapper.class);
		MapperObject<To, From> mappingBetweenToFrom = mapper.getMappingBetween(To.class, From.class);
		assertThat(mappingBetweenToFrom).isNotNull().isInstanceOf(ClassMapper.class);
	}
	@Test
	public void shouldCreateEnumMapperBidirectional() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.addBidirectional(TimeUnit.class, ChronoUnit.class);
		assertThat(mapper.getAllMappings()).isNotEmpty();
		assertThat(mapper.getAllMappings().size()).isEqualTo(2);
		
		MapperObject<TimeUnit,ChronoUnit> mappingBetweenTimeChrono = mapper.getMappingBetween(TimeUnit.class, ChronoUnit.class);
		assertThat(mappingBetweenTimeChrono).isNotNull().isInstanceOf(EnumMapper.class);
		MapperObject<ChronoUnit,TimeUnit> mappingBetweenChronoTime = mapper.getMappingBetween(ChronoUnit.class, TimeUnit.class);
		assertThat(mappingBetweenChronoTime).isNotNull().isInstanceOf(EnumMapper.class);
	}
	
	/* Map Method */
	/* Map Method Object OK */
	@Test
	public void shouldMapExistingMappingBetweenClasses() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		assertThat(mapper.map(from, To.class)).isNotNull().isInstanceOf(To.class);
	}
	@Test
	public void shouldMapExistingMappingFromInputOnly() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To result = mapper.map(from);
		assertThat(result).isNotNull().isInstanceOf(To.class);
	}
	@Test
	public void shouldReturnNullFromNullInputOnly() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		To result = mapper.map(null);
		assertThat(result).isNull();
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionFromInputOnly() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map("ciao"));
		assertThat(exception.getMessage()).contains("Found 0 mapping(s) from class java.lang.String. Cannot uniquely map the input ciao.");
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionFromInputOnlyAndMultipleMappingPresent() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		mapper.add(From.class, String.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map(from));
		assertThat(exception.getMessage()).contains("Found 2 mapping(s) from class from.From. Cannot uniquely map the input "+from+".");
	}
	@Test
	public void shouldNotMapWithNullInput() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		assertThat(mapper.map(null, To.class)).isNull();
	}
	@Test
	public void shouldThrowMappingExceptionWithNullClassDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		Class<?> myType = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(from, myType));
		assertThat(exception.getMessage()).contains(MessageFormat.format("Error mapping {0}: Destination class cannot be null",from));
	}
	@Test
	public void shouldMapFromToWithoutDefaultValues() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = new To();
		To mappedTo = mapper.map(from, to);
		assertThat(mappedTo).isNotNull().isEqualTo(to);
	}
	@Test
	public void shouldMapFromToWithNullStarter() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = null;
		To to = new To();
		To mappedTo = mapper.map(from, to);
		assertThat(mappedTo).isNotNull();
		assertThat(mappedTo.equals(to)).isTrue();
	}
	@Test
	public void shouldMapFromToWithDefaultValues() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = new To();
		to.setName("Sora");
		To mappedTo = mapper.map(from, to);
		assertThat(mappedTo).isNotNull().isEqualTo(to);
		assertThat(mappedTo.getName()).isEqualTo("Pippo");
	}

	@Test
	public void shouldThrowMappingExceptionWithNullDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(from, to));
		assertThat(exception.getMessage()).isEqualTo(MessageFormat.format("Error mapping {0}: Destination object cannot be null",from));
	}
	@Test
	public void shouldThrowExceptionWithNullValues() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		String nullValue = null;
		assertThat(mapper.map(null, nullValue)).isNull();
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionWithNotExistingMappingByClass() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map(from, To.class));
		assertThat(exception.getMessage()).contains("WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tnone\n" + 
				"Existing source mappings to class to.To:\n" +
				"\tnone\n" + 
				"Other existing mappings:\n" +
				"\tclass java.time.temporal.ChronoUnit -> class java.util.concurrent.TimeUnit\n"
				+ "");
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionWithNotExistingMappingByClass2() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, TimeUnit.class,from->TimeUnit.DAYS);
		mapper.add(ChronoUnit.class, To.class,$->new To());
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map(from, To.class));
		assertThat(exception.getMessage()).contains("WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tclass java.util.concurrent.TimeUnit\n" + 
				"Existing source mappings to class to.To:\n" +
				"\tclass java.time.temporal.ChronoUnit\n" + 
				"Other existing mappings:\n" +
				"\tnone\n"
				+ "");
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionWithNotExistingMappingByObject() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map(from, new To()));
		assertThat(exception.getMessage()).contains("WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tnone\n" + 
				"Existing source mappings to class to.To:\n" +
				"\tnone\n" + 
				"Other existing mappings:\n" +
				"\tclass java.time.temporal.ChronoUnit -> class java.util.concurrent.TimeUnit\n"
				+ "");
	}

	@Test
	public void shouldThrowCustomExceptionWithNullDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = null;
		ExampleException exception = assertThrows(ExampleException.class, ()->mapper.map(from, to, CustomException.forType(ExampleException.class)));
		assertThat(exception.getMessage()).isEqualTo(MessageFormat.format("es.utils.mapper.exception.MappingException: Error mapping {0}: Destination object cannot be null",from));
	}
	@Test
	public void shouldThrowCustomExceptionWithNotExistingMappingByClass() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		ExampleException exception = assertThrows(ExampleException.class, ()->mapper.map(from, To.class, CustomException.forType(ExampleException.class)));
		assertThat(exception.getMessage()).contains("es.utils.mapper.exception.MappingNotFoundException: WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tnone\n" +
				"Existing source mappings to class to.To:\n" +
				"\tnone\n" +
				"Other existing mappings:\n" +
				"\tclass java.time.temporal.ChronoUnit -> class java.util.concurrent.TimeUnit\n"
				+ "");
	}
	@Test
	public void shouldThrowCustomExceptionWithNotExistingMappingByClass2() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, TimeUnit.class,from->TimeUnit.DAYS);
		mapper.add(ChronoUnit.class, To.class,$->new To());
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		ExampleException exception = assertThrows(ExampleException.class, ()->mapper.map(from, To.class, CustomException.forType(ExampleException.class)));
		assertThat(exception.getMessage()).contains("es.utils.mapper.exception.MappingNotFoundException: WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tclass java.util.concurrent.TimeUnit\n" +
				"Existing source mappings to class to.To:\n" +
				"\tclass java.time.temporal.ChronoUnit\n" +
				"Other existing mappings:\n" +
				"\tnone\n"
				+ "");
	}
	@Test
	public void shouldThrowCustomExceptionWithNotExistingMappingByObject() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		ExampleException exception = assertThrows(ExampleException.class, ()->mapper.map(from, new To(), CustomException.forType(ExampleException.class).message("No mapping found")));
		assertThat(exception.getMessage()).isEqualTo("No mapping found");
		assertThat(exception.getCause().getMessage()).contains("WARNING - No mappings found in "+mapper.getClass().getName()+"["+mapper.getMapperName()+"] for input class from.From and output class to.To\nInput instance: "+from+"\n" +
				"Existing destination mappings from class from.From:\n" +
				"\tnone\n" +
				"Existing source mappings to class to.To:\n" +
				"\tnone\n" +
				"Other existing mappings:\n" +
				"\tclass java.time.temporal.ChronoUnit -> class java.util.concurrent.TimeUnit\n"
				+ "");
	}

	@Test
	public void shouldThrowRuntimeExceptionWithNullDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = null;
		RuntimeException exception = assertThrows(RuntimeException.class, ()->mapper.map(from, to, CustomException.forType(EmptyExampleException.class)));
		assertThat(exception.getMessage()).isEqualTo("Custom Exception class exception.EmptyExampleException does not have a constructor taking only a Throwable instance. Please provide a message error or don't pass any cause.");
	}
	@Test
	public void shouldThrowRuntimeExceptionWithNotExistingMappingByClass() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		RuntimeException exception = assertThrows(RuntimeException.class, ()->mapper.map(from, To.class, CustomException.forType(EmptyExampleException.class)));
		assertThat(exception.getMessage()).contains("Custom Exception class exception.EmptyExampleException does not have a constructor taking only a Throwable instance");
	}
	@Test
	public void shouldThrowRuntimeExceptionWithNotExistingMappingByClass2() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, TimeUnit.class,from->TimeUnit.DAYS);
		mapper.add(ChronoUnit.class, To.class,$->new To());
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		RuntimeException exception = assertThrows(RuntimeException.class, ()->mapper.map(from, To.class, CustomException.forType(EmptyExampleException.class)));
		assertThat(exception.getMessage()).contains("Custom Exception class exception.EmptyExampleException does not have a constructor taking only a Throwable instance");
	}
	@Test
	public void shouldThrowRuntimeExceptionWithNotExistingMappingByObject() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		RuntimeException exception = assertThrows(RuntimeException.class, ()->mapper.map(from, new To(), CustomException.forType(EmptyExampleException.class).message("message")));
		assertThat(exception.getMessage()).contains("Custom Exception class exception.EmptyExampleException does not have a constructor taking both String and Throwable instances");
		assertThat(exception.getCause().toString()).isEqualTo("java.lang.NoSuchMethodException: exception.EmptyExampleException.<init>(java.lang.String, java.lang.Throwable)");
	}

	@Test
	public void shouldThrowCustomExceptionWithNoExistingMappingFromInputOnly() throws MappingException {
		Mapper mapper = new Mapper();
//		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		ExampleException exception = assertThrows(ExampleException.class, ()->mapper.map(from, CustomException.forType(ExampleException.class)));
		assertThat(exception.getMessage()).contains("es.utils.mapper.exception.MappingNotFoundException: Found 0 mapping(s) from class from.From. Cannot uniquely map the input "+from+".");
	}
	@Test
	public void shouldThrowRuntimeExceptionWithNoExistingMappingFromInputOnly() throws MappingException {
		Mapper mapper = new Mapper();
//		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		RuntimeException exception = assertThrows(RuntimeException.class, ()->mapper.map(from, CustomException.forType(EmptyExampleException.class).message("message")));
		assertThat(exception.getMessage()).contains("Custom Exception class exception.EmptyExampleException does not have a constructor taking both String and Throwable instances");
		assertThat(exception.getCause().toString()).isEqualTo("java.lang.NoSuchMethodException: exception.EmptyExampleException.<init>(java.lang.String, java.lang.Throwable)");
	}

	@Test
	public void shouldMapExistingDirectMappingBetweenClasses() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(String.class,String[].class, s->s.split(" "));
		assertThat(mapper.map("Pippo",String[].class)).isNotNull()
													  .isInstanceOf(String[].class)
													  .hasSize(1)
													  .containsExactly("Pippo");
		String[] to = new String[0];
		to = mapper.map("Pippo Paperino Sora",to);
		assertThat(to).isNotNull()
					  .isInstanceOf(String[].class)
					  .hasSize(3)
					  .containsExactly("Pippo","Paperino","Sora");
	}
	@Test
	public void shouldMapArrayOfElementIntoArray() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		ChronoUnit[] origin = new ChronoUnit[]{ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES};
		assertThat(mapper.mapArray(origin,new TimeUnit[0])).isNotNull()
															.isInstanceOf(TimeUnit[].class)
															.hasSize(3)
															.containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
		assertThat(mapper.mapArray(null,new TimeUnit[0])).isNotNull()
														 .isInstanceOf(TimeUnit[].class)
														 .hasSize(0);

	}
	@Test
	public void shouldMapArrayOfElementIntoArrayOfSuperType() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		String[] origin = {"MINUTES","MINUTES","MINUTES"};
		assertThat(mapper.mapArray(origin,new CharSequence[0])).isNotNull()
															.isInstanceOf(CharSequence[].class)
															.hasSize(3)
															.containsSequence("MINUTES","MINUTES","MINUTES");
		assertThat(mapper.mapArray(null,new CharSequence[0])).isNotNull()
														 .isInstanceOf(CharSequence[].class)
														 .hasSize(0);
	}
	@Test
	public void shouldMapArrayOfElementIntoArrayOfSubType() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		CharSequence[] origin = {"MINUTES","MINUTES","MINUTES"};
		assertThat(mapper.mapArray(origin,new String[0])).isNotNull()
															.isInstanceOf(String[].class)
															.hasSize(3)
															.containsOnlyNulls();
		assertThat(mapper.mapArray(null,new String[0])).isNotNull()
														 .isInstanceOf(String[].class)
														 .hasSize(0);
	}
	@Test
	public void shouldMapArrayOfElementAndCreateArray() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		ChronoUnit[] origin = new ChronoUnit[]{ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES};
		assertThat(mapper.mapArray(origin,TimeUnit.class)).isNotNull()
															.isInstanceOf(TimeUnit[].class)
															.hasSize(3)
															.containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
	}
	@Test
	public void shouldMapCollectionOfElement() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		List<ChronoUnit> originList = Arrays.asList(ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES);
		Collection<TimeUnit> resultMapList = mapper.mapList(originList,TimeUnit.class);
		assertThat(resultMapList).isNotNull()
										.isInstanceOf(ArrayList.class)
										.hasSize(3)
										.containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
		Set<ChronoUnit> originSet = new HashSet<>();
		originSet.addAll(originList);
		Collection<TimeUnit> resultMapSet = mapper.mapSet(originSet,TimeUnit.class);
		assertThat(resultMapSet).isNotNull()
				.isInstanceOf(HashSet.class)
				.hasSize(1)
				.containsSequence(TimeUnit.MINUTES);
		assertThat(mapper.mapCollection(null,new ArrayList<TimeUnit>(),TimeUnit.class)).isNull();
	}
	@Test
	public void shouldThrowMappingExceptionBecauseOfErrorInMappingSingleElementOfCollection() throws MappingException, IOException {
		Mapper mapper = new Mapper();
		MapperObject<ClassMapperFromTest, ClassMapperToTest> objectMapper = mapper.addForClass(ClassMapperFromTest.class,ClassMapperToTest.class)
			  .addMapping()
			  	.from("name", ClassMapperFromTest::getNameFrom)
			  	.transform(String::length)
			  	.to("age", ClassMapperToTest::setAge)
			  	.create();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		PrintStream ps = new PrintStream(out);
		System.setErr(ps);
		
		ClassMapperToTest result = objectMapper.mapOrNull(new ClassMapperFromTest(null,null));

		String outString = out.toString();
		out.flush();
		out.close();
		System.setErr(originalErr);
		assertThat(result).isNull();
		assertThat(outString).startsWith("es.utils.mapper.exception.MappingException: Error mapping input value ClassMapperFromTest [nameFrom=null, surnameFrom=null]");
	}
	@SuppressWarnings("unchecked")
	@Test
	public void shouldMapCollectionOfElementByClassType() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		List<ChronoUnit> origin = Arrays.asList(ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES);
		ArrayList<TimeUnit> resultMapCollection = mapper.mapCollection(origin,ArrayList.class,TimeUnit.class);
		assertThat(resultMapCollection).isNotNull()
									   .isInstanceOf(ArrayList.class)
									   .hasSize(3)
									   .containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
		assertThat(mapper.mapCollection(null,ArrayList.class,TimeUnit.class)).isNull();
	}
	@Test
	public void shouldMapTwoObjectsAndResultAreEquals() throws MappingException, MappingNotFoundException, IOException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to1 = mapper.map(from,To.class);
		To to2 = mapper.map(from,To.class);
		assertThat(to1).isEqualTo(to2);
	}

	/* Map Method Enum */
	@Test
	public void shouldMapExistingMappingBetweenEnums() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		assertThat(mapper.map(ChronoUnit.MINUTES, TimeUnit.class)).isNotNull().isInstanceOf(TimeUnit.class);
	}
	@Test
	public void shoulThrowMappingExceptionWithNullClassEnumDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		ChronoUnit from = ChronoUnit.MINUTES;
		Class<?> myType = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(from, myType));
		assertThat(exception.getMessage()).contains(MessageFormat.format("Error mapping {0}: Destination class cannot be null",from));
	}
	@Test
	public void shouldNotMapWithNullInputEnum() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		assertThat(mapper.map(null, TimeUnit.class)).isNull();
	}
	@Test
	public void shouldReturnMappedEnum() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		assertThat(mapper.map(ChronoUnit.MINUTES, TimeUnit.DAYS)).isNotNull().isEqualTo(TimeUnit.MINUTES);
	}
	@Test
	public void shouldReturnDefaultEnumIfEnumMapperDoesNotFoundAnything() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		assertThat(mapper.map(ChronoUnit.CENTURIES, TimeUnit.DAYS)).isNotNull().isEqualTo(TimeUnit.DAYS);
	}
	@Test
	public void shouldThrowMappingExceptionWithNullDestinationEnum() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		ChronoUnit from = ChronoUnit.CENTURIES;
		TimeUnit to = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(from, to));
		assertThat(exception.getMessage()).contains(MessageFormat.format("Error mapping {0}: Destination object cannot be null",from));
	}
	@Test
	public void shouldNotMapWithNullInputEnumToEnumDefault() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		assertThat(mapper.map(null,TimeUnit.DAYS)).isEqualTo(TimeUnit.DAYS);
	}

	/* MapOrNull Method */
	@Test
	public void shouldReturnNullValueWithNullClassDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		Class<?> myType = null;
		assertThat(mapper.mapAsOptional(from, myType)).isNotPresent();
		assertThat(mapper.mapAsOptional(from, To.class)).isPresent();
	}
	@Test
	public void shouldReturnNullValueWithNullDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = null;
		assertThat(mapper.mapAsOptional(from, to)).isNotPresent();
		assertThat(mapper.mapAsOptional(from, new To())).isPresent();
	}
	@Test
	public void shouldReturnNullValueWithNotExistingMapping() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		assertThat(mapper.mapAsOptional(from, To.class)).isNotPresent();
	}
	@Test
	public void shoulReturnNullValueNullClassEnumDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		Class<?> myType = null;
		assertThat(mapper.mapAsOptional(ChronoUnit.MINUTES, myType)).isNotPresent();
	}
	@Test
	public void shouldReturnNullValueWithNullDestinationEnum() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		TimeUnit to = null;
		assertThat(mapper.mapAsOptional(ChronoUnit.CENTURIES, to)).isNotPresent();
	}
	@Test
	public void shouldReturnNullValueWithNotExistingMappingAndImplicitDestinationType() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		Optional<To> result = mapper.mapAsOptional(from);
		assertThat(result).isNotPresent();
	}
	@Test
	public void shouldReturnMappedValueWithExistingDestinationType() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		Optional<TimeUnit> result = mapper.mapAsOptional(ChronoUnit.DAYS);
		assertThat(result).isPresent().containsSame(TimeUnit.DAYS);
	}

	
	/* build method */
	@Test
	public void shouldActivateAddedMappings() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From,To> mapping = mapper.addForClass(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		assertThat(mapping.map(null)).isNull();
		mapper.build();
		To to = mapping.map(from);
		assertThat(to).hasNoNullFieldsOrPropertiesExcept("ignoredField","ignoredField1","ignoredField2","timestamp1","timestamp4");
	}
	
	/* toString method */
	@Test
	public void shouldReturnStringRappresentation() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		assertThat(mapper.toString()).isEqualTo("Mapper[defaultMapper][<class from.From,class to.To>]");
	}
	@Test
	public void shouldReturnStringRappresentationPrettyPrint() throws MappingException {
		Mapper mapper = new Mapper("PrettyPrint");
		mapper.add(From.class, To.class);
		assertThat(mapper.prettyPrint()).isEqualTo("Mapper \"PrettyPrint\"\n\tClassMapper[<class from.From,class to.To>]");
	}

	/* Map method gerarchical types */
	@Test
	public void shouldMapSubClass() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(SubFrom.class, SubTo.class);
		SubFrom from = new SubFrom("Pippo","Paperino",new SubFrom("InnerPippo","InnerPaperino"));
		SubTo to = mapper.map(from, SubTo.class);
		assertThat(to).isNotNull().isInstanceOf(SubTo.class);
		assertThat(from.newSubField).isEqualTo(to.newSubField);
	}
	@SuppressWarnings("static-access")
	@Test
	public void shouldNotMapFinalField() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ImplFrom.class, ImplTo.class);
		ImplFrom from = new ImplFrom();
		ImplTo to = mapper.map(from, ImplTo.class);
		assertThat(to).isNotNull().isInstanceOf(ImplTo.class);
		assertThat(to.staticFinal).isEqualTo(97);
		assertThat(to._final).isEqualTo(97);
	}
	@SuppressWarnings("static-access")
	@Test
	public void shouldMapStaticField() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ImplFrom.class, ImplTo.class);
		ImplFrom from = new ImplFrom();
		ImplTo to = mapper.map(from, ImplTo.class);
		assertThat(to).isNotNull().isInstanceOf(ImplTo.class);
		assertThat(to._static).isEqualTo(42);
	}
	@Test
	public void shouldMapComplexValue() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class,To.class)
			  .add(ImplFrom.class,ImplTo.class);
		ImplFrom from = new ImplFrom();
		ImplTo to = mapper.map(from, ImplTo.class);
		assertThat(to.complexValue).isNotNull();
	}

	@Test
	public void shouldReturnNamesForClass() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		mapper.build();
		assertThat(mapper.getNames(From.class)).contains("innerCollection2", "surname", "classFrom", "fromCollection2", "name", "fromArray", "innerCollection", "innerArray", "fromCollection");
	}

	@Test
	public void shouldCreateNewInstanceForClass() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		To instance = mapper.createNewInstance(To.class);
		assertThat(instance).isEqualTo(new To());
	}

	@Test
	public void shouldThrowMappingExceptionForCreation() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, ToWithNoEmptyConstructor.class);
		MappingException exception = assertThrows(MappingException.class, ()->mapper.createNewInstance(ToWithNoEmptyConstructor.class));
		assertThat(exception.getMessage()).isEqualTo("Destination class does not have a public empty constructor. Please provide a public empty constructor or a Supplier in the configuration such that the mapping can be done.");
	}

}
