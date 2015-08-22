package org.jaeyo.webscripter.dao;

import java.util.Date;

import javax.inject.Inject;

import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileWriteStatisticsDAO {
	private static final Logger logger = LoggerFactory.getLogger(FileWriteStatisticsDAO.class);
	@Inject
	private DerbyDataSource ds;
	
	public void insertStatistics(String scriptName, long timestamp, long count){
		ds.getJdbcTmpl().update("insert into filewrite_statistics (script_name, count_timestamp, count_value) values(?,?,?)", 
				scriptName, new Date(timestamp), count);
	} //insertStatistics
	
	public void deleteUnderTimestamp(long timestamp){
		logger.info("timestamp: {}", timestamp);
		ds.getJdbcTmpl().update("delete from filewrite_statistics where count_timestamp < ?", new Date(timestamp));
	} //deleteUnderTimestamp
	
	public JSONArray getScriptStatistics(String scriptName){
		return ds.getJdbcTmpl().queryForJsonArray("select count_timestamp, count_value from filewrite_statistics "
				+ "where script_name = ? "
				+ "order by count_timestamp", scriptName);
	} //getScriptStatistics
}  //class