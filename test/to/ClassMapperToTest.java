package to;

import es.utils.mapper.annotation.AliasNames;

public class ClassMapperToTest {

	@AliasNames("name")
	public String nameTo;
	@AliasNames("surname")
	private String surnameTo;
	private String fullName;
	private int age;
	
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
