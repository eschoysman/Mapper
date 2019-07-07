package from;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import converter.ConverterDateTimestamp;
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

	@AliasNames("data1")
	private Date date1;	// 2019-07-07 08:45:36
	@AliasNames(value="data2", converter=ConverterDateTimestamp.class)
	private Date date2;	// 2019-07-07 08:45:36
	@AliasNames("data3")
	private Date date3;	// 2019-07-07 08:45:36
	
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
	}

	@Override
	public String toString() {
		return String.format(
				"From [classFrom=%s, name=%s, surname=%s, fromArray=%s, fromCollection=%s, fromCollection2=%s, ignoredField=%s, ignoredField1=%s, ignoredField2=%s, date1=%s, date2=%s, date3=%s]",
				this.classFrom, this.name, this.surname, Arrays.toString(this.fromArray), this.fromCollection,
				this.fromCollection2, this.ignoredField, this.ignoredField1, this.ignoredField2, this.date1, this.date2, this.date3);
	}

}
