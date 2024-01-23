//package es.utils.mapper.holder.suppliers;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.annotation.Default;
//import es.utils.mapper.defaultvalue.DefaultValueFactory;
//import lombok.extern.slf4j.Slf4j;
//
//import java.lang.reflect.InvocationTargetException;
//import java.nio.charset.Charset;
//import java.text.MessageFormat;
//import java.util.Arrays;
//import java.util.function.Supplier;
//
//@Slf4j
//public class DefaultValueFactoryAnnotationSupplier extends DefaultAnnotationSupplier2<Object> {
//
//    public DefaultValueFactoryAnnotationSupplier(Mapper mapper, Default annotation) {
//        if(annotation.factory() != DefaultValueFactory.class) {
//            Charset charsetTemp = null;
//            try {
//                charsetTemp = Charset.forName(annotation.charset());
//            } catch (IllegalArgumentException e) {
//                charsetTemp = Charset.defaultCharset();
//            }
//            Charset charset = charsetTemp;
//            String value = new String(annotation.value().getBytes(charset));
//            String[] parametersInput = annotation.parameters();
//            String[] parametersOutput = new String[parametersInput.length];
//            Arrays.setAll(parametersOutput, i->new String(parametersInput[i].getBytes(charset)));
//            Supplier<String[]> parametersSupplier = ()->parametersOutput;
//            try {
//                DefaultValueFactory<?> factoryInstance = annotation.factory().getConstructor(Mapper.class).newInstance(mapper);
//                this.factorySupplier = (Supplier<Object>)factoryInstance.getSupplier(value,parametersSupplier.get());
//            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//                    | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
//	            String message = "WARNING - The factory for {} does not have a constructor accepting a Mapper instance; the factory is ignored.";
//	            // TODO $$$ togliere qeusto sout, ma due test case vanno in errore dato che guardano quello che viene scritto in console
//	            System.out.println(MessageFormat.format(message,annotation.factory()));
//	            log.warn(message,annotation.factory());
//            }
//        }
//    }
//
//}
