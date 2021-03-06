/**
 * Provides the annotation that can be used to configure the mappings operations directly from the Object class.<br>
 * The package contains the following annotations:
 * <ul>
 * <li>{@code @AliasNames} : used to specify the alternative names of a field.</li>
 * <li>{@code @CollectionType} : used for field of a type that extends {@code Collection}, it allow the user to specify the type of collection to have in the destination class.</li>
 * <li>{@code @Converter} : used to specify the converter to use for a field. This is a repeatable annotation.</li>
 * <li>{@code @Default} : used to specify a default value to use for a field.</li>
 * <li>{@code @IgnoreField} : the annotated field will be ignored during the mapping. If used on a class, it can take a list of string containing the field names or aliases to be ignored.</li>
 * </ul>
 * @author eschoysman
 * @see es.utils.mapper.annotation.AliasNames
 * @see es.utils.mapper.annotation.CollectionType
 * @see es.utils.mapper.annotation.Converter
 * @see es.utils.mapper.annotation.Default
 * @see es.utils.mapper.annotation.IgnoreField
 */
package es.utils.mapper.annotation;