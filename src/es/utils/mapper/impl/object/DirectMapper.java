package es.utils.mapper.impl.object;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import es.utils.mapper.Mapper;
import es.utils.mapper.exception.MappingException;
import es.utils.mapper.impl.MapperObject;

/**
 * 
 * @author eschyosman
 *
 * @param <T>
 * @param <U>
 */
public class DirectMapper<T,U> extends MapperObject<T,U> {

	private Function<T,U> transformer;

	/**
	 * 
	 * @param from
	 * @param to
	 * @param transformer
	 */
	public DirectMapper(Class<T> from, Class<U> to, Function<T,U> transformer) {
		super(from,to);
		this.transformer = Objects.requireNonNull(transformer);
	}

	/**
	 * 
	 */
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
