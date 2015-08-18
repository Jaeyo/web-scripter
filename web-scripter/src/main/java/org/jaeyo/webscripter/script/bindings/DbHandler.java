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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.jaeyo.webscripter.common.SpringBeans;
import org.jaeyo.webscripter.outputfiledelete.OutputFileLastModified;
import org.jaeyo.webscripter.service.DatabaseService;
import org.jaeyo.webscripter.statistics.FileWriteStatistics;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.sun.xml.internal.bind.v2.TODO;

public class DbHandler {
	private static final Logger logger = LoggerFactory.getLogger(DbHandler.class);
	private DatabaseService databaseService = SpringBeans.getBean(DatabaseService.class);
	private FileWriteStatistics fileWriteStatistics = SpringBeans.getBean(FileWriteStatistics.class);

	/**
	 * @param args: {
	 * 		database: {
	 * 			driver: (string)(required)
	 * 			connUrl: (string)(required)
	 * 			username: (string)(required)(encrypted)
	 * 			password: (string)(required)(encrypted)
	 * 		},
	 * 		query: (string)(required)
	 * }
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void update(Map<String, Object> args) throws SQLException, ClassNotFoundException{
		Map<String, Object> database = (Map<String, Object>) args.get("database");
		String query = (String) args.get("query");
		
		logger.info("query: {}", query);
		
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = getConnection(database);
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} finally {
			close(conn, stmt, null);
		} //finally
	} //query
	
	/**
	 * @param args: {
	 * 		database: {
	 * 			driver: (string)(required)
	 * 			connUrl: (string)(required)
	 * 			username: (string)(required)(encrypted)
	 * 			password: (string)(required)(encrypted)
	 * 		},
	 * 		queries: (array of string)(required)
	 * }
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void batch(Map<String, Object> args) throws SQLException {
		Map<String, Object> database = (Map<String, Object>) args.get("database");
		List<String> queries = (List<String>) args.get("queries");
		
		logger.info("queries count: {}", queries.size());
		if(queries.size() != 0)
		
		Connection conn = null;
		Statement stmt = null;
		try{
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			for(String query : queries)
				stmt.addBatch(query);
			stmt.executeBatch();
		} finally {
			close(conn, stmt, null);
		} //finally
	} //batch
	
	private void query(Connection conn, String query, Function<ResultSet, Void> callback) throws SQLException{
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
				callback.apply(rs);
		} finally {
			close(conn, stmt, rs);
		} //finally
	} //query
	
	/**
	 * @param args: {
	 * 		database: {
	 * 			driver: (string)(required)
	 * 			connUrl: (string)(required)
	 * 			username: (string)(required)(encrypted)
	 * 			password: (string)(required)(encrypted)
	 * 		},
	 * 		query: (string)(required)
	 * 		delimiter: (string)(default '|')
	 * 		filename: (string)(required)
	 * 		charset: (string)(default 'utf8')
	 * }
	 */
	public void selectAndAppend(Map<String, Object> args){
		Map<String, Object> database = (Map<String, Object>) args.get("database");
		String query = (String) args.get("query");
		String delimiter = (String) args.get("delimiter");
		String filename = (String) args.get("filename");
		String charset = (String) args.get("charset");
		
		if(delimiter == null) delimiter = "|";
		if(charset == null) charset = "utf8";
		
		TODO IMME
	} //selectAndAppend
	
	/**
	 * @param args: {
	 * 		database: {
	 * 			driver: (string)(required)
	 * 			connUrl: (string)(required)
	 * 			username: (string)(required)(encrypted)
	 * 			password: (string)(required)(encrypted)
	 * 		},
	 * 		query: (string)(required)
	 * 		delimiter: (string)(default: '|')
	 * 		lineDelimiter: (string)(default: '\n')
	 * }
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public String query(Map<String, Object> args) throws ClassNotFoundException, SQLException{
		Map<String, Object> database = (Map<String, Object>) args.get("database");
		String query = (String) args.get("query");
		String delimiter = (String) args.get("delimiter");
		String lineDelimiter = (String) args.get("lineDelimiter");
		
		if(delimiter == null) delimiter = "|";
		if(lineDelimiter == null) lineDelimiter = "\n";
		
		final String finalDelimiter = delimiter;
		final String finalLineDelimiter = lineDelimiter;
		final StringBuilder resultSb = new StringBuilder();
		query(getConnection(database), query, new Function<ResultSet, Void>() {
			@Override
			public Void apply(ResultSet rs){
				try {
					int colCount = rs.getMetaData().getColumnCount();
					StringBuilder rowSb = new StringBuilder();
					for (int i = 0; i < colCount; i++) {
						String value = rs.getString(i);
						if(value != null) rowSb.append(value);
						if(i < colCount) {
							rowSb.append(finalDelimiter);
						} else{
							if(rowSb.toString().trim().length() != 0)
								resultSb.append(rowSb.toString()).append(finalLineDelimiter);
						} //if
					} //for i
				} catch (SQLException e) {
					logger.error(String.format("%s, errmsg: %s", e.getClass().getSimpleName(), e.getMessage()), e);
				} //catch
				return null;
			} //apply
		});
		
		return resultSb.toString();
	} //query
	
	public void query(Map<String, Object> args, sun.org.mozilla.javascript.internal.Function callback){
		TODO IMME
	} //query
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

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

	public DbRowIterator selectQueryIterator(String dbName, String query) {
		logger.info("query : {}", query);

		JSONObject dbProps = databaseService.loadDatabase(dbName);
		if (dbProps == null) {
			logger.error("dbName {} not exists", dbName);
			return null;
		} // if

		Connection conn = null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			conn = getConnection(dbProps);
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query);
	
			return new DbRowIterator(conn, rs, stmt);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		} //finally
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
	
	private Connection getConnection(Map<String, Object> database) throws SQLException, ClassNotFoundException{
		Class.forName((String) database.get("driver"));
		String username = (String) database.get("username");
		String password = (String) database.get("password");
		String connUrl = (String) database.get("connUrl");
		
		return DriverManager.getConnection(connUrl, username, password);
	} //getConnection

	private void close(Connection conn, Statement stmt, ResultSet rs) {
		if(conn != null) try{ conn.close(); } catch(SQLException e){}
		if(stmt!= null) try{ stmt.close(); } catch(SQLException e){}
		if(rs!= null) try{ rs.close(); } catch(SQLException e){}
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
	
	public class DbRowIterator {
		private Connection conn;
		private ResultSet rs;
		private Statement stmt;

		public DbRowIterator(Connection conn, ResultSet rs, Statement stmt) {
			this.conn = conn;
			this.rs = rs;
			this.stmt = stmt;
		} //INIT
		
		public String[] next() {
			try {
				if(rs.next() == false)
					return null;
				
				ResultSetMetaData meta = rs.getMetaData();
				int colCount = meta.getColumnCount();
				String[] row = new String[colCount];
				for (int i = 1; i <= colCount; i++)
					row[i-1] = rs.getString(i);
				return row;
			} catch (SQLException e) {
				logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
				return null;
	 		} //catch
		} //next

		public void close(){
			try { this.rs.close(); } catch (SQLException e) {}
			try { this.conn.close(); } catch (SQLException e) {}
			try { this.stmt.close(); } catch (SQLException e) {}
		} //close
	} //class
} // class