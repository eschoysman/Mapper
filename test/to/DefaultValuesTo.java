package to;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import defaultvalue.DateFactory;
import defaultvalue.DateFactory2;
import defaultvalue.DateSupplier;
import es.utils.mapper.annotation.Default;

public class DefaultValuesTo {

	@Default("string di default")
	private String string;
	@Default(value="charset",charset="UTF-8")
	private String string2;
	@Default(number=42)
	private Byte numByte;
	@Default(number=42)
	private Short numShort;
	@Default(number=42)
	private Integer numInteger;
	@Default(number=42)
	private Long numLong;
	@Default(decimal=Math.PI)
	private Float numFloat;
	@Default(decimal=Math.PI)
	private Double numDouble;
	@Default(bool=true)
	private Boolean bool;
	@Default(character='c')
	private Character character;
	@Default(type=DefaultValuesTo.class)
	private Class<DefaultValuesTo> type;
	@Default(enumType=TimeUnit.class,value="DAYS")
	private TimeUnit timeUnit;
	@Default(supplier=DateSupplier.class)
	private Date date;
	@Default(value="24/12/2019",factory=DateFactory.class,parameters="dd/MM/yyyy")
	private Date dateF;
	@Default(value="24/12/2019",factory=DateFactory2.class,parameters="dd/MM/yyyy")
	private Date dateF2;
	
	public DefaultValuesTo() {}


	public String getString() {
		return string;
	}
	
	public String getString2() {
		return string2;
	}

	public Byte getNumByte() {
		return numByte;
	}

	public Short getNumShort() {
		return numShort;
	}

	public Integer getNumInteger() {
		return numInteger;
	}

	public Long getNumLong() {
		return numLong;
	}

	public Float getNumFloat() {
		return numFloat;
	}

	public Double getNumDouble() {
		return numDouble;
	}

	public Boolean getBool() {
		return bool;
	}

	public Character getCharacter() {
		return character;
	}

	public Class<DefaultValuesTo> getType() {
		return type;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public Date getDate() {
		return date;
	}

	public Date getDateF() {
		return dateF;
	}

	public Date getDateF2() {
		return dateF2;
	}

}
