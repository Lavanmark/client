package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.Record;

public class RecordDAO {

	private static Logger logger;
	
	static{
		logger = Logger.getLogger("recordindexer");
	}
	
	private Database db = null;
	
	
	
	
	/**
	 * Initializes DAO class with instance of the Database
	 * @param db
	 */
	public RecordDAO(Database db){
		this.db = db;
	}
	
	/**
	 * Gets a list of all the Records in Database
	 * 
	 * @return List of all Records
	 * @throws DatabaseException
	 */
	public List<Record> getAll() throws DatabaseException{
		logger.entering("server.database.Record", "getAll");
		
		ArrayList<Record> result = new ArrayList<Record>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select recordNum, rowNum, projectKey, imageKey, fieldKey, data from record";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int recordNum = rs.getInt(1);
				int rowNum = rs.getInt(2);
				int projectKey = rs.getInt(3);
				int imageKey = rs.getInt(4);
				int fieldKey = rs.getInt(5);
				String data = rs.getString(6);
				
				result.add(new Record(recordNum, rowNum, projectKey, imageKey, fieldKey, data));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Record", "getAll", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Record", "getAll");
		
		return result;
	}
	
	/**
	 * Adds the Record to the Database
	 * @param Record
	 * @throws DatabaseException
	 */
	public void add(Record record) throws DatabaseException{
		logger.entering("server.database.Record", "add");
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try{
			String query = "insert into record (rowNum, projectKey, imageKey, fieldKey, data)"
					+ " values (?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getRowNum());
			stmt.setInt(2, record.getProjectKey());
			stmt.setInt(3, record.getImageKey());
			stmt.setInt(4, record.getFieldID());
			stmt.setString(5, record.getData());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				record.setRecordNum(id);
			}else{
				
				DatabaseException serverEx = new DatabaseException("Could not insert record");
				
				logger.throwing("server.database.Record", "add", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			
			DatabaseException serverEx = new DatabaseException("Could not insert record", e);
			
			logger.throwing("server.database.Record", "add", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		logger.exiting("server.database.Record", "add");
		
	}
	
	/**
	 * Based on incomplete record it finds a matching record
	 * @param record
	 * @return null if not found record object if found
	 * @throws DatabaseException
	 */
	public Record getRecord(Record record) throws DatabaseException{
		logger.entering("server.database.Record", "getRecord");
		
		Record result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select recordNum, rowNum, projectKey, imageKey, fieldKey, data"
					+ " from record"
					+ " where recordNum = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, record.getRecordNum());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int recordNum = rs.getInt(1);
				int rowNum = rs.getInt(2);
				int projectKey = rs.getInt(3);
				int imageKey = rs.getInt(4);
				int fieldKey = rs.getInt(5);
				String data = rs.getString(6);
				
				
				result = new Record(recordNum, rowNum,projectKey, imageKey, fieldKey, data);
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Record", "getRecord", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Record", "getRecord");
		
		return result;
	}
}
