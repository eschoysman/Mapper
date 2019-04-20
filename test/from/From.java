package from;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.IgnoreField;

@IgnoreField({"ignoredField","ignoredField1"})
public class From {

	private final String classFrom;
	private String name,surname;
	@AliasNames("innerArray")
	private From[] fromArray;
	@AliasNames("innerCollection")
	private List<From> fromCollection;
	@AliasNames({"innerCollection2","prova di nome sbagliato"})
	private List<From> fromCollection2;
	private String ignoredField;
	private String ignoredField1;
	private String ignoredField2;

	public From() {
		this("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
	}
	
	public From(String name, String surname, From... fromArray) {
		this.classFrom = "From";
		this.name = name;
		this.surname = surname;
		this.fromArray = fromArray.length==0 ? null : fromArray;
		this.fromCollection = Arrays.asList(fromArray);
		this.fromCollection2 = new LinkedList<>(Arrays.asList(fromArray));
	}

	@Override
	public String toString() {
		return String.format(
				"From [classFrom=%s, name=%s, surname=%s, fromArray=%s, fromCollection=%s, fromCollection2=%s, ignoredField=%s, ignoredField1=%s, ignoredField2=%s]",
				this.classFrom, this.name, this.surname, Arrays.toString(this.fromArray), this.fromCollection,
				this.fromCollection2, this.ignoredField, this.ignoredField1, this.ignoredField2);
	}

}
