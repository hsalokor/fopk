package functional.java.examples;

import static com.google.common.base.Functions.compose;
import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

public class AddressTransformer implements Function<String, ContactInformation> {
	@Override
	public ContactInformation apply(String input) {
		try {
			return toAddress(Lines.from(input));
		} catch (ArrayIndexOutOfBoundsException e) {
			return NO_CONTACT_INFORMATION;
		}
	}

	private Address toAddress(final List<String> addressLines) {
		AddressBuilder addressBuilder = new AddressBuilder().withStreetAddress(First.of(addressLines));
		addressBuilder.withPostCode(firstWord(Second.of(addressLines)));
		addressBuilder.withPostOffice(secondWord(Second.of(addressLines)));
		return addressBuilder.build();
	}
	
	public static String firstWord(String input) {
		return compose(new First(), new Words()).apply(input);
	}
	
	public static String secondWord(String input) {
		return compose(new Second(), new Words()).apply(input);
	}

	public static class Lines implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return asList(input.split("\n"));
		}
		
		public static List<String> from(String input) {
			return new Lines().apply(input);
		}
	}

	public static class Words implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return Arrays.asList(input.split(" "));
		}
	}

	public static class First implements Function<List<String>, String> {
		@Override
		public String apply(List<String> input) {
			return input.get(0);
		}
		
		public static String of(List<String> input) {
			return new First().apply(input);
		}
	}
	
	public static class Second implements Function<List<String>, String> {
		@Override
		public String apply(List<String> input) {
			return input.get(1);
		}
		
		public static String of(List<String> input) {
			return new Second().apply(input);
		}
	}
}
