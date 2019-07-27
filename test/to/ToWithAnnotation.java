package to;

import annotation.TestAnnotation;

public class ToWithAnnotation {

	@TestAnnotation(name="name")
	private CharSequence field;
	
	public ToWithAnnotation() {
	}

	public CharSequence getName() {
		return this.field;
	}

	@Override
	public String toString() {
		return String.format("ToWithAnnotation [name=%s]", field);
	}

}
