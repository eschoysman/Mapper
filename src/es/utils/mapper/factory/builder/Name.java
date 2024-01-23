package es.utils.mapper.factory.builder;

import es.utils.mapper.Mapper;
import es.utils.mapper.impl.element.ElementMapper;
import es.utils.mapper.impl.object.ClassMapper;

/**
 * First (optional) step of the builder that allow too set a name to the ElementMapper.<br>
 * This step is optional.<br>
 * Next optional steps: {@link DefaultInput}, {@link Transformer}, {@link DefaultOutput}.<br>
 * Next mandatory steps: {@link From}, {@link To}.
 * @author eschoysman
 * @see ClassMapper#addMapping()
 * @see EMBuilder#using(Mapper,ClassMapper)
 * @see ElementMapper
 * @see <a href="package-summary.html">builder package</a>
 */
public interface Name<IN,OUT> extends From<IN,OUT> {
	
	/**
	 * Set the name of the ElementMapper that wil be used by the Getter and the Setter instance if no different name is provided.<br>
	 * @param name the name of the ElementMapper
	 * @return The next step of the builder: {@link From}.
	 * @see From
	 * @see DefaultInput
	 * @see Transformer
	 * @see DefaultOutput
	 * @see To
	 */
	From<IN,OUT> name(String name);

}
