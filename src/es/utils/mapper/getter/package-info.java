/**
 * Provides the implementation of the abstract {@code Getter} operation and its implementations.<br>
 * The package contains the following getter types:
 * <ul>
 * <li>{@code Getter} : handle the logic of a generic {@code getter} operation</li>
 * <li>{@code FunctionGetter} (sub-class of {@code Getter}) : create a {@code Getter} instance from a {@code Function}</li>
 * <li>{@code FieldGetter} (sub-class of {@code FunctionGetter}) : create a {@code Getter} instance from a {@code Field}</li>
 * <li>{@code SupplierGetter} (sub-class of {@code FunctionGetter}) : create a {@code Getter} instance from a {@code Supplier}</li>
 * <li>{@code ValueGetter} (sub-class of {@code SupplierGetter}) : create a {@code Getter} instance from a constant value</li>
 * </ul>
 * @author eschoysman
 * @see es.utils.mapper.getter.Getter
 * @see es.utils.mapper.getter.FunctionGetter
 * @see es.utils.mapper.getter.FieldGetter
 * @see es.utils.mapper.getter.SupplierGetter
 */
package es.utils.mapper.getter;