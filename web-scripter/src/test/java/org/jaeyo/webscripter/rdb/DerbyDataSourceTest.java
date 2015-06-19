package org.jaeyo.webscripter.rdb;

import org.json.JSONArray;
import org.junit.Test;

public class DerbyDataSourceTest {

	@Test
	public void test() {
		DerbyDataSource ds = new DerbyDataSource();
		JSONArray result = ds.getJdbcTmpl().queryForJsonArray("select * from database");
		System.out.println(result.toString(4));
	}
}