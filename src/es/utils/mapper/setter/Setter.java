package es.utils.mapper.setter;

import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class Setter<U,TMP> {

	private String name;
	private BiConsumer<U,TMP> setter;
	
	protected Setter(String name, BiConsumer<U,TMP> setter) {
		this.name = Objects.requireNonNull(name);
		this.setter = Objects.requireNonNull(setter);
	}
	
	public String getName() {
		return name;
	}

	public void apply(U input, TMP data) {
		setter.accept(input,data);
	}
	
}
