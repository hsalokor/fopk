package functional.java.examples;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class DbAddress implements ContactInformation {
	@Inject
	private QueryRunner database;
	private final Integer id;

	public DbAddress(Integer id) {
		this.id = id;
	}

	@Override
	public String getStreetAddress() {
		return fetchDbContactInfo("streetAddress");
	}

	@Override
	public String getPostCode() {
		return fetchDbContactInfo("postCode");
	}

	@Override
	public String getPostOffice() {
		return fetchDbContactInfo("postOffice");
	}

	private String fetchDbContactInfo(String fieldName) {
		try {
			return database.query("select " + fieldName + " from contactinfo where id = ?", new FirstStringHandler(), id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static final class FirstStringHandler implements ResultSetHandler<String> {
		@Override
		public String handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return rs.getString(0);
			}
			throw new SQLException("No result");
		}
	}
}
