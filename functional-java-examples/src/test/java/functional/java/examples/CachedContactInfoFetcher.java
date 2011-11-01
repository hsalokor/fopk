package functional.java.examples;

import com.google.common.base.Function;

public class CachedContactInfoFetcher implements ContactInfoFetcher {
	private final Function<DbKey, String> cache = CachingFunction.cache(new DbContactInfoFetcher());

	@Override
	public String fetch(DbKey input) {
		return cache.apply(input);
	}
}
