package from;

import es.utils.mapper.annotation.AliasNames;

import java.util.Map;

public class SpecificTestCaseFrom<T> {
	@SuppressWarnings("unused")
	private String string;
	@AliasNames("string")
	private String string1;
	
	@SuppressWarnings("unused")
	private Map<String, String> map;
	
}
