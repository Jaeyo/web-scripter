package org.jaeyo.webscripter.rdb;

import java.io.File;
import java.io.FileWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Test {
	@org.junit.Test
	public void test1() throws Exception {
//		OracleDataSource ds = new OracleDataSource();
//		ds.setDatabaseName("SPIDERX");
//		ds.setPassword("admin_3146");
//		ds.setPortNumber(1521);
//		ds.setServerName("192.168.10.101");
//		ds.setURL("jdbc:oracle:thin:@192.168.10.101:1521:SPIDERX");
//		ds.setUser("admin_3146");
		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setDatabaseName("nawa");
		ds.setPassword("nawadkagh");
		ds.setPortNumber(3306);
		ds.setServerName("192.168.10.101");
		ds.setURL("jdbc:mysql://localhost:3306/nawa");
		ds.setUser("nawa");
		
		JdbcUtils.extractDatabaseMetaData(ds, new DatabaseMetaDataCallback() {
			@Override
			public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
				try{
					ResultSet rs = dbmd.getColumns(null, "nawa", "project_info", null);
//					ResultSet rs = dbmd.getTables(null, "ADMIN_3146", "%", new String[]{ "TABLE" });
					File file = new File("d:\\tmp\\tmp\\tables.txt");
					if(file.exists() == true)
						file.delete();
					file.createNewFile();
					FileWriter output = new FileWriter(file);

					while(rs.next()){
						System.out.print(String.format("%s \t %s \t %s \t %s \t %s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
						output.write(String.format("%s \t %s \t %s \t %s \t %s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
						output.flush();
					} //while
				} catch(Exception e){
					e.printStackTrace();
				} //catch
				
				System.out.println("end");
				return null;
			}
		});
//		JsonJdbcTemplate tmpl = new JsonJdbcTemplate(ds);
//		
//		JSONArray result = tmpl.queryForJsonArray("select 1 from dual");
//		System.out.println(result.toString(4));
	} //test
} //class