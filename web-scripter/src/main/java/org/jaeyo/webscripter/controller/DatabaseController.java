package org.jaeyo.webscripter.controller;

import javax.inject.Inject;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.websocket.server.PathParam;

import org.jaeyo.webscripter.exception.DuplicateException;
import org.jaeyo.webscripter.service.DatabaseService;
import org.jaeyo.webscripter.service.MainService;
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
public class DatabaseController {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
	
	@Inject
	private DatabaseService databaseService;
	
	@RequestMapping(value = "/Database/", method = RequestMethod.POST)
	public @ResponseBody String postDatabase(
			@RequestParam(value = "dbMappingName", required = true) String dbMappingName,
			@RequestParam(value = "memo", required = true) String memo,
			@RequestParam(value = "jdbcDriver", required = true) String jdbcDriver,
			@RequestParam(value = "jdbcConnUrl", required = true) String jdbcConnUrl,
			@RequestParam(value = "jdbcUsername", required = true) String jdbcUsername,
			@RequestParam(value = "jdbcPassword", required = true) String jdbcPassword){
		try{
			databaseService.save(dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
			return new JSONObject().put("success", 1).toString();
		} catch(DuplicateException e){
			String msg = e.getMessage();
			logger.warn(msg);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postDatabase
	
	@RequestMapping(value = "/Databases/", method = RequestMethod.GET)
	public @ResponseBody String getDatabases(){
		try{
			JSONArray databases = databaseService.loadDatabases();
			return new JSONObject().put("success", 1).put("databases", databases).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getDatabases
	
	@RequestMapping(value = "/Database/{mappingName}/", method = RequestMethod.GET)
	public @ResponseBody String getDatabase(@PathVariable("mappingName") String mappingName){
		try{
			JSONObject database = databaseService.loadDatabase(mappingName);
			return new JSONObject().put("success", 1).put("database", database).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postDatabase
} //class