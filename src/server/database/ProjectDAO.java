package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.Project;

public class ProjectDAO {

	private static Logger logger;
	
	static{
		logger = Logger.getLogger("recordindexer");
	}
	
	private Database db = null;
	
	
	
	
	/**
	 * Initializes DAO class with instance of the Database
	 * @param db
	 */
	public ProjectDAO(Database db){
		this.db = db;
	}
	
	/**
	 * Gets a list of all the Projects in Database
	 * 
	 * @return List of all Projects
	 * @throws DatabaseException
	 */
	public List<Project> getAll() throws DatabaseException{
		logger.entering("server.database.Project", "getAll");
		
		List<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select key, title, recordsPerImage, firstYCoord, recordHeight from project";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int key = rs.getInt(1);
				String title = rs.getString(2);
				int recordsPerImage = rs.getInt(3);
				int firstYCoord = rs.getInt(4);
				int recordHeight = rs.getInt(5);
				
				result.add(new Project(key, recordsPerImage, firstYCoord, recordHeight, title));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Project", "getAll", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Project", "getAll");
		
		return result;	
	}
	
	/**
	 * Adds the Project to the Database
	 * @param Project
	 * @throws DatabaseException
	 */
	public void add(Project project) throws DatabaseException{
		logger.entering("server.database.Project", "add");
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try{
			String query = "insert into project (title, recordsPerImage, firstYCoord, recordHeight)"
					+ " values (?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordHeight());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int key = keyRS.getInt(1);
				project.setKey(key);
			}else{
				
				DatabaseException serverEx = new DatabaseException("Could not insert project");
				
				logger.throwing("server.database.Project", "add", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			
			DatabaseException serverEx = new DatabaseException("Could not insert project", e);
			
			logger.throwing("server.database.Project", "add", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		logger.exiting("server.database.Project", "add");
	}
	
	/**
	 * Gets specific project based on uncompleted project object
	 * @param project
	 * @return null if can't find project object if can
	 * @throws DatabaseException
	 */
	public Project getProject(Project project) throws DatabaseException{
		logger.entering("server.database.Projet", "getRecord");
		
		Project result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select key, title, recordsPerImage, firstYCoord, recordHeight"
					+ " from project"
					+ " where key = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, project.getKey());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int key = rs.getInt(1);
				String title = rs.getString(2);
				int recordsPerImage = rs.getInt(3);
				int firstYCoord = rs.getInt(4);
				int recordHeight = rs.getInt(5);
				
				result = new Project(key, recordsPerImage, firstYCoord, recordHeight, title);
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Project", "getRecord", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Project", "getRecord");
		
		return result;	
	}
}
