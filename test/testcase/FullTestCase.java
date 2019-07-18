package testcase;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
	BuilderTest.class,
	ClassMapperTest.class,
	CollectionFactoryTest.class,
	ConfigurationTest.class,
	ConverterTest.class,
	DirectMapperTest.class,
	ElementMapperBuilderTest.class,
	EnumMapperTest.class,
	MapperTest.class,
	MapperUtilTest.class,
	PairKeyTest.class
})
public class FullTestCase {}
