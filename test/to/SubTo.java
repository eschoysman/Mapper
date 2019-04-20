package to;

public class SubTo extends To {

	public String newSubField;

	@Override
	public String toString() {
		return String.format("SubTo [newSubField=%s, toString()=%s]", this.newSubField, super.toString());
	}
	
}
