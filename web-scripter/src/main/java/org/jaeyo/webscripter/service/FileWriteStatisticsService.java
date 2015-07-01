package org.jaeyo.webscripter.service;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.dao.EmbedDbDAO;
import org.jaeyo.webscripter.dao.FileWriteStatisticsDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
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
	
	public void insertStatistics(long scriptSequence, long timestamp, long count){
		fileWriteStatisticsDAO.insertStatistics(scriptSequence, timestamp, count);
	} //insertStatistics
	
	public void deleteUnderTimestamp(long timestamp){
		fileWriteStatisticsDAO.deleteUnderTimestamp(timestamp);
	} //deleteUnderTimestamp
	
	public JSONArray getScriptStatistics(long sequence){
		logger.info("sequence: {}", sequence);
		JSONArray statistics = fileWriteStatisticsDAO.getScriptStatistics(sequence);
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