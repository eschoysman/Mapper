package es.utils.mapper.defaultvalue;

//TODO javadoc
public enum DefaultValueStrategy {

	/**
	 * Never use the default values during the mappings.<br>
	 * Cannot be combined with other {@link DefaultValueStrategy} values.
	 */
	NEVER,
	
	/**
	 * Default behavior for the default value strategy to use.<br>
	 * Equivalent to {@link #ALWAYS} and {@link #OUTPUT}.<br>
	 * Cannot be combined with other {@link DefaultValueStrategy} values.
	 */
	DEFAULT,
	
	/**
	 * Always use the default values during the mappings.<br>
	 * Can be combined with {@link #INPUT} and {@link #OUTPUT}
	 */
	ALWAYS,
	
	/**
	 * Use the default values during the mappings only for existing (default or custom) mappings.<br>
	 * Can be combined with {@link #INPUT} and {@link #OUTPUT}<br>
	 */
	CUSTOM,
	
	/**
	 * Use to enable the default values for the empty values in the origin object instance.<br>
	 * Can be combined with {@link #ALWAYS} and {@link #CUSTOM}
	 */
	INPUT,
	
	/**
	 * Use to enable the default values for the empty values in the destination object instance.<br>
	 * Can be combined with {@link #ALWAYS} and {@link #CUSTOM}
	 */
	OUTPUT
	
}
