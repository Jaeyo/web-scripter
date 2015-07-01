package org.jaeyo.webscripter.controller;

import javax.inject.Inject;

import org.jaeyo.webscripter.service.EmbedDbService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.internal.bind.v2.TODO;

@Controller
public class EmbedDbController {
	private static final Logger logger = LoggerFactory.getLogger(EmbedDbController.class);
	
	@Inject
	private EmbedDbService embedDbService;
	
	@RequestMapping(value = "/View/EmbedDb/", method = RequestMethod.GET)
	public ModelAndView viewScripts(){
		return new ModelAndView("embed-db");
	} //scripts
	
	
	
	@RequestMapping(value = "/EmbedDb/Query/", method = RequestMethod.GET)
	public @ResponseBody String postScript(
			@RequestParam(value = "query", required = true) String query){
		try{
			JSONArray queryResult = embedDbService.runQuery(query);
			return new JSONObject().put("success", 1).put("result", queryResult).toString();
		} catch(Exception e){
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("errmsg", msg).toString();
		} //catch
	} //postScript

} //class