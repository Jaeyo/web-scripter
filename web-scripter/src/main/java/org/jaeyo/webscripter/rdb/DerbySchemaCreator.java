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
		
		if(existingTableNames.contains("TASK") == false)
			jdbcTmpl.execute("create table task ( "
					+ "sequence integer not null, "
					+ "title varchar(1024) not null, "
					+ "label varchar(20) not null, "
					+ "regdate timestamp not null )");
		
		if(existingTableNames.contains("TASK_HISTORY") == false)
			jdbcTmpl.execute("create table TASK_HISTORY ( "
					+ "sequence integer not null, "
					+ "task_sequence integer not null, "
					+ "content varchar(2048) not null, "
					+ "regdate timestamp not null) ");
	} //checkTables
} // class