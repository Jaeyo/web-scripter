package org.jaeyo.webscripter.script.bindings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaeyo.webscripter.common.SpringBeans;
import org.jaeyo.webscripter.outputfiledelete.OutputFileLastModified;
import org.jaeyo.webscripter.service.DatabaseService;
import org.jaeyo.webscripter.statistics.FileWriteStatistics;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbHandler {
	private static final Logger logger = LoggerFactory.getLogger(DbHandler.class);
	private DatabaseService databaseService = SpringBeans.getBean(DatabaseService.class);
	private FileWriteStatistics fileWriteStatistics = SpringBeans.getBean(FileWriteStatistics.class);

	public void executeQuery(String dbName, String query){
		logger.info("execute query : {}", query);
		
		JSONObject dbProps = databaseService.loadDatabase(dbName);
		if (dbProps == null) {
			logger.error("dbName {} not exists", dbName);
			System.exit(-1);
		} // if
		
		Connection conn = null;
		try {
			conn = getConnection(dbProps);
			excuteUpdate(conn, query);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("", e);
				} // catch
			} // if
		} // finally
	} //executeQuery
	
	public void executeBatch(String dbName, String[] queries){
		logger.info("execute batch, count : {}\nfirst[0] : {}\nlast[{}] : {}", queries.length, queries[0], queries.length-1, queries[queries.length-1]);
		
		JSONObject dbProps = databaseService.loadDatabase(dbName);
		if (dbProps == null) {
			logger.error("dbName {} not exists", dbName);
			System.exit(-1);
		} // if
		
		Connection conn = null;
		try {
			conn = getConnection(dbProps);
			excuteUpdate(conn, queries);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		} catch (SQLException e) {
			logger.error("", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("", e);
				} // catch
			} // if
		} // finally
	} //executeQuery
	
	public void selectAndAppend(String dbName, String query, String delimiter, String filename, String charsetName){
		logger.info("select query : {}", query);
		
		JSONObject dbProps = databaseService.loadDatabase(dbName);
		if (dbProps == null) {
			logger.error("dbName {} not exists", dbName);
			System.exit(-1);
		} // if
		
		Connection conn = null;
		PrintWriter output = null;
			
		try {
			File exportFile = new File(filename);

			if(exportFile.getParentFile()!=null && !exportFile.getParentFile().exists())
				exportFile.getParentFile().mkdirs();
			if (!exportFile.exists())
				exportFile.createNewFile();

			Charset charset=null;
			if(charsetName==null)
				charset=Charset.defaultCharset();
			else
				charset=Charset.forName(charsetName);

			conn = getConnection(dbProps);
			output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(exportFile, true), charset));
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			ResultSetMetaData meta=rs.getMetaData();
			int colCount=meta.getColumnCount();

			while(rs.next()){
				StringBuilder rowSb=new StringBuilder();
				for(int i=1; i<=colCount; i++){
					String data=rs.getString(i);
					if(i!=1)
						rowSb.append(delimiter);
					if(data!=null)
						rowSb.append(data);
				} //for i
				if(rowSb.length()!=0){
					output.print(rowSb.toString());
					output.print("\n");
					output.flush();
					OutputFileLastModified.getInstance().pushLastModifiedTime(exportFile.getAbsolutePath(), System.currentTimeMillis());
					incrementStatistics();
				} //if
			} //while
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				if (conn != null) 
					conn.close();
				if(output!=null)
					output.close();
			} catch (Exception e) {
				logger.error("", e);
			} // catch
		} // finally
	} //selectAndAppend

	public String selectQuery(String dbName, String query) {
		return selectQuery(dbName, query, " ");
	} // selectQuery 

	public String selectQuery(String dbName, String query, String delimiter) {
		logger.info("select query : {}", query);

		JSONObject dbProps = databaseService.loadDatabase(dbName);
		if (dbProps == null) {
			logger.error("dbName {} not exists", dbName);
			System.exit(-1);
		} // if

		Connection conn = null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			conn = getConnection(dbProps);
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query);
			ResultSetMetaData meta=rs.getMetaData();
			int colCount=meta.getColumnCount();
			
			StringBuilder resultSb = new StringBuilder();
			while(rs.next()){
				StringBuilder row=new StringBuilder();
				for (int i = 1; i <= colCount; i++) {
					String data=rs.getString(i);
					if(data!=null)
						row.append(data);
					if(i<colCount){
						row.append(delimiter);
					} else{
						//last column
						if(row.toString().trim().length()!=0){
							resultSb.append(row.toString()).append("\n");
						} //if
					} //if
				} //for i
			} //while
			
			if(resultSb.toString().trim().length()!=0) {
				return resultSb.toString();
			} else{
				return null;
			} //if
		} catch (Exception e) {
			logger.error("", e);
			return null;
		} finally {
			close(conn, stmt, rs);
		} //finally
	} // selectQuery
	
	private Connection getConnection(JSONObject dbProps) throws SQLException, ClassNotFoundException{
		Class.forName(dbProps.getString("DRIVER"));
		String username = dbProps.getString("USERNAME");
		String password = dbProps.getString("PASSWORD");
		String connUrl = dbProps.getString("CONNECTION_URL");
		return DriverManager.getConnection(connUrl, username, password);
	} //getConnection

	private void close(Connection conn, Statement stmt, ResultSet rs) {
		try{
			if(conn!=null) {
				conn.close();
				conn=null;
			} //if
			
			if (rs != null) {
				rs.close();
				rs = null;
			} // if

			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch(SQLException e){
			logger.error("", e);
		} //catch
	} //close

	private boolean excuteUpdate(Connection conn, String query) throws SQLException {
		if (query == null || query.length() == 0) {
			return false;
		}

		boolean flag = false;
		Statement stmt = null;
		try {
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			flag = true;
		} finally {
			close(null, stmt, null);
		} // finally

		return flag;
	} // executeUpdate
	
	private boolean excuteUpdate(Connection conn, String[] queries) throws SQLException {
		if (queries == null || queries.length == 0) {
			return false;
		}

		boolean flag = false;
		Statement stmt = null;
		try {
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			for(String query : queries)
				stmt.addBatch(query);
			stmt.executeBatch();
			flag = true;
		} finally {
			close(null, stmt, null);
		} // finally

		return flag;
	} // executeUpdate
	
	private void incrementStatistics(){
		long scriptSequence = Long.parseLong(Thread.currentThread().getName().replace("ScriptThread-", ""));
		fileWriteStatistics.incrementCount(scriptSequence);
	} //incrementStatisticskj
} // class