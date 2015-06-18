package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
	@Inject
	private DatabaseDAO databaseDAO;
	
	public void save(String dbMappingName, String jdbcDriver, String jdbcConnUrl, String jdbcUsername, String jdbcPassword){
		databaseDAO.save(dbMappingName, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //save
	
	public JSONArray loadDatabases(){
		return databaseDAO.loadDatabases();
	} //loadDatabases
} //class