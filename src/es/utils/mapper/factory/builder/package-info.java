/**
 * Provides a a Step builder implementation for creating a ElementMapper step by step<br>
 * The steps are:
 * <ol>
 * <li>{@code From}: create the getter</li>
 * <li>{@code Transformer}: (optional) create the transformer</li>
 * <li>{@code To}: create the setter</li>
 * <li>{@code Default}: (optional) create a default value for the setter</li>
 * <li>{@code ElementMapperBuilder}: create the ElementMapper</li>
 * </ol>
 * @author eschoysman
 * @see es.utils.mapper.factory.builder.From
 * @see es.utils.mapper.factory.builder.Transformer
 * @see es.utils.mapper.factory.builder.To
 * @see es.utils.mapper.factory.builder.Default
 * @see es.utils.mapper.factory.builder.ElementMapperBuilder
 */
package es.utils.mapper.factory.builder;