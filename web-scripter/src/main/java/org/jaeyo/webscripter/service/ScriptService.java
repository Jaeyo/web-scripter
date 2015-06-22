package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.dao.ScriptDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {
	private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);
	@Inject
	private ScriptDAO scriptDAO;
	
	public void save(String scriptName, String script){
		scriptDAO.save(scriptName, script);
	} //save
} //class