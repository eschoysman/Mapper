//package es.utils.mapper.holder.suppliers;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.annotation.Default;
//
//import java.nio.charset.Charset;
//
//public class DefaultStringAnnotationSupplier extends DefaultAnnotationSupplier2<String> {
//
//    public DefaultStringAnnotationSupplier(Mapper mapper, Default annotation) {
//        Charset charset;
//        try {
//            charset = Charset.forName(annotation.charset());
//        } catch (IllegalArgumentException e) {
//            charset = Charset.defaultCharset();
//        }
//        String value = new String(annotation.value().getBytes(charset));
//        this.supplier = () -> value;
//    }
//
//}
