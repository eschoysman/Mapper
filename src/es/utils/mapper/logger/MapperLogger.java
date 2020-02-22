//package es.utils.mapper.logger;
//
//import java.text.MessageFormat;
//import java.util.EnumSet;
//
//public class MapperLogger {
//
//	public enum Level { WARNING, CREATION, MAPPER, TYPE, FIELD };
//	
//	private EnumSet<Level> enabled;
//
//	public MapperLogger() {
//		disableAll();
//		enableWarning();
//	}
//	
//	public MapperLogger enableAll() {
//		this.enabled = EnumSet.allOf(Level.class);
//		return this;
//	}
//	public MapperLogger disableAll() {
//		this.enabled = EnumSet.noneOf(Level.class);
//		return this;
//	}
//
//	public MapperLogger enableWarning() {
//		this.enabled.add(Level.WARNING);
//		return this;
//	}
//	public MapperLogger disableWarning() {
//		this.enabled.remove(Level.WARNING);
//		return this;
//	}
//
//	public MapperLogger enableCreation() {
//		this.enabled.add(Level.CREATION);
//		return this;
//	}
//	public MapperLogger disableCreation() {
//		this.enabled.remove(Level.CREATION);
//		return this;
//	}
//
//	public MapperLogger enableMapper() {
//		this.enabled.add(Level.MAPPER);
//		return this;
//	}
//	public MapperLogger disableMapper() {
//		this.enabled.remove(Level.MAPPER);
//		return this;
//	}
//
//	public MapperLogger enableType() {
//		this.enabled.add(Level.TYPE);
//		return this;
//	}
//	public MapperLogger disableType() {
//		this.enabled.remove(Level.TYPE);
//		return this;
//	}
//
//	public MapperLogger enableField() {
//		this.enabled.add(Level.FIELD);
//		return this;
//	}
//	public MapperLogger disableField() {
//		this.enabled.remove(Level.FIELD);
//		return this;
//	}
//
//	public void logCreation(String format, Object...arguments) {
//		log(Level.CREATION,format,arguments);
//	}
//	public void logMapper(String format, Object...arguments) {
//		log(Level.MAPPER,format,arguments);
//	}
//	public void logType(String format, Object...arguments) {
//		log(Level.TYPE,format,arguments);
//	}
//	public void logField(String format, Object...arguments) {
//		log(Level.FIELD,format,arguments);
//	}
//
//	public void log(Level level, String format, Object...arguments) {
//		if(enabled.contains(level)) {
//			System.out.println(MessageFormat.format(format,arguments));
//		}
//	}
//
//	public void warning(String format, Object...arguments) {
//		if(enabled.contains(Level.WARNING)) {
//			System.out.println(MessageFormat.format(format,arguments));
//		}
//	}
//	
//}
