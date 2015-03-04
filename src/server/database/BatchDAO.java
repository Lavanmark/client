package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.Batch;

public class BatchDAO {

	private static Logger logger;
	
	static{
		logger = Logger.getLogger("recordindexer");
	}
	
	private Database db = null;
	
	
	
	
	/**
	 * Initializes DAO class with instance of the Database
	 * @param db
	 */
	public BatchDAO(Database db){
		this.db = db;
	}
	/**
	 * Gets a list of all the batches in Database
	 * 
	 * @return List of all Batches
	 * @throws DatabaseException
	 */
	public List<Batch> getAll() throws DatabaseException{
		logger.entering("server.database.Batch", "getAll");
		
		ArrayList<Batch> result = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, file, status from batch";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				String file = rs.getString(3);
				int status = rs.getInt(4);
				
				result.add(new Batch(id, projectKey, file, status));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Batch", "getAll", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Batch", "getAll");
		
		return result;
	}
	
	/**
	 * Adds the batch to the Database
	 * @param batch
	 * @throws DatabaseException
	 */
	public void add(Batch batch) throws DatabaseException{
		logger.entering("server.database.Batch", "add");
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try{
			String query = "insert into batch (projectKey, file, status)"
					+ " values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProjectKey());
			stmt.setString(2, batch.getImgFile());
			stmt.setInt(3, batch.getStatus());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				batch.setId(id);
			}else{
				
				DatabaseException serverEx = new DatabaseException("Could not insert batch");
				
				logger.throwing("server.database.Batch", "add", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			
			DatabaseException serverEx = new DatabaseException("Could not insert batch", e);
			
			logger.throwing("server.database.Batch", "add", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		logger.exiting("server.database.Batch", "add");
		
	}

	/**
	 * Updates the batch in the Database
	 * @param batch
	 * @throws DatabaseException
	 */
	public void update(Batch batch) throws DatabaseException{

		logger.entering("server.database.Batch", "update");
		
		PreparedStatement stmt = null;
		try{
			String query = "update batch set projectKey = ?, file = ?, status = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProjectKey());
			stmt.setString(2, batch.getImgFile());
			stmt.setInt(3, batch.getStatus());
			stmt.setInt(4, batch.getId());
			if (stmt.executeUpdate() != 1) {
				DatabaseException serverEx = new DatabaseException("Could not update user");
				
				logger.throwing("server.database.Batch", "update", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException("Could not update user",e);
			
			logger.throwing("server.database.Batch", "update", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Batch", "update");
		
	}
	
	/**
	 * Gets a batch with batch id
	 * @param batch
	 * @return null if not found batch if found
	 * @throws DatabaseException
	 */
	public Batch getBatch(Batch batch) throws DatabaseException{
		logger.entering("server.database.Batch", "getBatch");
		
		Batch result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, file, status"
					+ " from batch"
					+ " where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, batch.getId());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				String file = rs.getString(3);
				int status = rs.getInt(4);
				
				
				result = new Batch(id, projectKey, file, status);
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Batch", "getBatch", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Batch", "getBatch");
		
		return result;
	}
	
	/**
	 * Gets all the batches for a specific project
	 * 
	 * @param batch
	 * @return
	 * @throws DatabaseException
	 */
	public List<Batch> getProjectsBatch(Batch batch) throws DatabaseException{
		logger.entering("server.database.Batch", "getProjectsBatch");
		
		List<Batch> result = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, projectKey, file, status"
					+ " from batch"
					+ " where projectKey = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setInt(1, batch.getProjectKey());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				int projectKey = rs.getInt(2);
				String file = rs.getString(3);
				int status = rs.getInt(4);
				
				result.add(new Batch(id, projectKey, file, status));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Batch", "getBatch", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.Batch", "getProjectsBatch");
		
		return result;
	}
}
