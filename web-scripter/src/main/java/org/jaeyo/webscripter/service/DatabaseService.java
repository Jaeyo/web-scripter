package org.jaeyo.webscripter.service;

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
	
	public void save(String dbMappingName, String memo, String jdbcDriver, String jdbcConnUrl, String jdbcUsername, String jdbcPassword) throws DuplicateException{
		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName);
		if(isDbMappingNameExists == true)
			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
		
		databaseDAO.save(dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //save
	
	public JSONArray loadDatabases(){
		return databaseDAO.loadDatabases();
	} //loadDatabases
	
	public JSONObject loadDatabase(String mappingName){
		return databaseDAO.loadDatabase(mappingName);
	} //loadDatabase
} //class