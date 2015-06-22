package org.jaeyo.webscripter.dao;

import java.util.Date;

import javax.inject.Inject;

import org.jaeyo.webscripter.rdb.DerbyDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScriptDAO {
	private static final Logger logger = LoggerFactory.getLogger(ScriptDAO.class);
	@Inject
	private DerbyDataSource ds;
	
	public void save(String scriptName, String script){
		logger.info("scriptName: {}", scriptName);
		ds.getJdbcTmpl().update("insert into script (sequence, script_name, script, regdate) "
				+ "values(next value for main_seq, ?, ?, ?)", scriptName, script, new Date());
	} //save
} //class