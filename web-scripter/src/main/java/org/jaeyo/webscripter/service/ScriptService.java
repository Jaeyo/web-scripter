package org.jaeyo.webscripter.service;

import java.util.Set;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.FileWriteStatisticsDAO;
import org.jaeyo.webscripter.dao.ScriptDAO;
import org.jaeyo.webscripter.script.ScriptExecutor;
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
	@Inject
	private ScriptExecutor scriptExecutor;
	@Inject
	private FileWriteStatisticsDAO fileWriteStatisticsDAO;
	
	public JSONArray getScriptInfo(){
		JSONArray scripts = scriptDAO.selectScriptInfo();
		Set<String> runningScriptNames = scriptExecutor.getRunningScripts();
		for (int i = 0; i < scripts.length(); i++) {
			JSONObject scriptJson = scripts.getJSONObject(i);
			if(runningScriptNames.contains(scriptJson.getString("SCRIPT_NAME"))){
				scriptJson.put("IS_RUNNING", true);
			} else{
				scriptJson.put("IS_RUNNING", false);
			} //if
		} //for i
		
		return scripts;
	} //loadScripts
	
	//---------------------------------------------------------------------------------------
	
//	public void save(String scriptName, String script, String memo){
//		scriptDAO.save(scriptName, script, memo);
//	} //save
//	
//	public void edit(long sequence, String scriptName, String script, String memo){
//		scriptDAO.edit(sequence, scriptName, script, memo);
//	} //edit
//	
//	public JSONObject loadScript(long sequence) throws NotFoundException{
//		return scriptDAO.loadScript(sequence);
//	} //loadScript
//	
//	public void startScript(long sequence) throws AlreadyStartedException, JSONException, NotFoundException {
//		logger.info("sequence: {}", sequence);
//		String script = loadScript(sequence).getString("SCRIPT");
//		scriptExecutor.execute(sequence, script);
//	} //startScript
//	
//	public void stopScript(long sequence) throws ScriptNotRunningException{
//		logger.info("sequence: {}", sequence);
//		scriptExecutor.stop(sequence);
//	} //stopScript
//	
//	public void removeScript(long sequence){
//		logger.info("sequence: {}", sequence);
//		scriptDAO.removeScript(sequence);
//	} //removeScript
//	
//	public JSONArray loadDoc() throws IOException{
//		InputStream docInput = this.getClass().getClassLoader().getResourceAsStream("spdbreader-doc.json");
//		String doc = IOUtils.toString(docInput, "utf8");
//		return new JSONArray(doc);
//	} //loadDoc
} //class