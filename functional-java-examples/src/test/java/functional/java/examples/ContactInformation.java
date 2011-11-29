package functional.java.examples;

import java.io.Serializable;

public interface ContactInformation extends Serializable {
	public static final ContactInformation NO_CONTACT_INFORMATION = new NoContactInformation();

	String streetAddress();

	String postCode();

	String postOffice();

	public static final class NoContactInformation implements ContactInformation {
		@Override
		public String streetAddress() {
			return "";
		}

		@Override
		public String postCode() {
			return "";
		}

		@Override
		public String postOffice() {
			return "";
		}
	}
}
