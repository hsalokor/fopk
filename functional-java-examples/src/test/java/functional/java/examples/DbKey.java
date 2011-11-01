package functional.java.examples;

public class DbKey {
	private final String fieldName;
	private final Integer id;

	public DbKey(String fieldName, Integer id) {
		this.fieldName = fieldName;
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Integer getId() {
		return id;
	}
}