package org.jaeyo.webscripter.dao;

import java.util.Date;

import javax.inject.Inject;

import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public boolean isMappingNameExists(String dbMappingName){
		logger.info("dbMappingName: {}", dbMappingName);
		return 1 == ds.getJdbcTmpl().queryForObject("select count(*) from database where mapping_name = ?", new String[]{ dbMappingName }, Integer.class);
	} //isMappingNameExists
	
	public JSONArray loadDatabases(){
		logger.info("");
		return ds.getJdbcTmpl().queryForJsonArray("select sequence, mapping_name, memo, driver, connection_url, username, password, regdate from database");
	} //loadDatabases
} //class