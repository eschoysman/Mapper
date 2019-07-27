package from;

import es.utils.mapper.annotation.IgnoreField;

@IgnoreField({"ignoredField","ignoredField1"})
public class FromWithAnnotation {

	private String name;
	
	public FromWithAnnotation() {
		this("Pippo");
	}
	
	public FromWithAnnotation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return String.format("FromWithAnnotation [name=%s]", name);
	}

}
