package testcase;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;
import testcase.emb.*;

@RunWith(JUnitPlatform.class)
@SelectClasses({
	BuilderTest.class,
	ClassMapperTest.class,
	CollectionFactoryTest.class,
	ConfigurationTest.class,
	ConverterTest.class,
	DirectMapperTest.class,
	EMB_Step0.class,
	EMB_FromStep.class,
	EMB_DefaultInputStep.class,
	EMB_TransformerStep.class,
	EMB_DefaultOutputStep.class,
	EMB_ConsumeStep.class,
	EMB_ToStep.class,
	EMB_BuilderStep.class,
	EnumMapperTest.class,
	MapperTest.class,
	MapperUtilTest.class,
	PairKeyTest.class
})
public class FullTestCase {}
