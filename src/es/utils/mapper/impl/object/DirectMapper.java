package es.utils.mapper.impl.object;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.impl.MapperObject;

/**
 * This class customize the abstract class {@code MapperObject} creating a {@code MapperObject} that convert on object of type {@code T} into a {@code U} with a custom convertion function.
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
	 * @param transformer the function that execute all the convertion from {@code T} to {@code U}
	 */
	public DirectMapper(Class<T> from, Class<U> to, Function<T,U> transformer) {
		super(from,to);
		this.transformer = Objects.requireNonNull(transformer);
	}

	@Override
	public void activate(Mapper mapper) {
	}

	@Override
	protected U mapValue(T from) throws MappingException {
		return transformer.apply(from);
	}

	@Override
	protected U mapValue(T from, U to) {
		return Optional.ofNullable(transformer.apply(from)).orElse(to);
	}
	
}
