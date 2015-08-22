package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.FileWriteStatisticsDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileWriteStatisticsService {
	private static final Logger logger = LoggerFactory.getLogger(FileWriteStatisticsService.class);
	@Inject
	private FileWriteStatisticsDAO fileWriteStatisticsDAO;
	
	public void insertStatistics(String scriptName, long timestamp, long count){
		fileWriteStatisticsDAO.insertStatistics(scriptName, timestamp, count);
	} //insertStatistics
	
	public void deleteUnderTimestamp(long timestamp){
		fileWriteStatisticsDAO.deleteUnderTimestamp(timestamp);
	} //deleteUnderTimestamp
	
	public JSONArray getScriptStatistics(String scriptName){
		JSONArray statistics = fileWriteStatisticsDAO.getScriptStatistics(scriptName);
		JSONArray rebuildedStatistics = new JSONArray();
		
		for (int i = 0; i < statistics.length(); i++) {
			JSONObject row = statistics.getJSONObject(i);
			JSONArray rebuildedRow = new JSONArray();
			rebuildedRow.put(row.get("COUNT_TIMESTAMP"));
			rebuildedRow.put(row.get("COUNT_VALUE"));
			rebuildedStatistics.put(rebuildedRow);
		} //for i
		
		return rebuildedStatistics;
	} //getScriptStatistics
} //class