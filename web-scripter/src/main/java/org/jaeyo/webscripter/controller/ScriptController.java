package org.jaeyo.webscripter.controller;

import javax.inject.Inject;

import org.jaeyo.webscripter.service.FileWriteStatisticsService;
import org.jaeyo.webscripter.service.ScriptService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ScriptController {
	private static final Logger logger = LoggerFactory.getLogger(ScriptController.class);
	
	@Inject
	private ScriptService scriptService;
	@Inject
	private FileWriteStatisticsService fileWriteStatisticsService;
	
	@RequestMapping(value = "/View/Scripts/", method = RequestMethod.GET)
	public ModelAndView viewScripts(){
		return new ModelAndView("scripts");
	} //scripts
	
	@RequestMapping(value = "/View/NewScript/", method = RequestMethod.GET)
	public ModelAndView viewNewScript(){
		return new ModelAndView("new-script");
	} //newScript
	
	@RequestMapping(value = "/View/NewScriptOld/", method = RequestMethod.GET)
	public ModelAndView viewNewScriptOld(){
		return new ModelAndView("new-script-OLD");
	} //newScript
	
	@RequestMapping(value = "/View/EditScript/{sequence}/", method = RequestMethod.GET)
	public ModelAndView viewEditScript(@PathVariable("sequence") long sequence){
		ModelAndView mv = new ModelAndView("edit-script");
		mv.addObject("sequence", sequence);
		return mv;
	} //scripts
	
	@RequestMapping(value = "/View/Statistics/{sequence}/", method = RequestMethod.GET)
	public ModelAndView viewStatistics(@PathVariable("sequence") long sequence){
		ModelAndView mv = new ModelAndView("script-statistics");
		mv.addObject("sequence", sequence);
		return mv;
	} //scripts
	
	
	
	
	@RequestMapping(value = "/Script/", method = RequestMethod.POST)
	public @ResponseBody String postScript(
			@RequestParam(value = "scriptName", required = true) String scriptName,
			@RequestParam(value = "script", required = true) String script,
			@RequestParam(value = "memo", required = false) String memo){
		try{
			scriptService.save(scriptName, script, memo);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postScript
	
	@RequestMapping(value = "/Script/{sequence}/", method = RequestMethod.PUT)
	public @ResponseBody String putScript(
			@PathVariable("sequence") long sequence,
			@RequestParam(value = "scriptName", required = true) String scriptName,
			@RequestParam(value = "script", required = true) String script,
			@RequestParam(value = "memo", required = false) String memo){
		try{
			scriptService.edit(sequence, scriptName, script, memo);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postScript
	
	@RequestMapping(value = "/Scripts/", method = RequestMethod.GET)
	public @ResponseBody String getScripts(){
		try{
			JSONArray scripts = scriptService.loadScripts();
			return new JSONObject().put("success", 1).put("scripts", scripts).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getScripts
	
	@RequestMapping(value = "/Script/{sequence}/", method = RequestMethod.GET)
	public @ResponseBody String getScript(@PathVariable("sequence") long sequence){
		try{
			JSONObject script = scriptService.loadScript(sequence);
			return new JSONObject().put("success", 1).put("script", script).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getScript
	
	@RequestMapping(value = "/Script/Start/{sequence}/", method = RequestMethod.PUT)
	public @ResponseBody String startScript(@PathVariable("sequence") long sequence){
		try{
			scriptService.startScript(sequence);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //startScript
	
	@RequestMapping(value = "/Script/Stop/{sequence}/", method = RequestMethod.PUT)
	public @ResponseBody String stopScript(@PathVariable("sequence") long sequence){
		try{
			scriptService.stopScript(sequence);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //stopScript
	
	@RequestMapping(value = "/Script/{sequence}/", method = RequestMethod.DELETE)
	public @ResponseBody String removeScript(@PathVariable("sequence") long sequence){
		try{
			scriptService.removeScript(sequence);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //removeScript
	
	@RequestMapping(value = "/Script/Statistics/{sequence}/", method = RequestMethod.GET)
	public @ResponseBody String getScriptStatistics(@PathVariable("sequence") long sequence){
		try{
			JSONArray statistics = fileWriteStatisticsService.getScriptStatistics(sequence);
			JSONObject script = scriptService.loadScript(sequence);
			return new JSONObject().put("success", 1).put("name", script.getString("SCRIPT_NAME")).put("statistics", statistics).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getScriptStatistics
	
	@RequestMapping(value = "/Script/Doc/", method = RequestMethod.GET)
	public @ResponseBody String getScriptDoc(){
		try{
			JSONArray scriptDoc = scriptService.loadDoc();
			return new JSONObject().put("success", 1).put("scriptDoc", scriptDoc).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getScriptDoc
} //class