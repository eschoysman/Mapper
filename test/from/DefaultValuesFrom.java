package from;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DefaultValuesFrom {

	private String string;
	private Byte numByte;
	private Short numShort;
	private Integer numInteger;
	private Long numLong;
	private Float numFloat;
	private Double numDouble;
	private Boolean bool;
	private Character character;
	private Class<DefaultValuesFrom> type;
	private TimeUnit timeUnit;
	private Date date;
	private Date dateF;
	
	public DefaultValuesFrom() {}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Byte getNumByte() {
		return numByte;
	}

	public void setNumByte(Byte numByte) {
		this.numByte = numByte;
	}

	public Short getNumShort() {
		return numShort;
	}

	public void setNumShort(Short numShort) {
		this.numShort = numShort;
	}

	public Integer getNumInteger() {
		return numInteger;
	}

	public void setNumInteger(Integer numInteger) {
		this.numInteger = numInteger;
	}

	public Long getNumLong() {
		return numLong;
	}

	public void setNumLong(Long numLong) {
		this.numLong = numLong;
	}

	public Float getNumFloat() {
		return numFloat;
	}

	public void setNumFloat(Float numFloat) {
		this.numFloat = numFloat;
	}

	public Double getNumDouble() {
		return numDouble;
	}

	public void setNumDouble(Double numDouble) {
		this.numDouble = numDouble;
	}

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public Class<DefaultValuesFrom> getType() {
		return type;
	}

	public void setType(Class<DefaultValuesFrom> type) {
		this.type = type;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateF() {
		return dateF;
	}

	public void setDateF(Date dateF) {
		this.dateF = dateF;
	}

}
