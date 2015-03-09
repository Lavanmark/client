package client.communication;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.communication.*;
import shared.model.*;
import client.ClientException;

public class ClientCommunicatorTests {

	private ClientCommunicator cc = null;
	private Database db = new Database();
	private User properUser;
	private List<Project> properProjects = null;
	private List<Batch> properBatches = null;
	private List<Field>	properFields = null;
	private List<Record> properRecords = null;
	
	
	
	@Before
	public void setup(){
		try{
			/* Add things to the database... 
			 * I know we shouldn't technically do it this way but I want to have lots of control and
			 * not depend on other parts of the program for these tests
			 */
			
			//User
			properUser = new User("bob","bob","bob","tastic","bob@ibob.com",1,-1);
			
			//Projects
			Project testp1 = new Project(23,400,15,"ProjUno");
			Project testp2 = new Project(1,2,3,"ProjDos");
			Project testp3 = new Project(2,2,3,"ProjTres");
			properProjects = new ArrayList<Project>();
			properProjects.add(testp1);
			properProjects.add(testp2);
			properProjects.add(testp3);
			
			//Batches
			properBatches = new ArrayList<Batch>();
			Batch testb1 = new Batch(1,"images/bird.png", 0);
			properBatches.add(testb1);
			
			//Fields
			properFields = new ArrayList<Field>();
			Field testf1 = new Field(1,1,"First name", "fields/help/help.html", 97, 20);
			properFields.add(testf1);
			
			properRecords = new ArrayList<Record>();
			Record testr1 = new Record(1,1,1,"Bob");
			Record testr2 = new Record(1,1,1,"larry");
			Record testr3 = new Record(1,1,1,"sam");
			Record testr4 = new Record(1,1,1,"Bobby");
			Record testr5 = new Record(1,1,1,"Don");
			Record testr6 = new Record(1,1,1,"Joe");
			properRecords.add(testr1);
			properRecords.add(testr2);
			properRecords.add(testr3);
			properRecords.add(testr4);
			properRecords.add(testr5);
			properRecords.add(testr6);
			
			//Add them to database
			db.startTransaction();
			db.getUserDAO().add(properUser);
			db.getProjectDAO().add(testp1);
			db.getProjectDAO().add(testp2);
			db.getProjectDAO().add(testp3);
			db.getBatchDAO().add(testb1);
			db.getFieldDAO().add(testf1);
			db.getRecordDAO().add(testr1);
			db.getRecordDAO().add(testr2);
			db.getRecordDAO().add(testr3);
			db.getRecordDAO().add(testr4);
			db.getRecordDAO().add(testr5);
			db.getRecordDAO().add(testr6);
			db.endTransaction(true);
		}catch(DatabaseException e){
			e.printStackTrace();
		}
		cc = new ClientCommunicator("localhost","39640");
	}
	
