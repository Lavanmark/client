package client.communication;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.communication.GetAllProjectResult;
import shared.communication.ValidateUserParams;
import shared.communication.ValidateUserResult;
import shared.model.Project;
import shared.model.User;
import client.ClientException;

public class ClientCommunicatorTests {

	private static ClientCommunicator cc = null;
	private static Database db = new Database();;
	private static User properUser = new User("bob","bob","bob","tastic","bob@ibob.com",1,"bobs");;
	private static List<Project> properProjects = null;
	@BeforeClass
	public static void setup(){
		try{
			/* Add things to the database... 
			 * I know we shouldn't technically do it this way but I want to have lots of control and
			 * not depend on other parts of the program for these tests
			 */
			//User
			//properUser = new User("bob","bob","bob","tastic","bob@ibob.com",1,"bobs");
			
			//Projects
			Project testp1 = new Project(1,2,3,"ProjUno");
			Project testp2 = new Project(1,2,3,"ProjDos");
			Project testp3 = new Project(2,2,3,"ProjTres");
			properProjects = new ArrayList<Project>();
			properProjects.add(testp1);
			properProjects.add(testp2);
			properProjects.add(testp3);
			
			
			
			//Add them to database
			db.startTransaction();
			db.getUserDAO().add(properUser);
			db.getProjectDAO().add(testp1);
			db.getProjectDAO().add(testp2);
			db.getProjectDAO().add(testp3);
			db.endTransaction(true);
		}catch(DatabaseException e){
			e.printStackTrace();
		}
		cc = new ClientCommunicator("localhost","39640");
	}
	
	@AfterClass
	public static void teardown(){
		
	}
	
	@Test
	public void validateUserTests(){
		try{
			//Test valid user
			ValidateUserParams params = new ValidateUserParams("bob","bob");
			ValidateUserResult result = cc.validateUser(params); 
			
			ValidateUserResult properResult = new ValidateUserResult(true, properUser);
			
			assertEquals(result,properResult);
			
			
			//Test invalid user
			params.setPassword("notthepassword");
			result = cc.validateUser(params);
			
			assertFalse(properResult.equals(result));
			
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void getAllProjectsTests(){
		try{
			//Test valid user
			ValidateUserParams params = new ValidateUserParams("bob","bob");
			GetAllProjectResult result = cc.getAllProjects(params); 
			
			GetAllProjectResult properResult = new GetAllProjectResult(properProjects);
			
			assertEquals(result,properResult);
			
			
			//Test invalid user
			params.setPassword("notthepassword");
			result = cc.getAllProjects(params);
			
			assertFalse(properResult.equals(result));
			
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	//TODO add more of these tests
}
