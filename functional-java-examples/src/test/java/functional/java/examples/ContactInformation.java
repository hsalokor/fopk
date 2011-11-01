package functional.java.examples;

import java.io.Serializable;

public interface ContactInformation extends Serializable {
	public static final ContactInformation NO_CONTACT_INFORMATION = new NoContactInformation();

	String getStreetAddress();

	String getPostCode();

	String getPostOffice();

	public static final class NoContactInformation implements ContactInformation {
		@Override
		public String getStreetAddress() {
			return "";
		}

		@Override
		public String getPostCode() {
			return "";
		}

		@Override
		public String getPostOffice() {
			return "";
		}
	}
}
