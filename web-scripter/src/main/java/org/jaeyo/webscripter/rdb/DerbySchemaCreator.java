package org.jaeyo.webscripter.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

public class DerbySchemaCreator {
	
	public void check(){
		JdbcTemplate jdbcTmpl = new DerbyDataSource().getJdbcTmpl();
		
		checkSequence(jdbcTmpl);
		checkTables(jdbcTmpl);
	} //check
	
	private void checkSequence(JdbcTemplate jdbcTmpl){
		final Set<String> existingSequences = new HashSet<String>();
		
		jdbcTmpl.query("select sequencename from sys.syssequences", new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				existingSequences.add(rs.getString("sequencename"));
			} //processRow
		});
		
		if(existingSequences.contains("MAIN_SEQ") == false)
			jdbcTmpl.execute("create sequence main_seq");
	} //checkSequence
	
	private void checkTables(JdbcTemplate jdbcTmpl){
		final Set<String> existingTableNames = new HashSet<String>();

		jdbcTmpl.query("select tablename from sys.systables where tabletype='T'", new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				existingTableNames.add(rs.getString("tablename"));
			} //processRow
		});	
		
		if(existingTableNames.contains("DATABASE") == false)
			jdbcTmpl.execute("create table database ( "
					+ "sequence integer not null primary key, "
					+ "mapping_name varchar(100) not null unique, "
					+ "memo varchar(300) , "
					+ "driver varchar(100) not null, "
					+ "connection_url varchar(100) not null, "
					+ "username varchar(100) not null, "
					+ "password varchar(100) not null, "
					+ "regdate timestamp not null )");
		
		if(existingTableNames.contains("SCRIPT") == false)
			jdbcTmpl.execute("create table script ( "
					+ "sequence integer not null primary key, "
					+ "script_name varchar(100) not null unique, "
					+ "script long varchar, "
					+ "regdate timestamp not null )");
		
		if(existingTableNames.contains("FILEWRITE_STATISTICS") == false)
			jdbcTmpl.execute("create table filewrite_statistics ( "
					+ "script_sequence integer not null, "
					+ "count_timestamp timestamp not null, "
					+ "count_value integer not null )");
	} //checkTables
} // class