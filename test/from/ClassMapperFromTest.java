package from;

import es.utils.mapper.annotation.AliasNames;

public class ClassMapperFromTest {
	
	@AliasNames("name")
	private String nameFrom;
	@AliasNames("surname")
	private String surnameFrom;
	
	public ClassMapperFromTest(String nameFrom, String surnameFrom) {
		this.nameFrom = nameFrom;
		this.surnameFrom = surnameFrom;
	}
	
	public ClassMapperFromTest() {
		this("Pippo", "Sora");
	}
	
	public String getNameFrom() {
		return this.nameFrom;
	}
	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}
	public String getSurnameFrom() {
		return this.surnameFrom;
	}
	public void setSurnameFrom(String surnameFrom) {
		this.surnameFrom = surnameFrom;
	}

}
