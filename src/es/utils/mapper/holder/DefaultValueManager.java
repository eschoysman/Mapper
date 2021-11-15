package es.utils.mapper.holder;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.Default;
import es.utils.mapper.defaultvalue.DefaultValueFactory;
import es.utils.mapper.logger.LogConstant;
import es.utils.mapper.logger.MapperLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class DefaultValueManager {

	private static MapperLogger logger = MapperLogger.forType(DefaultValueManager.class);

	private FieldHolder fieldHolder;
	private Supplier<?> supplier;
	
	@SuppressWarnings("serial")
	private static final Map<Class<?>,Function<DefaultAnnotationSupplier,Supplier<?>>> SUPPLIERS = new HashMap<Class<?>,Function<DefaultAnnotationSupplier,Supplier<?>>>(){{
		put(String.class,	DefaultAnnotationSupplier::getStringSupplier);
		put(Byte.class,		DefaultAnnotationSupplier::getByteSupplier);
		put(Short.class,	DefaultAnnotationSupplier::getShortSupplier);
		put(Integer.class,	DefaultAnnotationSupplier::getIntegerSupplier);
		put(Long.class,		DefaultAnnotationSupplier::getLongSupplier);
		put(Float.class,	DefaultAnnotationSupplier::getFloatSupplier);
		put(Double.class,	DefaultAnnotationSupplier::getDoubleSupplier);
		put(Boolean.class,	DefaultAnnotationSupplier::getBooleanSupplier);
		put(Character.class,DefaultAnnotationSupplier::getCharacterSupplier);
		put(Class.class,	DefaultAnnotationSupplier::getClassSupplier);
	}};
	
	public DefaultValueManager(Mapper mapper, FieldHolder fieldHolder) {
		this.fieldHolder = fieldHolder;
		process(mapper);
	}
	
	private void process(Mapper mapper) {
		Field field = fieldHolder.getField();
		Class<?> fieldType = fieldHolder.getType();
		Default[] fieldDefault = field.getAnnotationsByType(Default.class);
		for(Default annotation : fieldDefault) {
			extractDefaultValueSupplier(fieldType,new DefaultAnnotationSupplier(mapper,annotation));
			if(this.supplier!=null) {
				break;
			}
		}
		if(this.supplier==null) {
			this.supplier = mapper.config().getDefaultValueSupplier(fieldType);
		}
		if(this.supplier==null) {
			this.supplier = ()->null;
		}
	}

	private void extractDefaultValueSupplier(Class<?> type, DefaultAnnotationSupplier defaultAnnotationSupplier) {
		if(type.isEnum()) {
			this.supplier = defaultAnnotationSupplier.getEnumSupplier();
		}
		if(this.supplier==null) {
			this.supplier = SUPPLIERS.getOrDefault(type,DefaultAnnotationSupplier::getSupplier)
									 .apply(defaultAnnotationSupplier);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> Supplier<T> getSupplier() {
		return (Supplier<T>)this.supplier;
	}
	
	private static class DefaultAnnotationSupplier {
		
		private Supplier<String> stringSupplier;
		private Supplier<Byte> byteSupplier;
		private Supplier<Short> shortSupplier;
		private Supplier<Integer> integerSupplier;
		private Supplier<Long> longSupplier;
		private Supplier<Float> floatSupplier;
		private Supplier<Double> doubleSupplier;
		private Supplier<Boolean> booleanSupplier;
		private Supplier<Character> characterSupplier;
		private Supplier<Class<?>> typeSupplier;
		private Supplier<? extends Enum<?>> enumSupplier;
		private Supplier<?> supplier;
		private Supplier<?> factorySupplier;
		private Supplier<Charset> charsetSupplier;
		
		public DefaultAnnotationSupplier(Mapper mapper, Default annotation) {
			process(mapper,annotation);
		}

		private void process(Mapper mapper, Default annotation) {
			try {
				Charset charset = Charset.forName(annotation.charset());
				this.charsetSupplier = ()->charset;
			}
			catch(Exception e) {
				Charset charset = Charset.defaultCharset();
				this.charsetSupplier = ()->charset;
			}
			String value = new String(annotation.value().getBytes(charsetSupplier.get()));
			this.stringSupplier = ()->value;
			long longValue = annotation.number();
			this.byteSupplier = ()->(byte)longValue;
			this.shortSupplier = ()->(short)longValue;
			this.integerSupplier = ()->(int)longValue;
			this.longSupplier = ()->longValue;
			double doubleValue = annotation.decimal();
			this.floatSupplier = ()->(float)doubleValue;
			this.doubleSupplier = ()->doubleValue;
			boolean booleanValue = annotation.bool();
			this.booleanSupplier = ()->booleanValue;
			char charValue = annotation.character();
			this.characterSupplier = ()->charValue;
			Class<?> typeValue = annotation.type();
			this.typeSupplier = ()->typeValue;
			if(annotation.enumType() != Enum.class) {
				@SuppressWarnings("unchecked")
				Enum<?> enumInsance = Enum.valueOf(annotation.enumType(),stringSupplier.get());
				this.enumSupplier = ()->enumInsance;
			}
			if(annotation.supplier() != Supplier.class) {
				try {
					this.supplier = (Supplier<?>)annotation.supplier().newInstance();
				} catch (InstantiationException | IllegalAccessException ignored) {
				}
			}
			if(annotation.factory() != DefaultValueFactory.class) {
				String[] parametersInput = annotation.parameters();
				String[] parametersOutput = new String[parametersInput.length];
				Arrays.setAll(parametersOutput,i->new String(parametersInput[i].getBytes(charsetSupplier.get())));
				Supplier<String[]> parametersSupplier = ()->parametersOutput;
				try {
					DefaultValueFactory<?> factoryInstance = annotation.factory().getConstructor(Mapper.class).newInstance(mapper);
					this.factorySupplier = factoryInstance.getSupplier(stringSupplier.get(),parametersSupplier.get());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					logger.warning(LogConstant.CREATION_LEVEL_FIELD,"WARNING - The factory for {} does not have a constructor accepting a Mapper instance; the factory is ignored.",annotation.factory());
				}
			}
		}

		public Supplier<String> getStringSupplier() {
			return this.stringSupplier;
		}
		public Supplier<Byte> getByteSupplier() {
			return this.byteSupplier;
		}
		public Supplier<Short> getShortSupplier() {
			return this.shortSupplier;
		}
		public Supplier<Integer> getIntegerSupplier() {
			return this.integerSupplier;
		}
		public Supplier<Long> getLongSupplier() {
			return this.longSupplier;
		}
		public Supplier<Float> getFloatSupplier() {
			return this.floatSupplier;
		}
		public Supplier<Double> getDoubleSupplier() {
			return this.doubleSupplier;
		}
		public Supplier<Boolean> getBooleanSupplier() {
			return this.booleanSupplier;
		}
		public Supplier<Character> getCharacterSupplier() {
			return this.characterSupplier;
		}
		public Supplier<Class<?>> getClassSupplier() {
			return this.typeSupplier;
		}
		public Supplier<? extends Enum<?>> getEnumSupplier() {
			return this.enumSupplier;
		}
		public Supplier<?> getSupplier() {
			return this.factorySupplier!=null ? this.factorySupplier : this.supplier;
		}
		
	}
	
}
