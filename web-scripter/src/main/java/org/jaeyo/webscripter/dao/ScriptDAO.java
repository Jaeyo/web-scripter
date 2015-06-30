package org.jaeyo.webscripter.dao;

import java.util.Date;

import javax.inject.Inject;

import org.jaeyo.webscripter.exception.NotFoundException;
import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScriptDAO {
	private static final Logger logger = LoggerFactory.getLogger(ScriptDAO.class);
	@Inject
	private DerbyDataSource ds;
	
	public void save(String scriptName, String script){
		logger.info("scriptName: {}", scriptName);
		ds.getJdbcTmpl().update("insert into script (sequence, script_name, script, regdate) "
				+ "values(next value for main_seq, ?, ?, ?)", scriptName, script, new Date());
	} //save
	
	public void edit(long sequence, String scriptName, String script){
		logger.info("sequence: {}, scriptName: {}", sequence, scriptName);
		ds.getJdbcTmpl().update("update script set script_name = ?, script = ?, regdate = ? "
				+ "where sequence = ?",
				scriptName, script, new Date(), sequence);
	} //edit
	
	public JSONArray loadScripts(){
		logger.info("");
		return ds.getJdbcTmpl().queryForJsonArray("select sequence, script_name, script, regdate from script");
	} //loadScripts
	
	public JSONObject loadScript(long sequence) throws NotFoundException{
		logger.info("sequence: {}", sequence);
		JSONArray result = ds.getJdbcTmpl().queryForJsonArray("select sequence, script_name, script, regdate from script where sequence = ?", sequence);
		
		if(result == null || result.length() == 0)
			throw new NotFoundException("script not found : " + sequence);
		
		return result.getJSONObject(0);
	} //loadScript
} //class