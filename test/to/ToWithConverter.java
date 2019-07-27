package to;

import annotation.TestAnnotation;

public class ToWithConverter {

	@TestAnnotation(name="name")
	private CharSequence field;
	
	public ToWithConverter() {
	}

	public CharSequence getName() {
		return this.field;
	}

	@Override
	public String toString() {
		return String.format("ToWithAnnotation [name=%s]", field);
	}

}
