package testcase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.exception.MappingNotFoundException;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.impl.object.ClassMapper;
import es.utils.mapper.impl.object.DirectMapper;
import es.utils.mapper.impl.object.EnumMapper;
import from.From;
import from.ImplFrom;
import from.SubFrom;
import to.ImplTo;
import to.NoEmptyConstructor;
import to.SubTo;
import to.To;

public class MapperTest {

	/* Constructor Method */
	@Test
	public void shouldCreateMapper() {
		Mapper mapper = new Mapper();
		assertThat(mapper.getAllMappings()).isNotNull();
		assertThat(mapper.getAllMappings()).isEmpty();
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
		assertThat(exception.getMessage()).isEqualTo("From class or To class is null");
		exception = assertThrows(MappingException.class, ()->mapper.add(ChronoUnit.class, null));
		assertThat(exception.getMessage()).isEqualTo("From class or To class is null");
		exception = assertThrows(MappingException.class, ()->mapper.add(null, null));
		assertThat(exception.getMessage()).isEqualTo("From class or To class is null");
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
	public void shouldThrowsMappingExceptionIfDestinationTypeDoesNotHaveEmptyConstructor() throws MappingException {
		Mapper mapper = new Mapper();
		MappingException ex = assertThrows(MappingException.class,()->mapper.addForClass(From.class, NoEmptyConstructor.class));
		assertThat(ex.getMessage()).contains("Destination class does not have a empty constructor. Please provide a empty contructor such that the mapping can be done.");
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
		assertThat(exception.getMessage()).contains("Destination class cannot be null");
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
		assertThat(exception.getMessage()).isEqualTo("Destination object cannot be null");
	}
	@Test
	public void shouldThrowExceptionWithNullValues() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		String nullValue = null;
		assertThat(mapper.map(null, nullValue)).isNull();
	}
	@Test
	public void shouldThrowMappingNotFoundExceptionWithNotExistingMapping() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		MappingNotFoundException exception = assertThrows(MappingNotFoundException.class, ()->mapper.map(from, To.class));
		assertThat(exception.getMessage()).contains("WARNING - No mappings found in es.utils.mapper.Mapper for input class java.lang.Class and output class to.To\n" + 
				"Exisiting destination mappings from class from.From:\n" + 
				"\tnone\n" + 
				"Exisiting source mappings to class to.To:\n" + 
				"\tnone\n" + 
				"Other exisiting mappings:\n" + 
				"\tclass java.time.temporal.ChronoUnit -> class java.util.concurrent.TimeUnit\n"
				+ "");
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
		List<ChronoUnit> origin = Arrays.asList(ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES);
		Collection<TimeUnit> resultMapCollection = mapper.mapCollection(origin,new ArrayList<TimeUnit>(),TimeUnit.class);
		assertThat(resultMapCollection).isNotNull()
										.isInstanceOf(ArrayList.class)
										.hasSize(3)
										.containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
	}
	@Test
	public void shouldMapCollectionOfElementByClassType() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		List<ChronoUnit> origin = Arrays.asList(ChronoUnit.MINUTES,ChronoUnit.MINUTES,ChronoUnit.MINUTES);
		@SuppressWarnings("unchecked")
		ArrayList<TimeUnit> resultMapCollection = mapper.mapCollection(origin,ArrayList.class,TimeUnit.class);
		assertThat(resultMapCollection).isNotNull()
															.isInstanceOf(ArrayList.class)
															.hasSize(3)
															.containsSequence(TimeUnit.MINUTES,TimeUnit.MINUTES,TimeUnit.MINUTES);
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
		Class<?> myType = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(ChronoUnit.MINUTES, myType));
		assertThat(exception.getMessage()).contains("Destination class cannot be null");
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
		TimeUnit to = null;
		MappingException exception = assertThrows(MappingException.class, ()->mapper.map(ChronoUnit.CENTURIES, to));
		assertThat(exception.getMessage()).contains("Destination object cannot be null");
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
		assertThat(mapper.mapOrNull(from, myType)).isNull();
		assertThat(mapper.mapOrNull(from, To.class)).isNotNull();
	}
	@Test
	public void shouldReturnNullValueWithNullDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		To to = null;
		assertThat(mapper.mapOrNull(from, to)).isNull();
		assertThat(mapper.mapOrNull(from, new To())).isNotNull();
	}
	@Test
	public void shouldReturnNullValueWithNotExistingMapping() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		assertThat(mapper.mapOrNull(from, To.class)).isNull();
	}
	@Test
	public void shoulReturnNullValueNullClassEnumDestination() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		Class<?> myType = null;
		assertThat(mapper.mapOrNull(ChronoUnit.MINUTES, myType)).isNull();
	}
	@Test
	public void shouldReturnNullValueWithNullDestinationEnum() throws MappingException, MappingNotFoundException {
		Mapper mapper = new Mapper();
		mapper.add(ChronoUnit.class, TimeUnit.class);
		TimeUnit to = null;
		assertThat(mapper.mapOrNull(ChronoUnit.CENTURIES, to)).isNull();
	}

	
	/* build method */
	@Test
	public void shouldActivateAddedMappings() throws MappingException {
		Mapper mapper = new Mapper();
		ClassMapper<From,To> mapping = mapper.addForClass(From.class, To.class);
		From from = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
		assertThat(mapping.map(from)).isEqualTo(new To());
		assertThat(mapping.map(null)).isNull();
		mapper.build();
		To to = mapping.map(from);
		assertThat(to).hasNoNullFieldsOrPropertiesExcept("ignoredField","ignoredField1","ignoredField2");
	}
	
	/* toString method */
	@Test
	public void shouldReturnStringRappresentation() throws MappingException {
		Mapper mapper = new Mapper();
		mapper.add(From.class, To.class);
		assertThat(mapper.toString()).isEqualTo("Mapper[<class from.From,class to.To>]");
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
		assertThat(mapper.getNames(From.class)).contains("innerCollection2", "ignoredField1", "surname", "classFrom", "fromCollection2", "ignoredField2", "name", "fromArray", "innerCollection", "innerArray", "fromCollection", "ignoredField");
	}

}
