package from;

public class SubFrom extends From {

	public String newSubField;
	
	public SubFrom(String name, String surname, From... fromArray) {
		super(name, surname, fromArray);
		this.newSubField = "newSubField";
	}

	@Override
	public String toString() {
		return String.format("SubFrom [newSubField=%s, toString()=%s]", this.newSubField, super.toString());
	}

}
