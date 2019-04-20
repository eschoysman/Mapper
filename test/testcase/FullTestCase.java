package testcase;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
	MapperTest.class,
	EnumMapperTest.class,
	ClassMapperTest.class,
	DirectMapperTest.class
})
public class FullTestCase {}
