package org.jaeyo.webscripter.controller;

import javax.inject.Inject;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import org.jaeyo.webscripter.service.MainService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Inject
	private MainService mainService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView main(){
		return scripts();
	} //main
	
	@RequestMapping(value = "/ViewScripts/", method = RequestMethod.GET)
	public ModelAndView scripts(){
		return new ModelAndView("scripts");
	} //scripts
	
	@RequestMapping(value = "/ViewNewScript/", method = RequestMethod.GET)
	public ModelAndView newScript(){
		return new ModelAndView("new-script");
	} //newScript
	
	@RequestMapping(value = "/ViewDatabases/", method = RequestMethod.GET)
	public ModelAndView databases(){
		return new ModelAndView("databases");
	} //databases
	
	@RequestMapping(value = "/ViewNewDatabase/", method = RequestMethod.GET)
	public ModelAndView newDatabase(){
		return new ModelAndView("new-database");
	} //databases
	
	@RequestMapping(value = "/ViewConfiguration/", method = RequestMethod.GET)
	public ModelAndView configuration(){
		return new ModelAndView("configuration");
	} //configuration
	
	
	
	
	@RequestMapping(value = "/Script/Run/", method = RequestMethod.POST)
	public @ResponseBody String runScript(
			@RequestParam(value = "script", required = true) String script){
		try{
			Bindings bindings = new SimpleBindings();
			bindings.put("logger", logger);
			ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
			scriptEngine.eval(script, bindings);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //addNewTask
} //class