package testcase;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import testcase.emb.EMB_BuilderStep;
import testcase.emb.EMB_ConsumeStep;
import testcase.emb.EMB_DefaultInputStep;
import testcase.emb.EMB_DefaultOutputStep;
import testcase.emb.EMB_FromStep;
import testcase.emb.EMB_Step0;
import testcase.emb.EMB_ToStep;
import testcase.emb.EMB_TransformerStep;

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
