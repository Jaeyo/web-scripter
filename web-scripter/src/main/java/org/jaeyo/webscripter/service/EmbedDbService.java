package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.dao.EmbedDbDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmbedDbService {
	private static final Logger logger = LoggerFactory.getLogger(EmbedDbService.class);
	@Inject
	private EmbedDbDAO embedDbDAO;
	
	public JSONArray runQuery(String query){
		while(query.endsWith(";"))
			query = query.substring(0, query.length()-1);
		return embedDbDAO.runQuery(query);
	} //runQuery
} //class