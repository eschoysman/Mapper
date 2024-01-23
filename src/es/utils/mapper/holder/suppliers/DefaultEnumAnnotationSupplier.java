//package es.utils.mapper.holder.suppliers;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.annotation.Default;
//
//import java.nio.charset.Charset;
//
//public class DefaultEnumAnnotationSupplier extends DefaultAnnotationSupplier2<Enum<? extends Enum<?>>> {
//
//    public DefaultEnumAnnotationSupplier(Mapper mapper, Default annotation) {
//        if(annotation.enumType() != Enum.class) {
//            Charset charset;
//            try {
//                charset = Charset.forName(annotation.charset());
//            } catch (IllegalArgumentException e) {
//                charset = Charset.defaultCharset();
//            }
//            String value = new String(annotation.value().getBytes(charset));
//            Enum<?> enumInsance = Enum.valueOf(annotation.enumType(), value);
//            this.supplier = () -> enumInsance;
//        }
//    }
//
//}
