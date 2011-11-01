package functional.java.examples;

import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

public class AddressTransformer implements Function<String, ContactInformation> {
	@Override
	public ContactInformation apply(String input) {
		try {
			return toAddress(lines(input));
		} catch (ArrayIndexOutOfBoundsException e) {
			return NO_CONTACT_INFORMATION;
		}
	}
	
	private Address toAddress(final List<String> addressLines) {
		AddressBuilder addressBuilder = new AddressBuilder().withStreetAddress(first(addressLines));
		addressBuilder.withPostCode(first(words(second(addressLines))));
		addressBuilder.withPostOffice(second(words(second(addressLines))));
		return addressBuilder.build();
	}

	private static List<String> lines(String input) {
		return new Lines().apply(input);
	}

	private static class Lines implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return Arrays.asList(input.split("\n"));
		}
	}
	
	private static List<String> words(final String input) {
		return new Words().apply(input);
	}

	private static class Words implements Function<String, List<String>> {
		@Override
		public List<String> apply(String input) {
			return Arrays.asList(input.split(" "));
		}
	}

	private static String second(final List<String> postCodeAndOffice) {
		return postCodeAndOffice.get(1);
	}

	private static String first(final List<String> addressLines) {
		return addressLines.get(0);
	}
}
