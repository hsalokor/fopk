package functional.java.examples;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.google.common.base.Function;

public class DbContactInfoFetcher implements ContactInfoFetcher, Function<DbKey, String> {
	@Inject
	private QueryRunner database;

	@Override
	public String fetch(DbKey input) {
		return apply(input);
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

	@Override
	public String apply(DbKey input) {
		try {
			return database.query("select " + input.getFieldName() + " from contactinfo where id = ?", new FirstStringHandler(),
				input.getId());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
