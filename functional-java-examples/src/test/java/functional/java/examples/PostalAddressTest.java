package functional.java.examples;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class PostalAddressTest {
	private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
	private final static ContactInformation postalAddress = new PostalAddress(ADDRESS);
	
	@Test
	public void hasCorrectStreetAddress() {
		assertEquals("Testitie 5", postalAddress.getStreetAddress());
	}

	@Test
	public void hasCorrectPostCode() {
		assertEquals("00999", postalAddress.getPostCode());
	}
	
	@Test
	public void hasCorrectPostOffice() {
		assertEquals("OLEMATON", postalAddress.getPostOffice());
	}
}
