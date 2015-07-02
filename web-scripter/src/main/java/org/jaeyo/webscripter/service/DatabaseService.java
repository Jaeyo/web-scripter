package org.jaeyo.webscripter.service;

import java.sql.SQLException;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
	@Inject
	private DatabaseDAO databaseDAO;
	
	public void save(String dbMappingName, String memo, String jdbcDriver, String jdbcConnUrl, 
			String jdbcUsername, String jdbcPassword) throws DuplicateException{
		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName);
		if(isDbMappingNameExists == true)
			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
		
		databaseDAO.save(dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //save
	
	public void update(long sequence, String dbMappingName, String memo, String jdbcDriver, 
			String jdbcConnUrl, String jdbcUsername, String jdbcPassword) throws DuplicateException {
		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName, sequence);
		if(isDbMappingNameExists == true)
			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
		
		databaseDAO.update(sequence, dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //update
	
	public JSONArray loadDatabases(){
		return databaseDAO.loadDatabases();
	} //loadDatabases
	
	public JSONObject loadDatabase(long sequence){
		return databaseDAO.loadDatabase(sequence);
	} //loadDatabase
	
	public JSONObject loadDatabase(String mappingName){
		return databaseDAO.loadDatabase(mappingName);
	} //loadDatabase
	
	public void removeDatabase(long sequence){
		databaseDAO.removeDatabase(sequence);
	} //remvoeDatabase
	
	public JSONArray runQuery(long sequence, String query) throws ClassNotFoundException, SQLException{
		logger.info("query: {}", query);
		
		String[] spliteds = query.split("\n");
		StringBuilder rebuildedQuery = new StringBuilder();
		for(String splited : spliteds){
			splited = splited.trim();
			if(splited.startsWith("--"))
				continue;
			rebuildedQuery.append(splited).append("\n");
		} //for splited
		
		query = rebuildedQuery.toString().trim();
		
		while(query.endsWith(";"))
			query = query.substring(0, query.length()-1);
		
		JSONObject dbProps = databaseDAO.loadDatabase(sequence);
		String driver = dbProps.getString("DRIVER");
		String username = dbProps.getString("USERNAME");
		String password = dbProps.getString("PASSWORD");
		String connUrl = dbProps.getString("CONNECTION_URL");
		
		if(query.toLowerCase().startsWith("select")){
			return databaseDAO.query(driver, connUrl, username, password, query);
		} else{
			databaseDAO.execute(driver, connUrl, username, password, query);
			return new JSONArray().put(new JSONObject().put("success", 1));
		} //if
	} //runQuery
} //class