package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.EmbedDbDAO;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmbedDbService {
	private static final Logger logger = LoggerFactory.getLogger(EmbedDbService.class);
	@Inject
	private EmbedDbDAO embedDbDAO;
	
	public JSONArray runQuery(String query){
		logger.info("query: {}", query);
		
		String[] spliteds = query.split("\n");
		StringBuilder rebuildedQuery = new StringBuilder();
		for(String splited : spliteds){
			splited = splited.trim();
			if(splited.startsWith("--"))
				continue;
			rebuildedQuery.append(splited).append("\n");
		} //for splited
		
		query = rebuildedQuery.toString().trim();
		
		while(query.endsWith(";"))
			query = query.substring(0, query.length()-1);
		return embedDbDAO.runQuery(query);
	} //runQuery
} //class