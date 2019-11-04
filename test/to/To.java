package to;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import converter.ConverterDateTimestamp;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.Converter;
import es.utils.mapper.annotation.IgnoreField;

public class To {

	private final String classTo;
	private CharSequence name;
//	@IgnoreField
	private CharSequence surname;
	@AliasNames("innerArray")
	private To[] toArray;
	@AliasNames("innerCollection")
	@CollectionType(LinkedList.class)
	private List<? extends To> toCollection;
	@AliasNames("innerCollection2")
	private List<To> toCollection2;
	@IgnoreField
	private String ignoredField;
	private String ignoredField1;
	@IgnoreField
	private String ignoredField2;

	@AliasNames("data1")
	private Timestamp timestamp1;
	@AliasNames("data2")
	private Timestamp timestamp2;
	@AliasNames("data3")
	@Converter(ConverterDateTimestamp.class)
	private Timestamp timestamp3;
	@AliasNames("data4")
	private Timestamp timestamp4;
	private List<? extends Optional<String>> optionalCollection;
	
	public To() {
		this.classTo = "To";
	}

	public List<? extends To> getToCollection() {
		return toCollection;
	}

	@Override
	public String toString() {
		return String.format(
				"To [classTo=%s, name=%s, surname=%s, toArray=%s, toCollection=%s, toCollection2=%s, ignoredField=%s, ignoredField1=%s, ignoredField2=%s, timestamp1=%s, timestamp2=%s, timestamp3=%s]",
				this.classTo, this.name, this.surname, Arrays.toString(this.toArray), this.toCollection,
				this.toCollection2, this.ignoredField, this.ignoredField1, this.ignoredField2, this.timestamp1, this.timestamp2, this.timestamp2);
	}

	public CharSequence getName() {
		return this.name;
	}

	public void setName(CharSequence name) {
		this.name = name;
	}

	public CharSequence getSurname() {
		return this.surname;
	}

	public void setSurname(CharSequence surname) {
		this.surname = surname;
	}

	public Timestamp getTimestamp1() {
		return timestamp1;
	}

	public void setTimestamp1(Timestamp timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public Timestamp getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(Timestamp timestamp2) {
		this.timestamp2 = timestamp2;
	}

	public Timestamp getTimestamp3() {
		return timestamp3;
	}

	public void setTimestamp3(Timestamp timestamp3) {
		this.timestamp3 = timestamp3;
	}
	public Timestamp getTimestamp4() {
		return timestamp4;
	}

	@Override
	public int hashCode() {
		return Objects.hash(classTo,
							ignoredField,
							ignoredField1,
							ignoredField2,
							name,
							surname,
							toArray,
							toCollection,
							toCollection2,
							timestamp1,
							timestamp2,
							timestamp3,
							timestamp4);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		To other = (To) obj;
		if (this.classTo == null) {
			if (other.classTo != null)
				return false;
		} else if (!this.classTo.equals(other.classTo))
			return false;
		if (this.ignoredField == null) {
			if (other.ignoredField != null)
				return false;
		} else if (!this.ignoredField.equals(other.ignoredField))
			return false;
		if (this.ignoredField1 == null) {
			if (other.ignoredField1 != null)
				return false;
		} else if (!this.ignoredField1.equals(other.ignoredField1))
			return false;
		if (this.ignoredField2 == null) {
			if (other.ignoredField2 != null)
				return false;
		} else if (!this.ignoredField2.equals(other.ignoredField2))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.surname == null) {
			if (other.surname != null)
				return false;
		} else if (!this.surname.equals(other.surname))
			return false;
		if (!Arrays.equals(this.toArray, other.toArray))
			return false;
		if (this.toCollection == null) {
			if (other.toCollection != null)
				return false;
		} else if (!this.toCollection.equals(other.toCollection))
			return false;
		if (this.toCollection2 == null) {
			if (other.toCollection2 != null)
				return false;
		} else if (!this.toCollection2.equals(other.toCollection2))
			return false;
		if (this.timestamp1 == null) {
			if (other.timestamp1 != null)
				return false;
		} else if (!this.timestamp1.equals(other.timestamp1))
			return false;
		if (this.timestamp2 == null) {
			if (other.timestamp2 != null)
				return false;
		} else if (!this.timestamp2.equals(other.timestamp2))
			return false;
		if (this.timestamp3 == null) {
			if (other.timestamp3 != null)
				return false;
		} else if (!this.timestamp3.equals(other.timestamp3))
			return false;
		if (this.timestamp4 == null) {
			if (other.timestamp4 != null)
				return false;
		} else if (!this.timestamp4.equals(other.timestamp4))
			return false;
		return true;
	}

	public List<? extends Optional<String>> getOptionalCollection() {
		return optionalCollection;
	}

	public void setOptionalCollection(List<? extends Optional<String>> optionalCollection) {
		this.optionalCollection = optionalCollection;
	}

}