	@After
	public void teardown(){
		try{
			db.startTransaction();
			db.createDatabase();
			db.endTransaction(true);
		}catch(DatabaseException e){
			e.printStackTrace();
		}
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
	
	@Test
	public void getSampleImageTests(){
		try{
			//test legitimate sample image
			GetSampleImageParams params = new GetSampleImageParams("bob","bob",1);
			GetSampleImageResult result = cc.getSampleImage(params);
			
			GetSampleImageResult properResult = new GetSampleImageResult(this.properBatches.get(0).getImgFile());
			
			assertEquals(result,properResult);
			
			
			//test non existent batch image
			params.setProjectID(this.properProjects.get(1).getKey());
			result = cc.getSampleImage(params);
			
			properResult = new GetSampleImageResult();
			
			assertEquals(result,properResult);
			
			
			//test non existent project
			params.setProjectID(3000);
			result = cc.getSampleImage(params);
			
			properResult = new GetSampleImageResult();
			
			assertEquals(result,properResult);
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void downloadBatchTests(){
		try{
			
			//Test downloading an available batch
			GetBatchParams params = new GetBatchParams("bob","bob",this.properProjects.get(0).getKey());
			GetBatchResult result = cc.downloadBatch(params);
			
			//change status to in use because the server won't update it on client side... duh
			this.properBatches.get(0).setStatus(1);
			//make a result that will match if everything went down right
			List<Field> fields = new ArrayList<Field>();
			fields.add(this.properFields.get(0));
			GetBatchResult properResult = new GetBatchResult(this.properBatches.get(0),this.properProjects.get(0),fields);
			assertEquals(properResult, result);
			
			
			
			//test downloading a batch when we have a current batch
			result = cc.downloadBatch(params);
			assertEquals(result,null);
			
			
			
			//add a user for next part
			User test2 = new User("larry","bob","larry","tastic","bob@iamnotbob.com",1,-1);
			try{
				db.startTransaction();
				db.getUserDAO().add(test2);
				db.endTransaction(true);
			}catch(DatabaseException e){
				e.printStackTrace();
				assertFalse(true);
			}
			
			//Test downloading a batch in use
			params.setUsername("larry");
			result = cc.downloadBatch(params);
			assertEquals(result,null);
			
			
			
			//test downloading a nonexistent batch
			params.setProjectID(this.properProjects.get(2).getKey());
			result = cc.downloadBatch(params);
			assertEquals(result,null);
			
			
			
			//test downloading a batch from non existent project
			params.setProjectID(3000);
			result = cc.downloadBatch(params);
			assertEquals(result,null);
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void GetFieldsTests(){
		try{
			//add several fields for testing
			List<Field> properFieldsP1 = new ArrayList<Field>();
			List<Field> properFieldsP2 = new ArrayList<Field>();
			properFieldsP1.add(properFields.get(0));
			
			Field testf2 = new Field(2,1,"First name", "fields/help/help.html", 97, 20);
			Field testf3 = new Field(2,2,"Last name", "fields/help/help.html", 97, 20);
			Field testf4 = new Field(2,3,"hometown", "fields/help/help.html", 97, 20);
			Field testf5 = new Field(2,4,"age", "fields/help/help.html", 97, 20);
			properFields.add(testf2);
			properFields.add(testf3);
			properFields.add(testf4);
			properFields.add(testf5);
			properFieldsP2.add(testf2);
			properFieldsP2.add(testf3);
			properFieldsP2.add(testf4);
			properFieldsP2.add(testf5);
			try{
				db.startTransaction();
				db.getFieldDAO().add(testf2);
				db.getFieldDAO().add(testf3);
				db.getFieldDAO().add(testf4);
				db.getFieldDAO().add(testf5);
				db.endTransaction(true);
			}catch(DatabaseException e){
				e.printStackTrace();
				assertFalse(true);
			}
			
			
			
			//get fields for project 1
			GetFieldsParams params = new GetFieldsParams("bob","bob",1);
			GetFieldsResult result = cc.getFields(params);
			
			GetFieldsResult properResult = new GetFieldsResult(properFieldsP1);
			
			assertEquals(result, properResult);
			
			
			
			//get fields for project 2
			params.setProjectID(2);
			result = cc.getFields(params);
			
			properResult = new GetFieldsResult(properFieldsP2);
			
			assertEquals(result, properResult);
			
			
			
			//get fields for all projects
			params = new GetFieldsParams("bob","bob");
			result = cc.getFields(params);
			
			properResult = new GetFieldsResult(properFields);
			
			assertEquals(result, properResult);
			
			
			
			//get fields for non existent project
			params = new GetFieldsParams("bob","bob",3000);
			result = cc.getFields(params);
			properResult = new GetFieldsResult();
			assertEquals(result, properResult);
			
			
			
			//get fields for project with no fields
			params = new GetFieldsParams("bob","bob",3);
			result = cc.getFields(params);
			properResult = new GetFieldsResult();
			assertEquals(result, properResult);
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void submitBatchTests(){
		try{
			//check the batch out to user bob
			try{
				db.startTransaction();
				properBatches.get(0).setStatus(1);
				db.getBatchDAO().update(properBatches.get(0));
				db.endTransaction(true);
			}catch(DatabaseException e){
				e.printStackTrace();
				assertFalse(true);
			}
			//add these records
			AddBatchParams params = new AddBatchParams("bob","bob",new Batch(1, "id"),"Jones;Rogers;;;Van Fleet");
			
			assertTrue(cc.submitBatch(params).hasSucceded());
			
			//see if the fields are all there
			Database db = new Database();
			try{
				db.startTransaction();
				List<Record> recs = db.getRecordDAO().getAll();
				db.endTransaction(false);
				assertTrue(recs.size() == 11);
			}catch(DatabaseException e){
				e.printStackTrace();
				db.endTransaction(false);
				assertFalse(true);
			}
			
			//add records for non existent batch/project
			params = new AddBatchParams("bob","bob",new Batch(15, "id"),"Jones;Rogers;;;Van Fleet");
			
			assertFalse(cc.submitBatch(params).hasSucceded());
			
			//make sure only those five are still there
			try{
				db.startTransaction();
				List<Record> recs = db.getRecordDAO().getAll();
				db.endTransaction(false);
				assertTrue(recs.size() == 11);
			}catch(DatabaseException e){
				e.printStackTrace();
				db.endTransaction(false);
				assertFalse(true);
			}
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void searchTests(){
		try{
			//should find three of the records
			SearchParams params = new SearchParams("bob","bob","1","bob,Larry,sama,don");
			SearchResult result = cc.search(params);
			
			assertTrue(result.isSuccessful());
			assertTrue(result.getSearchTuples().size() == 3);
			
			//Invalid field ids
			params = new SearchParams("bob","bob","1,2,3","bob,Larry,sama,don");
			result = cc.search(params);
			
			assertFalse(result.isSuccessful());
			
			//just doesn't find it
			params = new SearchParams("bob","bob","1","sama");
			result = cc.search(params);
			
			assertTrue(result.isSuccessful());
			assertTrue(result.getSearchTuples().size() == 0);
			
		}catch(ClientException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
}
