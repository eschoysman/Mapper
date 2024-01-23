//package es.utils.mapper.holder.suppliers;
//
//import es.utils.mapper.Mapper;
//import es.utils.mapper.annotation.Default;
//
//import java.util.function.Supplier;
//
//public class DefaultSupplierAnnotationSupplier extends DefaultAnnotationSupplier2<Object> {
//
//    public DefaultSupplierAnnotationSupplier(Mapper mapper, Default annotation) {
//        if(annotation.supplier() != Supplier.class) {
//            try {
//                this.supplier = annotation.supplier().newInstance();
//            } catch (InstantiationException | IllegalAccessException ignored) {
//            }
//        }
//    }
//
//}
