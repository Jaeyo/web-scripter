package org.jaeyo.webscripter.service;

import java.util.Set;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.ScriptDAO;
import org.jaeyo.webscripter.exception.AlreadyStartedException;
import org.jaeyo.webscripter.exception.NotFoundException;
import org.jaeyo.webscripter.exception.ScriptNotRunningException;
import org.jaeyo.webscripter.script.ScriptExecutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScriptService {
	private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);
	@Inject
	private ScriptDAO scriptDAO;
	@Inject
	private ScriptExecutor scriptExecutor;
	
	public void save(String scriptName, String script){
		scriptDAO.save(scriptName, script);
	} //save
	
	public void edit(long sequence, String scriptName, String script){
		scriptDAO.edit(sequence, scriptName, script);
	} //edit
	
	public JSONArray loadScripts(){
		JSONArray scripts = scriptDAO.loadScripts();
		Set<Long> runningScriptSequences = scriptExecutor.getRunningScripts();
		for (int i = 0; i < scripts.length(); i++) {
			JSONObject scriptJson = scripts.getJSONObject(i);
			if(runningScriptSequences.contains(scriptJson.getLong("SEQUENCE"))){
				scriptJson.put("IS_RUNNING", true);
			} else{
				scriptJson.put("IS_RUNNING", false);
			} //if
		} //for i
		
		return scripts;
	} //loadScripts
	
	public JSONObject loadScript(long sequence) throws NotFoundException{
		return scriptDAO.loadScript(sequence);
	} //loadScript
	
	public void startScript(long sequence) throws AlreadyStartedException, JSONException, NotFoundException {
		logger.info("sequence: {}", sequence);
		String script = loadScript(sequence).getString("SCRIPT");
		scriptExecutor.execute(sequence, script);
	} //startScript
	
	public void stopScript(long sequence) throws ScriptNotRunningException{
		logger.info("sequence: {}", sequence);
		scriptExecutor.stop(sequence);
	} //stopScript
} //class