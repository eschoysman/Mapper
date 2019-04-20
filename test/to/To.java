package to;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.CollectionType;
import es.utils.mapper.annotation.IgnoreField;

public class To {

	@SuppressWarnings("unused")
	private final String classTo;
	private CharSequence name;
//	@IgnoreField
	private CharSequence surname;
	@AliasNames("innerArray")
	private To[] toArray;
	@AliasNames("innerCollection")
	@CollectionType(LinkedList.class)
	private List<To> toCollection;
	@AliasNames("innerCollection2")
	private List<To> toCollection2;
	@IgnoreField
	private String ignoredField;
	@SuppressWarnings("unused")
	private String ignoredField1;
	@IgnoreField
	private String ignoredField2;

	public To() {
		this.classTo = "To";
	}

	public List<To> getToCollection() {
		return toCollection;
	}

	@Override
	public String toString() {
		return String.format(
				"To [classTo=%s, name=%s, surname=%s, toArray=%s, toCollection=%s, toCollection2=%s, ignoredField=%s, ignoredField1=%s, ignoredField2=%s]",
				this.classTo, this.name, this.surname, Arrays.toString(this.toArray), this.toCollection,
				this.toCollection2, this.ignoredField, this.ignoredField1, this.ignoredField2);
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
							toCollection2);
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
		return true;
	}

}
