package server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.model.User;

public class UserDAOTests {

	private Database db;
	private UserDAO usrd;
	
	@Before
	public void setup() {
		db = new Database();
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
			return;
		}
		usrd = db.getUserDAO();
		
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void addTest() {
		try{
			db.startTransaction();
			
			User test1 = new User("bobman","bobpass","bob","tastic","bob@iambob.com",1,"bobsbatch");
			User test2 = new User("man","pass","bob","tastic","bob@ambob.com",1,"batch");
			User test3 = new User("bob","bob","bob","tastic","bob@ibob.com",1,"bobs");
			User gotUser = null;
			
			//basic add then get user test
			usrd.add(test1);
			gotUser = usrd.getUser(test1);
			assertEquals(test1,gotUser);
			
			//getting a non existent user
			gotUser = null;
			User badusr = new User("bob", "notmypass");
			gotUser = usrd.getUser(badusr);
			assertEquals(gotUser,null);
			
			//get one user after adding multiple users
			usrd.add(test2);
			usrd.add(test3);
			gotUser = null;
			gotUser = usrd.getUser(test1);
			assertEquals(gotUser,test1);
			
			
		}catch(DatabaseException e){
			e.printStackTrace();
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void addDuplicateTest(){
		try{
			db.startTransaction();
			
			User test1 = new User("bobman","bobpass","bob","tastic","bob@iambob.com",1,"bobsbatch");
			
			//add user and then add again hopefully throwing exception
			usrd.add(test1);
				//this should catch because of column email
			usrd.add(test1);
			
			//if this happens we failed
			assertFalse(true);
			
		}catch(DatabaseException e){
			//additional tests
			if(e.getCause() != null){
				assertTrue(e.getCause().getLocalizedMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column email is not unique)"));
				
				//NEXT PART
				try{
					//Different email
					User test2 = new User("bobman","bobpass","bob","tastic","b@iambob.com",1,"bobsbatch");
					
					//Should fail here on same username
					usrd.add(test2);
					
					//no fail then assert it
					assertFalse(true);
				}catch(DatabaseException in){
					if(e.getCause() != null){
						assertTrue(in.getCause().getLocalizedMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column username is not unique)"));
					}else{
						assertFalse(true);
					}
				}
				
			}else{
				assertFalse(true);
			}
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void getAllTest(){
		try{
			db.startTransaction();
			User test1 = new User("bobman","bobpass","bob","tastic","bob@iambob.com",1,"bobsbatch");
			User test2 = new User("man","pass","bob","tastic","bob@ambob.com",1,"batch");
			User test3 = new User("bob","bob","bob","tastic","bob@ibob.com",1,"bobs");
			
			ArrayList<User> testUsrs = new ArrayList<User>();
			testUsrs.add(test1);
			testUsrs.add(test2);
			testUsrs.add(test3);
			
			usrd.add(test1);
			usrd.add(test2);
			usrd.add(test3);
			
			ArrayList<User> comp = new ArrayList<User>();
			comp = (ArrayList<User>) usrd.getAll();
			
			assertEquals(comp,testUsrs);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void updateTest(){
		try{
			db.startTransaction();
			
			User test1 = new User("bobman","bobpass","bob","tastic","bob@iambob.com",1,"bobsbatch");
			User gotUser = null;
			
			//basic add then get user test
			usrd.add(test1);
			gotUser = usrd.getUser(test1);
			assertEquals(test1,gotUser);
			
			//update the password test
			gotUser = null;
			test1.setPassword("newpass");
			usrd.update(test1);
			gotUser = usrd.getUser(test1);
			assertEquals(test1,gotUser);
			
		}catch(DatabaseException e){
			if(e.getCause() != null)
				System.out.println(e.getCause().toString());
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	
	
}
