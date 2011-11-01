package functional.java.examples;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class CachingFunction<F, T> implements Function<F, T> {
	private final Cache<F, T> cache;

	public CachingFunction(final Function<F, T> source) {
		cache = CacheBuilder.newBuilder().softValues().build(CacheLoader.from(source));
	}

	@Override
	public T apply(final F input) {
		try {
			return cache.get(input);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public static <F, T> Function<F, T> cache(final Function<F, T> source) {
		return new CachingFunction<F, T>(source);
	}
}
