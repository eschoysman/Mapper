package testcase;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
	ClassMapperTest.class,
	CollectionFactoryTest.class,
	DirectMapperTest.class,
	EnumMapperTest.class,
	GetterSetterFactoryTest.class,
	MapperTest.class,
	PairKeyTest.class
})
public class FullTestCase {}
