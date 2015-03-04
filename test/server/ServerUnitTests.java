package server;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DatabaseException;


public class ServerUnitTests {
	

	
	@BeforeClass
	public static void setup() {
		String[] args = new String[1];
		args[0] = "39640";
		Server.main(args);
		Database db = new Database();
		try{
			Database.initialize();
			db.startTransaction();
			db.createDatabase();
			db.endTransaction(true);
		}catch(DatabaseException e){
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void teardown() {
		
	}
	
	@Test
	public void basicTests(){
		assertTrue(true);
	}
	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.ServerUnitTests",
				"server.database.UserDAOTests",
				"server.database.RecordDAOTests",
				"server.database.ProjectDAOTests",
				"server.database.FieldDAOTests",
				"server.database.BatchDAOTests",
				"client.communication.ClientCommunicatorTests"
		};

		org.junit.runner.JUnitCore.main(testClasses);
		Database db = new Database();
		try{
			Database.initialize();
			db.startTransaction();
			db.createDatabase();
			db.endTransaction(true);
		}catch(DatabaseException e){
			e.printStackTrace();
		}
	}
	
}

