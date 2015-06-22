package org.jaeyo.webscripter.controller;

import javax.inject.Inject;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.websocket.server.PathParam;

import org.jaeyo.webscripter.dao.ScriptDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
import org.jaeyo.webscripter.service.DatabaseService;
import org.jaeyo.webscripter.service.MainService;
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
} //class