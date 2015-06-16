package org.jaeyo.webscripter.rdb;

import java.io.File;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.jaeyo.webscripter.common.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DerbyDataSource {
	private static Logger logger = LoggerFactory.getLogger(DerbyDataSource.class);
	private JsonJdbcTemplate jdbcTmpl = null;
	
	public DerbyDataSource(){
		String derbyPath = Conf.get(Conf.DERBY_PATH);
		if(derbyPath == null){
			derbyPath = new File(System.getProperty("java.io.tmpdir"), "derby").getAbsolutePath();
			logger.warn("derby.path is null, set to {}", derbyPath);
		} //if
		
		EmbeddedConnectionPoolDataSource ds = new EmbeddedConnectionPoolDataSource();
		ds.setDatabaseName(derbyPath);
		ds.setCreateDatabase("create");
		ds.setUser("");
		ds.setPassword("");
		this.jdbcTmpl = new JsonJdbcTemplate(ds);
	} //INIT
	
	public JsonJdbcTemplate getJdbcTmpl(){
		return this.jdbcTmpl;
	} //getJdbcTmpl
} //class