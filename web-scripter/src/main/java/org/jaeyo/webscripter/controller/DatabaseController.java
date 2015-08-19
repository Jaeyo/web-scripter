package org.jaeyo.webscripter.controller;

import javax.inject.Inject;

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

@Controller
public class DatabaseController {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
	
	@Inject
	private DatabaseService databaseService;
	
	@RequestMapping(value = "/Tables/", method = RequestMethod.GET)
	public @ResponseBody String getTables(
			@RequestParam(value="driver", required=true) String driver,
			@RequestParam(value="connUrl", required=true) String connurl,
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="password", required=true) String password){
		try{
			JSONObject jdbcParams = new JSONObject();
			jdbcParams.put("driver", driver);
			jdbcParams.put("connUrl", connurl);
			jdbcParams.put("username", username);
			jdbcParams.put("password", password);
			
			JSONArray tables = databaseService.getTables(jdbcParams);
			return new JSONObject().put("success", 1).put("tables", tables).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getTables
	
	@RequestMapping(value = "/Columns/{tableName}/", method = RequestMethod.GET)
	public @ResponseBody String getColumns(
			@RequestParam(value="driver", required=true) String driver,
			@RequestParam(value="connUrl", required=true) String connurl,
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="password", required=true) String password,
			@PathVariable("tableName") String tableName){
		try{
			JSONObject jdbcParams = new JSONObject();
			jdbcParams.put("driver", driver);
			jdbcParams.put("connUrl", connurl);
			jdbcParams.put("username", username);
			jdbcParams.put("password", password);
			
			JSONArray tables = databaseService.getColumns(jdbcParams, tableName);
			return new JSONObject().put("success", 1).put("columns", tables).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //getColumns
	
	@RequestMapping(value = "/QuerySampleData/", method = RequestMethod.GET)
	public @ResponseBody String querySampleData(
			@RequestParam(value="driver", required=true) String driver,
			@RequestParam(value="connUrl", required=true) String connurl,
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="password", required=true) String password,
			@RequestParam(value="query", required=true) String query,
			@RequestParam(value="rowCount", required=true) int rowCount){
		try{
			JSONObject jdbcParams = new JSONObject();
			jdbcParams.put("driver", driver);
			jdbcParams.put("connUrl", connurl);
			jdbcParams.put("username", username);
			jdbcParams.put("password", password);
			
			JSONArray sampleData = databaseService.querySampleData(jdbcParams, query, rowCount);
			return new JSONObject().put("success", 1).put("sampleData", sampleData).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //querySampleData
} //class