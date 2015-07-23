package org.jaeyo.webscripter.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.inject.Inject;

import org.jaeyo.webscripter.common.Function;
import org.jaeyo.webscripter.common.JdbcUtil;
import org.jaeyo.webscripter.dao.DatabaseDAO;
import org.jaeyo.webscripter.rdb.JsonJdbcTemplate;
import org.jaeyo.webscripter.rdb.SingleConnectionDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
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
			conn = DriverManager.getConnection(jdbcParams.getString("connUrl"), jdbcParams.getString("username"), jdbcParams.getString("password"));
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
			conn = DriverManager.getConnection(jdbcParams.getString("connUrl"), jdbcParams.getString("username"), jdbcParams.getString("password"));
			DatabaseMetaData meta = conn.getMetaData();
			
			Function handleResultSetFunction = new Function() {
				@Override
				public Object execute(Object... args) {
					try{
						ResultSet rs = (ResultSet) args[0];
						JSONArray result = new JSONArray();
						while(rs.next()){
							JSONObject columnInfo = new JSONObject();
							columnInfo.put("columnName", rs.getString(4));
							columnInfo.put("columnType", JdbcUtil.convertDataTypeCode2String(rs.getInt(5)));
							result.put(columnInfo);
						} //while
						return result;
					} catch(Exception e){
						logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
						return null;
					} //catch
				} //execute
			};
			
			JSONArray result = null;
			
			rs = meta.getColumns(null, jdbcParams.getString("username"), tableName, null);
			result = (JSONArray) handleResultSetFunction.execute(rs);
			
			if(result.length() != 0) 
				return result;
			
			rs.close();
			rs = meta.getColumns(null, jdbcParams.getString("username").toLowerCase(), tableName.toLowerCase(), null);
			result = (JSONArray) handleResultSetFunction.execute(rs);
	
			if(result.length() != 0)
				return result;
			
			rs.close();
			rs = meta.getColumns(null, jdbcParams.getString("username").toUpperCase(), tableName.toUpperCase(), null);
			result = (JSONArray) handleResultSetFunction.execute(rs);
			return result;
		} finally{
			if(conn != null)
				conn.close();
			if(rs != null)
				rs.close();
		} //finally
	} //getColumns
	
	public JSONArray querySampleData(JSONObject jdbcParams, String query, final int rowCount) throws JSONException, SQLException, ClassNotFoundException{
		Class.forName(jdbcParams.getString("driver"));
		Connection conn = DriverManager.getConnection(jdbcParams.getString("connUrl"), jdbcParams.getString("username"), jdbcParams.getString("password"));
		try{
			JsonJdbcTemplate jdbcTmpl = new JsonJdbcTemplate(new SingleConnectionDataSource(conn));
			return jdbcTmpl.query(query, new ResultSetExtractor<JSONArray>() {
				@Override
				public JSONArray extractData(ResultSet rs) throws SQLException, DataAccessException {
					int counter = 0;
					JSONArray rows = new JSONArray();
					ResultSetMetaData meta = rs.getMetaData();
					int colCount = meta.getColumnCount();
					while(rs.next()){
						counter++;
						if(counter > rowCount)
							break;
	
						JSONObject row = new JSONObject();
						for (int i = 1; i <= colCount; i++)
							row.put(meta.getColumnLabel(i), rs.getObject(i));
						rows.put(row);
					} //while
					return rows;
				} //extractData
			});
		} finally{
			if(conn != null)
				conn.close();
		} //finally
	} //querySampleData 

	//------------------------------------------------------------------------------------------------

//	public JSONArray loadDatabases(){
//		return databaseDAO.selectDatabases();
//	} //loadDatabases
//
//	public boolean connectTest(long sequence){
//		return false;
//		//		TODO IMME
//	} //connectTest
//
//
//
//
//	public void save(String dbMappingName, String memo, String jdbcDriver, String jdbcConnUrl, 
//			String jdbcUsername, String jdbcPassword) throws DuplicateException{
//		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName);
//		if(isDbMappingNameExists == true)
//			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
//
//		databaseDAO.save(dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
//	} //save
//
//	public void update(long sequence, String dbMappingName, String memo, String jdbcDriver, 
//			String jdbcConnUrl, String jdbcUsername, String jdbcPassword) throws DuplicateException {
//		boolean isDbMappingNameExists = databaseDAO.isMappingNameExists(dbMappingName, sequence);
//		if(isDbMappingNameExists == true)
//			throw new DuplicateException(String.format("dbMappingName \"%s\" is duplicated", dbMappingName));
//		
//		databaseDAO.update(sequence, dbMappingName, memo, jdbcDriver, jdbcConnUrl, jdbcUsername, jdbcPassword);
//	} //update
//	
//	public JSONObject loadDatabase(long sequence){
//		return databaseDAO.selectDatabase(sequence);
//	} //loadDatabase
//	
//	public JSONObject loadDatabase(String mappingName){
//		return databaseDAO.loadDatabase(mappingName);
//	} //loadDatabase
//	
//	public void removeDatabase(long sequence){
//		databaseDAO.removeDatabase(sequence);
//	} //remvoeDatabase
//	
//	public JSONArray runQuery(long sequence, String query) throws ClassNotFoundException, SQLException{
//		logger.info("query: {}", query);
//		
//		String[] spliteds = query.split("\n");
//		StringBuilder rebuildedQuery = new StringBuilder();
//		for(String splited : spliteds){
//			splited = splited.trim();
//			if(splited.startsWith("--"))
//				continue;
//			rebuildedQuery.append(splited).append("\n");
//		} //for splited
//		
//		query = rebuildedQuery.toString().trim();
//		
//		while(query.endsWith(";"))
//			query = query.substring(0, query.length()-1);
//		
//		JSONObject dbProps = databaseDAO.selectDatabase(sequence);
//		String driver = dbProps.getString("DRIVER");
//		String username = dbProps.getString("USERNAME");
//		String password = dbProps.getString("PASSWORD");
//		String connUrl = dbProps.getString("CONNECTION_URL");
//		
//		if(query.toLowerCase().startsWith("select")){
//			return databaseDAO.query(driver, connUrl, username, password, query);
//		} else{
//			databaseDAO.execute(driver, connUrl, username, password, query);
//			return new JSONArray().put(new JSONObject().put("success", 1));
//		} //if
//	} //runQuery
} //class