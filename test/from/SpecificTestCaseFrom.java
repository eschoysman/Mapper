package from;

import java.util.Map;

import es.utils.mapper.annotation.AliasNames;

public class SpecificTestCaseFrom<T> {
	@SuppressWarnings("unused")
	private String string;
	@AliasNames("string")
	private String string1;
	
	@SuppressWarnings("unused")
	private Map<String, String> map;
	
}
