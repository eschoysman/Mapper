package to;

import converter.ConverterToNull;
import es.utils.mapper.annotation.AliasNames;
import es.utils.mapper.annotation.Converter;
import es.utils.mapper.annotation.Default;

public class ClassMapperToTest {

	@AliasNames("name")
	public String nameTo;
	@AliasNames("surname")
	private String surnameTo;
	private String fullName;
	@Converter(ConverterToNull.class)
	@Default(type=Integer.class)
	private int age;
	public String publicField;
	
	public ClassMapperToTest(String nameTo, String surnameTo) {
		this.nameTo = nameTo;
		this.surnameTo = surnameTo;
	}
	public ClassMapperToTest(String nameTo, String surnameTo, int age) {
		this.nameTo = nameTo;
		this.surnameTo = surnameTo;
		this.age = age;
	}
	
	public ClassMapperToTest() {
//		this("Pippo", "Sora");
	}
	
	public String getNameTo() {
		return this.nameTo;
	}
	public void setNameTo(String nameTo) {
		this.nameTo = nameTo;
	}
	public String getSurnameTo() {
		return this.surnameTo;
	}
	public void setSurnameTo(String surnameTo) {
		this.surnameTo = surnameTo;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getAge() {
		return this.age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
