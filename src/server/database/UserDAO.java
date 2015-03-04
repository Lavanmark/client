package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.model.User;

public class UserDAO {

	private static Logger logger;
	
	static{
		logger = Logger.getLogger("recordindexer");
	}
	
	private Database db = null;
	
	/**
	 * Initializes DAO class with instance of the Database
	 * @param db
	 */
	public UserDAO(Database db){
		this.db = db;
	}
	/**
	 * Gets a list of all the Users in Database
	 * 
	 * @return List of all Users
	 * @throws DatabaseException
	 */
	public List<User> getAll() throws DatabaseException{
		
		logger.entering("server.database.User", "getAll");
		
		ArrayList<User> result = new ArrayList<User>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, username, password, firstName, lastName, email, recordsIndexed, currentBatch from user";
			stmt = db.getConnection().prepareStatement(query);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				String usrname = rs.getString(2);
				String password = rs.getString(3);
				String fstName = rs.getString(4);
				String lstName = rs.getString(5);
				String email = rs.getString(6);
				int recordsIndx = rs.getInt(7);
				String curBatch = rs.getString(8);
				
				result.add(new User(id,usrname,password,fstName,lstName,email,recordsIndx,curBatch));
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.User", "getAll", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.User", "getAll");
		
		return result;
	}
	
	/**
	 * Adds the User to the Database
	 * @param User
	 * @throws DatabaseException
	 */
	public void add(User user) throws DatabaseException{
		
		logger.entering("server.database.User", "add");
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try{
			String query = "insert into user (username, password, firstName, lastName, email, recordsIndexed, currentBatch)"
					+ " values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getRecordsIndexed());
			stmt.setString(7, user.getCurrentBatch());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setId(id);
			}else{
				
				DatabaseException serverEx = new DatabaseException("Could not insert user");
				
				logger.throwing("server.database.User", "add", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			
			DatabaseException serverEx = new DatabaseException("Could not insert user", e);
			
			logger.throwing("server.database.User", "add", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		logger.exiting("server.database.User", "add");
	}
	
	/**
	 * Updates the User in the Database
	 * @param User
	 * @throws DatabaseException
	 */
	public void update(User user) throws DatabaseException{
		
		logger.entering("server.database.User", "update");
		
		PreparedStatement stmt = null;
		try{
			String query = "update user set username = ?, password = ?, firstName = ?, lastName = ?, email = ?, recordsIndexed = ?, currentBatch = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getRecordsIndexed());
			stmt.setString(7, user.getCurrentBatch());
			stmt.setInt(8, user.getId());
			if (stmt.executeUpdate() != 1) {
				DatabaseException serverEx = new DatabaseException("Could not update user");
				
				logger.throwing("server.database.User", "update", serverEx);
				
				throw serverEx;
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException("Could not update user",e);
			
			logger.throwing("server.database.User", "update", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.User", "update");
		
	}
	
	/**
	 * give it a partially built user and based on info it will find a user
	 * @param user
	 * @return null if can't find otherwise returns complete user object
	 * @throws DatabaseException
	 */
	public User getUser(User user) throws DatabaseException{
		
		logger.entering("server.database.User", "getUser");
		
		User result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String query = "select id, username, password, firstName, lastName, email, recordsIndexed, currentBatch"
					+ " from user"
					+ " where username = ? and password = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			
			rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt(1);
				String usrname = rs.getString(2);
				String password = rs.getString(3);
				String fstName = rs.getString(4);
				String lstName = rs.getString(5);
				String email = rs.getString(6);
				int recordsIndx = rs.getInt(7);
				String curBatch = rs.getString(8);
				
				result = new User(id,usrname,password,fstName,lstName,email,recordsIndx,curBatch);
			}
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.User", "getUser", serverEx);
			
			throw serverEx;
		}finally{
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		logger.exiting("server.database.User", "getUser");
		
		return result;
	}
	
}
