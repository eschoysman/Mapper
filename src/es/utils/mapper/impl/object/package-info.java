/**
 * Provides a class handling the mapping between two different types.<br>
 * The package contains the following classes (sub-classes of {@code MapperObject}):
 * <ul>
 * <li>{@code ClassMapper} : creating a {@code MapperObject} that convert on object into another field by field</li>
 * <li>{@code DirectMapper} : creating a {@code MapperObject} that convert on object into another with a custom convertion function</li>
 * <li>{@code EnumMapper} : creating a {@code MapperObject} that convert on enum type into another</li>
 * </ul>
 * @author eschoysman
 * @see es.utils.mapper.impl.MapperObject
 * @see es.utils.mapper.impl.object.ClassMapper
 * @see es.utils.mapper.impl.object.DirectMapper
 * @see es.utils.mapper.impl.object.EnumMapper
 */
package es.utils.mapper.impl.object;