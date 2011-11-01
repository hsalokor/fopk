package functional.java.examples;

import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;

import java.util.Arrays;
import java.util.List;

public class AddressTransformer implements Function<String, ContactInformation> {
	@Override
	public ContactInformation apply(String input) {
		try {
			return toAddress(addressLines(input));
		} catch (ArrayIndexOutOfBoundsException e) {
			return NO_CONTACT_INFORMATION;
		}
	}
	
	private Address toAddress(final List<String> addressLines) {
		AddressBuilder addressBuilder = new AddressBuilder().withAddress(firstItem(addressLines));
		addressBuilder.withPostCode(firstItem(postCodeAndOffice(addressLines)));
		addressBuilder.withPostOffice(secondItem(postCodeAndOffice(addressLines)));
		return addressBuilder.build();
	}

	private List<String> addressLines(String input) {
		return Arrays.asList(input.split("\n"));
	}

	private List<String> postCodeAndOffice(final List<String> addressLines) {
		return Arrays.asList(secondItem(addressLines).split(" "));
	}

	private String secondItem(final List<String> postCodeAndOffice) {
		return postCodeAndOffice.get(1);
	}

	private String firstItem(final List<String> addressLines) {
		return addressLines.get(0);
	}
}
