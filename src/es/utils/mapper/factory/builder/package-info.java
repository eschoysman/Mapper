/**
 * Provides a a Step builder implementation for creating a ElementMapper step by step.<br>
 * The steps are:
 * <ol>
 * <li>{@code From}: (mandatory) create the getter.</li>
 * <li>{@code DefaultInput}: (optional) allow to specify a default value if the getter of the {@code From} step is null.</li>
 * <li>{@code Transformer}: (optional, repeatable) create the transformer between the result of the getter and the input of the setter. It is possible to concatenate multiple transformations.</li>
 * <li>{@code DefaultOutput}: (optional) allow to specify a default value if the result of the transformation is null.</li>
 * <li>{@code To}: (mandatory) create the setter.</li>
 * <li>{@code ElementMapperBuilder}: (mandatory) create the ElementMapper.</li>
 * </ol>
 * <br><br>
 * Here the flow chart of the builder:<br><br>
 * <img src="./doc-files/stepBuilderFlow.png" alt="Flow: From -> [DefaultInput] -> [Transformer (repeatable)] -> [DefaultOutput] -> To ">
 * <br><br>
 * @author eschoysman
 * @see es.utils.mapper.factory.builder.From
 * @see es.utils.mapper.factory.builder.DefaultInput
 * @see es.utils.mapper.factory.builder.Transformer
 * @see es.utils.mapper.factory.builder.DefaultOutput
 * @see es.utils.mapper.factory.builder.To
 * @see es.utils.mapper.factory.builder.EMBuilder
 * @see es.utils.mapper.factory.builder.EMBuilder#using(es.utils.mapper.Mapper,es.utils.mapper.impl.object.ClassMapper)
 */
package es.utils.mapper.factory.builder;