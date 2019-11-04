package es.utils.mapper.impl.object;

import java.util.Optional;
import java.util.function.Function;

import es.utils.mapper.converter.AbstractConverter;
import es.utils.mapper.impl.MapperObject;
import es.utils.mapper.utils.ThrowingFunction;

/**
 * This class customize the abstract class {@code MapperObject} creating a {@code MapperObject} that convert on object of type {@code T} into a {@code U} with a custom conversion function.
 * @author eschoysman
 *
 * @param <T> the type of the origin object
 * @param <U> the type of the destination object
 * 
 * @see MapperObject
 * @see ClassMapper
 * @see EnumMapper
 */
public class DirectMapper<T,U> extends MapperObject<T,U> {

	private Function<T,U> transformer;

	/**
	 * Create a {@code MapperObject} from type {@code T} to type {@code U} applying the given function as mapping.
	 * @param from the type of the origin object
	 * @param to the type of the destination object
	 * @param transformer the function that execute all the conversion from {@code T} to {@code U}
	 */
	public DirectMapper(Class<T> from, Class<U> to, Function<T,U> transformer) {
		super(from,to);
		this.transformer = transformer;
	}

	@Override
	public final void activate() {
	}

	@Override
	protected final U mapValue(T from) {
		if(transformer==null && this instanceof AbstractConverter) {
			ThrowingFunction<T,U> convert = ((AbstractConverter<T,U>)this)::convert;
			this.transformer = convert;
		}
		return transformer.apply(from);
	}

	@Override
	protected final U mapValue(T from, U to) {
		return Optional.ofNullable(mapValue(from)).orElse(to);
	}
	
}
