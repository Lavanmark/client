package server.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database {

	
	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "recordindexer.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY + File.separator + DATABASE_FILE;
	
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("recordindexer");
	}
	
	private Connection connection;
	
	private UserDAO userDAO;
	private ProjectDAO projectDAO;
	private BatchDAO batchDAO;
	private RecordDAO recordDAO;
	private FieldDAO fieldDAO;
	
	
	/**
	 * Creates instances of all DAO classes
	 */
	public Database(){
		this.userDAO = new UserDAO(this);
		this.projectDAO = new ProjectDAO(this);
		this.batchDAO = new BatchDAO(this);
		this.recordDAO = new RecordDAO(this);
		this.fieldDAO = new FieldDAO(this);
		connection = null;
	}
	
	/**
	 * Initialize the database
	 * @throws DatabaseException
	 */
	public static void initialize() throws DatabaseException{
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}catch(ClassNotFoundException e){
			DatabaseException serverEx = new DatabaseException("Could not load database");
			
			logger.throwing("server.database.Database", "initialize", serverEx);
			
			throw serverEx;
		}
	}
	
	/**
	 * DROPS ALL TABLES then creates new tables
	 */
	public void createDatabase() throws DatabaseException{
		try{
			dropTables();
			createTables();
		}catch(SQLException e){
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.Database", "createDatabase", serverEx);
			
			throw serverEx;
		}
	}
	
	//Used by create database
	private void dropTables() throws SQLException{
		String dropUser = "drop table if exists user; ";
		String dropProject = "drop table if exists project;";
		String dropField = "drop table if exists field;";
		String dropBatch = "drop table if exists batch;";
		String dropRecord = "drop table if exists record;";
		PreparedStatement stmt = null;
		
		stmt = connection.prepareStatement(dropUser);
		stmt.execute();
		stmt = connection.prepareStatement(dropProject);
		stmt.execute();
		stmt = connection.prepareStatement(dropField);
		stmt.execute();
		stmt = connection.prepareStatement(dropBatch);
		stmt.execute();
		stmt = connection.prepareStatement(dropRecord);
		stmt.execute();
		
		Database.safeClose(stmt);
	}
	
	//Used by create database
	private void createTables() throws SQLException{
		String createUser = "create table user(id integer not null primary key autoincrement,username text unique not null,password text not null,firstName text not null,lastName text not null,email text unique not null,recordsIndexed integer,currentBatch text);";
		String createProject = "create table project(key integer not null primary key autoincrement,title text unique not null,recordsPerImage integer,firstYCoord integer,recordHeight integer);";
		String createField = "create table field(id integer not null primary key autoincrement,projectKey integer not null,recordOrder integer not null,title text not null,xCoord integer not null,width integer not null,helpHtml text not null,knownData text);";
		String createBatch = "create table batch(id integer not null primary key autoincrement,projectKey integer not null,file text unique not null,status integer not null);";
		String createRecord = "create table record(recordNum integer not null primary key autoincrement,projectKey integer not null,imageKey integer not null,fieldKey integer not null,data text);";
		PreparedStatement stmt = null;
		
		stmt = connection.prepareStatement(createUser);
		stmt.execute();
		stmt = connection.prepareStatement(createProject);
		stmt.execute();
		stmt = connection.prepareStatement(createField);
		stmt.execute();
		stmt = connection.prepareStatement(createBatch);
		stmt.execute();
		stmt = connection.prepareStatement(createRecord);
		stmt.execute();
		
		Database.safeClose(stmt);
	}
	
	/**
	 * Starts connection to database 
	 * @throws DatabaseException
	 */
	public void startTransaction() throws DatabaseException{
		try {
			assert(connection == null);
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}catch(SQLException e){
			throw new DatabaseException("Could not connect to database. Make sure" + DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY,e);
		}
	}
	
	/**
	 * Ends connection to database either committing or rollingback changes
	 * @param commit
	 */
	public void endTransaction(boolean commit){
		if(connection != null){
			try{
				if(commit){
					connection.commit();
				}else{
					connection.rollback();
				}
			}catch(SQLException e){
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}finally{
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	
	
	
	/*
	 * 
	 * Static functions that safely close respective objects.
	 * 
	 */
	
	public static void safeClose(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){
				
			}
		}
	}
	
	public static void safeClose(Statement stmt){
		if(stmt != null){
			try{
				stmt.close();
			}catch(SQLException e){
				
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public Connection getConnection(){
		return connection;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	
	public BatchDAO getBatchDAO() {
		return batchDAO;
	}
	
	public RecordDAO getRecordDAO() {
		return recordDAO;
	}
	
	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}
}
