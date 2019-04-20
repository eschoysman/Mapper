package es.utils.mapper.setter;

import java.util.function.BiConsumer;

public class FunctionSetter<U,TMP> extends Setter<U,TMP> {

	public FunctionSetter(String name, BiConsumer<U,TMP> setter) {
		super(name,setter);
	}

}
