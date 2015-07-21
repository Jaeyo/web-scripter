package org.jaeyo.webscripter.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.inject.Inject;

import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.exception.DuplicateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
	@Inject
	private DatabaseDAO databaseDAO;
	
	public JSONArray getTables(JSONObject jdbcParams) throws ClassNotFoundException, JSONException, SQLException{
		Connection conn = null;
		ResultSet rs = null;
		
		try{
			Class.forName(jdbcParams.getString("driver"));
			conn = DriverManager.getConnection(jdbcParams.getString("connurl"), jdbcParams.getString("username"), jdbcParams.getString("password"));
			DatabaseMetaData meta = conn.getMetaData();
			
			rs = meta.getTables(null, jdbcParams.getString("username"), "%", new String[]{ "TABLE" });
			JSONArray result = new JSONArray();
			while(rs.next())
				result.put(rs.getString(3));
			
			if(result.length() != 0)
				return result;
			
			rs.close();
			rs = meta.getTables(null, jdbcParams.getString("username").toLowerCase(), "%", new String[]{ "TABLE" });
			while(rs.next())
				result.put(rs.getString(3));
	
			if(result.length() != 0)
				return result;
			
			rs.close();
			rs = meta.getTables(null, jdbcParams.getString("username").toUpperCase(), "%", new String[]{ "TABLE" });
			while(rs.next())
				result.put(rs.getString(3));
			
			return result;
		} finally{
			if(conn != null)
				conn.close();
			if(rs != null)
				rs.close();
		} //finally
	} //getTables

	public JSONArray getColumns(JSONObject jdbcParams, String tableName) throws ClassNotFoundException, JSONException, SQLException{
		Connection conn = null;
		ResultSet rs = null;
		
		try{
			Class.forName(jdbcParams.getString("driver"));
			conn = DriverManager.getConnection(jdbcParams.getString("connurl"), jdbcParams.getString("username"), jdbcParams.getString("password"));
			DatabaseMetaData meta = conn.getMetaData();
			
			rs = meta.getColumns(null, jdbcParams.getString("username"), tableName, null);
			JSONArray result = new JSONArray();
			while(rs.next()){
				String columnName = rs.getString(4);
				int dataTypeCode = rs.getInt(5);
				String columnType = null;
				switch(dataTypeCode){
				case Types.BIT: columnType = "bit"; break;
				case Types.TINYINT: columnType = "tinyint"; break;
				case Types.SMALLINT: columnType = "smallint"; break;
				case Types.INTEGER: columnType = "integer"; break;
				case Types.BIGINT: columnType = "bigint"; break;
				case Types.FLOAT: columnType = "float"; break;
				case Types.REAL: columnType = "real"; break;
				case Types.DOUBLE: columnType = "double"; break;
				case Types.NUMERIC: columnType = "numeric"; break;
				case Types.DECIMAL: columnType = "decimal"; break;
				case Types.CHAR: columnType = "char"; break;
				case Types.VARCHAR: columnType = "varchar"; break;
				case Types.LONGVARCHAR: columnType = "longvarchar"; break;
				case Types.DATE: columnType = "date"; break;
				case Types.TIME : columnType = "time"; break;
				case Types.TIMESTAMP: columnType = "timestamp"; break;
				case Types.BINARY: columnType = "binary"; break;
				case Types.VARBINARY: columnType = "varbinary"; break;
				case Types.LONGVARBINARY: columnType = "longvarbinary"; break;
				case Types.NULL: columnType = "null"; break;
				case Types.OTHER: columnType = "other"; break;
				case Types.JAVA_OBJECT: columnType = "java_object"; break;
				case Types.DISTINCT: columnType = "distinct"; break;
				case Types.STRUCT: columnType = "struct"; break;
				case Types.ARRAY: columnType = "array"; break;
				case Types.BLOB: columnType = "blob"; break;
				case Types.CLOB: columnType = "clob"; break;
				case Types.REF: columnType = "ref"; break;
				case Types.DATALINK: columnType = "datalink"; break;
				case Types.BOOLEAN: columnType = "boolean"; break;
				case Types.ROWID: columnType = "rowid"; break;
				case Types.NCHAR: columnType = "nchar"; break;
				case Types.NVARCHAR: columnType = "nvarchar"; break;
				case Types.LONGNVARCHAR: columnType = "longnvarchar"; break;
				case Types.NCLOB: columnType = "nclob"; break;
				case Types.SQLXML: columnType = "sqlxml"; break;
				default: columnType = "unknown";
				} //switch
				JSONObject columnInfo = new JSONObject();
				columnInfo.put("columnName", columnName);
				columnInfo.put("columnType", columnType);
				result.put(columnInfo);
			} //while
			
			if(result.length() != 0) TODO IMME 저 기나긴 switch문을 inline method 로 빼고 rs를 총 3번 돌리면서 호출해야 함
				return result;
			
			rs.close();
			rs = meta.getTables(null, jdbcParams.getString("username").toLowerCase(), "%", new String[]{ "TABLE" });
			while(rs.next())
				result.put(rs.getString(3));
	
			if(result.length() != 0)
				return result;
			
			rs.close();
			rs = meta.getTables(null, jdbcParams.getString("username").toUpperCase(), "%", new String[]{ "TABLE" });
			while(rs.next())
				result.put(rs.getString(3));
			
			return result;
		} finally{
			if(conn != null)
				conn.close();
			if(rs != null)
				rs.close();
		} //finally
	} //getColumns


	//------------------------------------------------------------------------------------------------

	public JSONArray loadDatabases(){
		return databaseDAO.selectDatabases();
	} //loadDatabases

	public boolean connectTest(long sequence){
		return false;
		//		TODO IMME
	} //connectTest




	public void save(String dbMappingName, String memo, String jdbcDriver, String jdbcConnUrl, 
			String jdbcUsername, String jdbcPassword) throws DuplicateException{
		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName);
		if(isDbMappingNameExists == true)
			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));

		databaseDAO.save(dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //save

	public void update(long sequence, String dbMappingName, String memo, String jdbcDriver, 
			String jdbcConnUrl, String jdbcUsername, String jdbcPassword) throws DuplicateException {
		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName, sequence);
		if(isDbMappingNameExists == true)
			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
		
		databaseDAO.update(sequence, dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
	} //update
	
	public JSONObject loadDatabase(long sequence){
		return databaseDAO.selectDatabase(sequence);
	} //loadDatabase
	
	public JSONObject loadDatabase(String mappingName){
		return databaseDAO.loadDatabase(mappingName);
	} //loadDatabase
	
	public void removeDatabase(long sequence){
		databaseDAO.removeDatabase(sequence);
	} //remvoeDatabase
	
	public JSONArray runQuery(long sequence, String query) throws ClassNotFoundException, SQLException{
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
		
		JSONObject dbProps = databaseDAO.selectDatabase(sequence);
		String driver = dbProps.getString("DRIVER");
		String username = dbProps.getString("USERNAME");
		String password = dbProps.getString("PASSWORD");
		String connUrl = dbProps.getString("CONNECTION_URL");
		
		if(query.toLowerCase().startsWith("select")){
			return databaseDAO.query(driver, connUrl, username, password, query);
		} else{
			databaseDAO.execute(driver, connUrl, username, password, query);
			return new JSONArray().put(new JSONObject().put("success", 1));
		} //if
	} //runQuery
} //class