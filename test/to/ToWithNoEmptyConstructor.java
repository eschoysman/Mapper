package to;

import annotation.TestAnnotation;

public class ToWithNoEmptyConstructor {

	@TestAnnotation(name="name")
	private CharSequence field;
	
	public ToWithNoEmptyConstructor(String name) {
		this.field = name;
	}

	public CharSequence getName() {
		return this.field;
	}

	@Override
	public String toString() {
		return String.format("ToWithAnnotation [name=%s]", field);
	}
	
	public static ToWithNoEmptyConstructor withName(String name) {
		return new ToWithNoEmptyConstructor(name);
	}

}
