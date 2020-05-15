package from;

import converter.ConverterDateTimestamp;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.Converter;
import es.utils.mapper.annotation.IgnoreField;

import java.util.*;

@IgnoreField({"ignoredField","ignoredField1"})
public class From {

	private final String classFrom;
	private String name,surname;
	@AliasNames("innerArray")
	private From[] fromArray;
	@AliasNames("innerCollection")
	private List<From> fromCollection;
	@AliasNames({"innerCollection2","prova di nome sbagliato"})
	private List<? extends From> fromCollection2;
	private String ignoredField;
	private String ignoredField1;
	private String ignoredField2;
	private List<? extends Optional<String>> optionalCollection;

	@AliasNames("data1")
	private Date date1;	// 2019-07-07 08:45:36
	@AliasNames("data2")
	@Converter(ConverterDateTimestamp.class)
	private Date date2;	// 2019-07-07 08:45:36
	@AliasNames("data3")
	private Date date3;	// 2019-07-07 08:45:36
	@AliasNames("data4")
	private Date date4;	// 2019-07-07 08:45:36
	
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
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, Calendar.JULY, 07, 8, 45, 36);
		calendar.set(Calendar.MILLISECOND,0);
		this.date1 = calendar.getTime();
		this.date2 = calendar.getTime();
		this.date3 = calendar.getTime();
		this.date4 = calendar.getTime();
		this.optionalCollection = new ArrayList<>();
	}

	@Override
	public String toString() {
		return String.format(
				"From [classFrom=%s, name=%s, surname=%s, fromArray=%s, fromCollection=%s, fromCollection2=%s, ignoredField=%s, ignoredField1=%s, ignoredField2=%s, date1=%s, date2=%s, date3=%s]",
				this.classFrom, this.name, this.surname, Arrays.toString(this.fromArray), this.fromCollection,
				this.fromCollection2, this.ignoredField, this.ignoredField1, this.ignoredField2, this.date1, this.date2, this.date3);
	}

	public List<? extends Optional<String>> getOptionalCollection() {
		return optionalCollection;
	}

	public void setOptionalCollection(List<? extends Optional<String>> optionalCollection) {
		this.optionalCollection = optionalCollection;
	}

}
