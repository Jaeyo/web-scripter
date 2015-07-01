package org.jaeyo.webscripter.dao;

import javax.inject.Inject;

import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmbedDbDAO {
	private static final Logger logger = LoggerFactory.getLogger(EmbedDbDAO.class);
	@Inject
	private DerbyDataSource ds;
	
	public JSONArray query(String query){
		logger.info("query: {}", query);
		return ds.getJdbcTmpl().queryForJsonArray(query);
	} //runQuery
	
	public void execute(String query){
		logger.info("query: {}", query);
		ds.getJdbcTmpl().execute(query);
	} //execute
}  //class