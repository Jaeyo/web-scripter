package org.jaeyo.webscripter.controller;

import javax.inject.Inject;

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
	
	@RequestMapping(value = "/View/Scripts/", method = RequestMethod.GET)
	public ModelAndView viewScripts(){
		return new ModelAndView("scripts");
	} //scripts
	
	@RequestMapping(value = "/View/NewScript/", method = RequestMethod.GET)
	public ModelAndView viewNewScript(){
		return new ModelAndView("new-script");
	} //newScript
	
	@RequestMapping(value = "/View/EditScript/*", method = RequestMethod.GET)
	public ModelAndView viewEditScript(){
		return new ModelAndView("edit-script");
	} //scripts
	
	
	
	@RequestMapping(value = "/Script/", method = RequestMethod.POST)
	public @ResponseBody String postScript(
			@RequestParam(value = "scriptName", required = true) String scriptName,
			@RequestParam(value = "script", required = true) String script){
		try{
			scriptService.save(scriptName, script);
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
	
	@RequestMapping(value = "/Script/{sequence}", method = RequestMethod.GET)
	public @ResponseBody String getScript(@PathVariable("sequence") String sequence){
		try{
			JSONObject script = scriptService.loadScript(sequence);
			return new JSONObject().put("success", 1).put("script", script).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getScript
} //class