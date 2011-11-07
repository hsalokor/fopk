package functional.java.examples;

import static functional.java.examples.AddressTransformer.*;

public class PostalAddress implements ContactInformation {
	private final String address;

	public PostalAddress(String address) {
		this.address = address;
	}

	@Override
	public String getStreetAddress() {
		return First.of(Lines.from(address));
	}

	@Override
	public String getPostCode() {
		return firstWord(Second.of(Lines.from(address)));
	}

	@Override
	public String getPostOffice() {
		return secondWord(Second.of(Lines.from(address)));
	}
}
