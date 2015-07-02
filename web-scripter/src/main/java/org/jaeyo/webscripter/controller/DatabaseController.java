package org.jaeyo.webscripter.controller;

import javax.inject.Inject;

import org.jaeyo.webscripter.exception.DuplicateException;
import org.jaeyo.webscripter.service.DatabaseService;
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
	
	@RequestMapping(value = "/View/Databases/", method = RequestMethod.GET)
	public ModelAndView viewDatabases(){
		return new ModelAndView("databases");
	} //databases
	
	@RequestMapping(value = "/View/NewDatabase/", method = RequestMethod.GET)
	public ModelAndView viewNewDatabases(){
		return new ModelAndView("new-database");
	} //viewNewDatabases
	
	@RequestMapping(value = "/View/EditDatabase/{sequence}/", method = RequestMethod.GET)
	public ModelAndView viewEditDatabase(@PathVariable("sequence") long sequence){
		ModelAndView mv = new ModelAndView("edit-database");
		mv.addObject("sequence", sequence);
		return mv;
	} //viewEditdatabase
	
	@RequestMapping(value = "/View/Database/Query/{sequence}/", method = RequestMethod.GET)
	public ModelAndView viewDatabaseQuery(@PathVariable("sequence") long sequence){
		ModelAndView mv = new ModelAndView("database-query");
		mv.addObject("sequence", sequence);
		return mv;
	} //viewEditdatabase
	
	
	
	
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
	
	@RequestMapping(value = "/Database/", method = RequestMethod.PUT)
	public @ResponseBody String putDatabase(
			@RequestParam(value = "sequence", required = true) long sequence,
			@RequestParam(value = "dbMappingName", required = true) String dbMappingName,
			@RequestParam(value = "memo", required = true) String memo,
			@RequestParam(value = "jdbcDriver", required = true) String jdbcDriver,
			@RequestParam(value = "jdbcConnUrl", required = true) String jdbcConnUrl,
			@RequestParam(value = "jdbcUsername", required = true) String jdbcUsername,
			@RequestParam(value = "jdbcPassword", required = true) String jdbcPassword){
		try{
			databaseService.update(sequence, dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
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
	
	@RequestMapping(value = "/Database/{sequence}/", method = RequestMethod.GET)
	public @ResponseBody String getDatabase(@PathVariable("sequence") long sequence){
		try{
			JSONObject database = databaseService.loadDatabase(sequence);
			return new JSONObject().put("success", 1).put("database", database).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postDatabase
	
	@RequestMapping(value = "/Database/{sequence}/", method = RequestMethod.DELETE)
	public @ResponseBody String deleteDatabase(@PathVariable("sequence") long sequence){
		try{
			databaseService.removeDatabase(sequence);
			return new JSONObject().put("success", 1).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postDatabase
	
	@RequestMapping(value = "/Database/Query/{sequence}/", method = RequestMethod.GET)
	public @ResponseBody String deleteDatabase(
			@PathVariable("sequence") long sequence,
			@RequestParam(value = "query", required = true) String query){
		try{
			JSONArray queryResult = databaseService.runQuery(sequence, query);
			return new JSONObject().put("success", 1).put("result", queryResult).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postDatabase
} //class