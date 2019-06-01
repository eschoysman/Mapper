/**
 * Provides the implementation of the abstract {@code Setter} operation and its implementations.<br>
 * The package contains the following setter types:
 * <ul>
 * <li>{@code Setter} : handle the logic of a generic {@code setter} operation</li>
 * <li>{@code FunctionSetter} (sub-class of {@code Setter}) : create a {@code Setter} instance from a {@code BiConsumer}</li>
 * <li>{@code FieldSetter} (sub-class of {@code FunctionSetter}) : create a {@code Setter} instance from a {@code Field}</li>
 * </ul>
 * @author eschoysman
 * @see es.utils.mapper.setter.Setter
 * @see es.utils.mapper.setter.FunctionSetter
 * @see es.utils.mapper.setter.FieldSetter
 */
package es.utils.mapper.setter;