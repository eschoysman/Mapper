//package es.utils.mapper.holder.suppliers;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.annotation.Default;
//
//import java.nio.charset.Charset;
//
//public class DefaultCharsetAnnotationSupplier extends DefaultAnnotationSupplier2<Charset> {
//
//    public DefaultCharsetAnnotationSupplier(Mapper mapper, Default annotation) {
//        try {
//            Charset charset = Charset.forName(annotation.charset());
//            this.supplier = ()->charset;
//        } catch (IllegalArgumentException e) {
//            Charset charset = Charset.defaultCharset();
//            this.supplier = ()->charset;
//        }
//    }
//
//}
