package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.Field;

public class FieldDAO {

	
	private static Logger logger;
	
	static{
		logger = Logger.getLogger("recordindexer");
	}
	
	private Database db = null;
	
	
	
	
	/**
	 * Initializes DAO class with instance of the Database
	 * @param db
	 */
	public FieldDAO(Database db){
		this.db = db;
	}
	
	/**
	 * Gets a list of all the Fields in Database
	 * 
	 * @return List of all Fields
	 * @throws DatabaseException
	 */
	public List<Field> getAll() throws DatabaseException{

		logger.entering("server.database.Field", "getAll");
		
		ArrayList<Field> result = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, recordOrder, title, xCoord, width, helpHtml, knownData from field";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				int recordOrder = rs.getInt(3);
				String title = rs.getString(4);
				int xCoord = rs.getInt(5);
				int width = rs.getInt(6);
				String helpHtml = rs.getString(7);
				String knownData = rs.getString(8);
				
				result.add(new Field(id, projectKey, recordOrder, title, helpHtml, knownData, xCoord, width));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Field", "getAll", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Field", "getAll");
		
		return result;
	}
	
	/**
	 * Adds the Field to the Database
	 * @param Field
	 * @throws DatabaseException
	 */
	public void add(Field field) throws DatabaseException{
		logger.entering("server.database.Field", "add");
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try{
			String query = "insert into field (projectKey, recordOrder, title, xCoord, width, helpHtml, knownData)"
					+ " values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field.getProjectKey());
			stmt.setInt(2, field.getRecordOrder());
			stmt.setString(3, field.getTitle());
			stmt.setInt(4, field.getxCoord());
			stmt.setInt(5, field.getWidth());
			stmt.setString(6, field.getHelpHtml());
			stmt.setString(7, field.getKnownData());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setId(id);
			}else{
				
				DatabaseException serverEx = new DatabaseException("Could not insert field");
				
				logger.throwing("server.database.Field", "add", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			
			DatabaseException serverEx = new DatabaseException("Could not insert field", e);
			
			logger.throwing("server.database.Field", "add", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		logger.exiting("server.database.Field", "add");
	}
	
	/**
	 * Gets a field based on incomplete field object
	 * @param field
	 * @return null if not found or field object if found
	 * @throws DatabaseException
	 */
	public Field getField(Field field)throws DatabaseException{
		logger.entering("server.database.Field", "getField");
		
		Field result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, recordOrder, title, xCoord, width, helpHtml, knownData"
					+ " from field"
					+ " where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, field.getId());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				int recordOrder = rs.getInt(3);
				String title = rs.getString(4);
				int xCoord = rs.getInt(5);
				int width = rs.getInt(6);
				String helpHtml = rs.getString(7);
				String knownData = rs.getString(8);
				
				result = new Field(id, projectKey, recordOrder, title, helpHtml, knownData, xCoord, width);
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Field", "getField", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Field", "getField");
		
		return result;
	}
	/**
	 * 
	 * @param field
	 * @return
	 * @throws DatabaseException
	 */
	public List<Field> getProjectsFields(Field field)throws DatabaseException{
		logger.entering("server.database.Field", "getField");
		
		List<Field> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, recordOrder, title, xCoord, width, helpHtml, knownData"
					+ " from field"
					+ " where projectKey = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, field.getProjectKey());
			
			rs = stmt.executeQuery();
			result = new ArrayList<Field>();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				int recordOrder = rs.getInt(3);
				String title = rs.getString(4);
				int xCoord = rs.getInt(5);
				int width = rs.getInt(6);
				String helpHtml = rs.getString(7);
				String knownData = rs.getString(8);
				
				result.add(new Field(id, projectKey, recordOrder, title, helpHtml, knownData, xCoord, width));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Field", "getField", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Field", "getField");
		
		return result;
	}
}
