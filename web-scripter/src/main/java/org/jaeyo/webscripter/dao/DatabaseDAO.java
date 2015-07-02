package org.jaeyo.webscripter.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.jaeyo.webscripter.rdb.JsonJdbcTemplate;
import org.jaeyo.webscripter.rdb.SingleConnectionDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseDAO {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseDAO.class);
	@Inject
	private DerbyDataSource ds;
	
	public void save(String dbMappingName, String memo, String jdbcDriver, String jdbcConnUrl, String jdbcUsername, String jdbcPassword){
		logger.info("dbMappingName: {}, jdbcDriver: {}, jdbcConnUrl: {}, jdbcUsername: {}, jdbcPassword: {}", dbMappingName, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
		ds.getJdbcTmpl().update("insert into database (sequence, mapping_name, memo, driver, connection_url, username, password, regdate) "
				+ "values(next value for main_seq, ?, ?, ?, ?, ?, ?, ?)", dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword, new Date());
	} //save
	
	public void update(long sequence, String dbMappingName, String memo, String jdbcDriver, 
			String jdbcConnUrl, String jdbcUsername, String jdbcPassword){
		logger.info("sequence: {}, dbMappingName: {}, jdbcDriver: {}, jdbcConnUrl: {}, jdbcUsername: {}, jdbcPassword: {}", 
				sequence, dbMappingName, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
		ds.getJdbcTmpl().update("update database set mapping_name = ?, memo = ?, driver = ?, connection_url = ?, "
				+ "username = ?, password = ?, regdate = ? where sequence = ? ", 
				dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword, new Date(), sequence);
	} //update
	
	public boolean isMappingNameExists(String dbMappingName){
		logger.info("dbMappingName: {}", dbMappingName);
		return 1 == ds.getJdbcTmpl().queryForObject("select count(*) from database where mapping_name = ?", new String[]{ dbMappingName }, Integer.class);
	} //isMappingNameExists
	
	public boolean isMappingNameExists(String dbMappingName, long excludeSequence){
		logger.info("dbMappingName: {}, excludeSequence: {}", dbMappingName, excludeSequence);
		return 1 == ds.getJdbcTmpl().queryForObject("select count(*) from database where mapping_name = ? and sequence <> ?", 
				new String[]{ dbMappingName, excludeSequence+"" }, Integer.class);
	} //isMappingNameExists
	
	public JSONArray loadDatabases(){
		logger.info("");
		return ds.getJdbcTmpl().queryForJsonArray("select sequence, mapping_name, memo, driver, connection_url, username, password, regdate from database");
	} //loadDatabases
	
	public JSONObject loadDatabase(long sequence){
		logger.info("sequence: {}", sequence);
		JSONArray rows = ds.getJdbcTmpl().queryForJsonArray("select sequence, mapping_name, memo, driver, connection_url, username, password, regdate "
				+ "from database "
				+ "where sequence = ?", sequence);
		return rows.getJSONObject(0);
	} //loadDatabase
	
	public JSONObject loadDatabase(String mappingName){
		logger.info("mappingName: {}", mappingName);
		JSONArray rows = ds.getJdbcTmpl().queryForJsonArray("select sequence, mapping_name, memo, driver, connection_url, username, password, regdate "
				+ "from database "
				+ "where mapping_name = ?", mappingName);
		return rows.getJSONObject(0);
	} //loadDatabase
	
	public void removeDatabase(long sequence){
		logger.info("sequence: {}", sequence);
		ds.getJdbcTmpl().update("delete from database where sequence = ?", sequence);
	} //removeDatabase
	
	public JSONArray query(String driver, String connUrl, String username, String password, String query) throws SQLException, ClassNotFoundException{
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connUrl, username, password);
		JsonJdbcTemplate jdbcTmpl = new JsonJdbcTemplate(new SingleConnectionDataSource(conn));
		return jdbcTmpl.queryForJsonArray(query);
	} //query
	
	public void execute(String driver, String connUrl, String username, String password, String query) throws SQLException, ClassNotFoundException{
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connUrl, username, password);
		JsonJdbcTemplate jdbcTmpl = new JsonJdbcTemplate(new SingleConnectionDataSource(conn));
		jdbcTmpl.execute(query);
	} //execute
} //class