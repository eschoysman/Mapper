package es.utils.mapper.holder.suppliers;

import es.utils.mapper.Mapper;
import es.utils.mapper.annotation.Default;
import es.utils.mapper.defaultvalue.DefaultValueFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class DefaultAnnotationSupplier2 {

    private static class Prova {

        public void method() {
            Function<DefaultAnnotationSupplier,Supplier<?>> function = DefaultAnnotationSupplier::getSupplier;
        }

    }

    private static class DefaultAnnotationSupplier<TYPE> {

        protected Supplier<TYPE> supplier;
        protected Supplier<Object> factorySupplier;

        public Supplier<? super TYPE> getSupplier() {
            return this.factorySupplier != null ? this.factorySupplier : this.supplier;
        }

    }
    private static class DefaultBooleanAnnotationSupplier extends DefaultAnnotationSupplier<Boolean> {
        public DefaultBooleanAnnotationSupplier(Mapper mapper, Default annotation) {
            this.supplier = annotation::bool;
        }
    }
//    private static class DefaultByteAnnotationSupplier extends DefaultAnnotationSupplier<Byte> {
//        public DefaultByteAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::byteValue;
//        }
//    }
    private static class DefaultCharacterAnnotationSupplier extends DefaultAnnotationSupplier<Character> {
        public DefaultCharacterAnnotationSupplier(Mapper mapper, Default annotation) {
            this.supplier = annotation::character;
        }
    }
    private static class DefaultCharsetAnnotationSupplier extends DefaultAnnotationSupplier<Charset> {
        public DefaultCharsetAnnotationSupplier(Mapper mapper, Default annotation) {
            try {
                Charset charset = Charset.forName(annotation.charset());
                this.supplier = ()->charset;
            } catch (IllegalArgumentException e) {
                Charset charset = Charset.defaultCharset();
                this.supplier = ()->charset;
            }
        }
    }
//    private static class DefaultDoubleAnnotationSupplier extends DefaultAnnotationSupplier<Double> {
//        public DefaultDoubleAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::doubleValue;
//        }
//    }
    private static class DefaultEnumAnnotationSupplier extends DefaultAnnotationSupplier<Enum<? extends Enum<?>>> {
        public DefaultEnumAnnotationSupplier(Mapper mapper, Default annotation) {
            if(annotation.enumType() != Enum.class) {
                DefaultStringAnnotationSupplier stringDefault = new DefaultStringAnnotationSupplier(mapper,annotation);
                String value = stringDefault.supplier.get();
                Enum<?> enumInsance = Enum.valueOf(annotation.enumType(), value);
                this.supplier = () -> enumInsance;
            }
        }
    }
//    private static class DefaultFloatAnnotationSupplier extends DefaultAnnotationSupplier<Float> {
//        public DefaultFloatAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::floatValue;
//        }
//    }
//    private static class DefaultIntegerAnnotationSupplier extends DefaultAnnotationSupplier<Integer> {
//        public DefaultIntegerAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::intValue;
//        }
//    }
//    private static class DefaultLongAnnotationSupplier extends DefaultAnnotationSupplier<Long> {
//        public DefaultLongAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::longValue;
//        }
//    }
//    private static class DefaultShortAnnotationSupplier extends DefaultAnnotationSupplier<Short> {
//        public DefaultShortAnnotationSupplier(Mapper mapper, Default annotation) {
//            this.supplier = annotation::shortValue;
//        }
//    }
    private static class DefaultStringAnnotationSupplier extends DefaultAnnotationSupplier<String> {
        private Charset charset;
        public DefaultStringAnnotationSupplier(Mapper mapper, Default annotation) {
            DefaultCharsetAnnotationSupplier charsetDefault = new DefaultCharsetAnnotationSupplier(mapper,annotation);
            charset = charsetDefault.supplier.get();
            String value = new String(annotation.value().getBytes(charset));
            this.supplier = () -> value;
        }
        public Charset getCharset() {
            return this.charset;
        }
    }
    private static class DefaultSupplierAnnotationSupplier<T> extends DefaultAnnotationSupplier<T> {
        public DefaultSupplierAnnotationSupplier(Mapper mapper, Default annotation) {
            if(annotation.supplier() != Supplier.class) {
                try {
                    this.supplier = annotation.supplier().newInstance();
                } catch (InstantiationException | IllegalAccessException ignored) {
                }
            }
        }
    }
    private static class DefaultTypeAnnotationSupplier extends DefaultAnnotationSupplier<Class<?>> {
        public DefaultTypeAnnotationSupplier(Mapper mapper, Default annotation) {
            this.supplier = annotation::type;
        }
    }
    private static class DefaultValueFactoryAnnotationSupplier extends DefaultAnnotationSupplier<Object> {
        public DefaultValueFactoryAnnotationSupplier(Mapper mapper, Default annotation) {
            if(annotation.factory() != DefaultValueFactory.class) {
                DefaultStringAnnotationSupplier stringDefault = new DefaultStringAnnotationSupplier(mapper,annotation);
                String value = stringDefault.supplier.get();
                String[] parametersInput = annotation.parameters();
                String[] parametersOutput = new String[parametersInput.length];
                Arrays.setAll(parametersOutput, i->new String(parametersInput[i].getBytes(stringDefault.getCharset())));
                Supplier<String[]> parametersSupplier = ()->parametersOutput;
                try {
                    DefaultValueFactory<?> factoryInstance = annotation.factory().getConstructor(Mapper.class).newInstance(mapper);
                    this.factorySupplier = (Supplier<Object>)factoryInstance.getSupplier(value,parametersSupplier.get());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
                    log.warn("WARNING - The factory for {} does not have a constructor accepting a Mapper instance; the factory is ignored.",annotation.factory());
                }
            }
        }
    }

}