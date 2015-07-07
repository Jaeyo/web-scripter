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
	
	public void save(String scriptName, String script, String memo){
		logger.info("scriptName: {}", scriptName);
		ds.getJdbcTmpl().update("insert into script (sequence, script_name, script, memo, regdate) "
				+ "values(next value for main_seq, ?, ?, ?, ?)", scriptName, script, memo, new Date());
	} //save
	
	public void edit(long sequence, String scriptName, String script, String memo){
		logger.info("sequence: {}, scriptName: {}", sequence, scriptName);
		ds.getJdbcTmpl().update("update script set script_name = ?, script = ?, regdate = ?, memo = ? "
				+ "where sequence = ?",
				scriptName, script, new Date(), memo, sequence);
	} //edit
	
	public JSONArray loadScripts(){
		logger.info("");
		return ds.getJdbcTmpl().queryForJsonArray("select sequence, script_name, script, regdate, memo from script");
	} //loadScripts
	
	public JSONObject loadScript(long sequence) throws NotFoundException{
		logger.info("sequence: {}", sequence);
		JSONArray result = ds.getJdbcTmpl().queryForJsonArray("select sequence, script_name, script, regdate, memo from script where sequence = ?", sequence);
		
		if(result == null || result.length() == 0)
			throw new NotFoundException("script not found : " + sequence);
		
		return result.getJSONObject(0);
	} //loadScript
	
	public void removeScript(long sequence){
		logger.info("sequence: {}", sequence);
		ds.getJdbcTmpl().update("delete from script where sequence = ?", sequence);
	} //removeScript
} //class