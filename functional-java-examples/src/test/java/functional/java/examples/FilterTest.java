package functional.java.examples;

import static com.google.common.collect.Iterables.filter;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;

import functional.java.examples.Filter.Condition;

public class FilterTest {
	private static final List<String> PETS = asList("cat", "dog", "bunny", "tiger");
	private static final List<String> BEASTS = asList("tiger", "lion", "rhino", "bear");

	@Test
	public void filterBeasts() {
		List<String> petsWithoutBeasts = new Filter().apply(PETS, new Condition<String>() {
			@Override
			public boolean apply(String input) {
				return isNoBeast(input);
			}
		});
		assertNoBeasts(petsWithoutBeasts);
	}
	
	@Test
	public void filterBeastsWithGuava() {
		Iterable<String> petsWithoutBeasts = filter(PETS, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return isNoBeast(input);
			}
		});
		assertNoBeasts(petsWithoutBeasts);
	}
	
	private void assertNoBeasts(Iterable<String> petsWithoutBeasts) {
		for (String pet : petsWithoutBeasts) {
			for (String beast : BEASTS) {
				assertFalse(pet.equals(beast));
			}
		}
	}
	
	private static boolean isNoBeast(String input) {
		for (String beast : BEASTS) {
			if(input.equals(beast)) {
				return false;
			}
		}
		return true;
	}
}
