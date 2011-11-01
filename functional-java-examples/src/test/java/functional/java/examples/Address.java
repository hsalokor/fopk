package functional.java.examples;

public class Address implements ContactInformation {
	private final String streetAddress;
	private final String postCode;
	private final String postOffice;

	public Address(String streetAddress, String postCode, String postOffice) {
		this.streetAddress = streetAddress;
		this.postCode = postCode;
		this.postOffice = postOffice;
	}

	@Override
	public String getStreetAddress() {
		return streetAddress;
	}

	@Override
	public String getPostCode() {
		return postCode;
	}

	@Override
	public String getPostOffice() {
		return postOffice;
	}
}
