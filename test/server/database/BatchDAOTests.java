package server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.model.Batch;

public class BatchDAOTests {

	private Database db;
	private BatchDAO batd;
	
	
	
	
	@Before
	public void setup() {
		db = new Database();
		batd = db.getBatchDAO();
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void addTest() {
		try{
			db.startTransaction();
			
			Batch test1 = new Batch(1, "file", 3);
			Batch test2 = new Batch(2, "difffile", 4);
			Batch test3 = new Batch(3, "thisisit", 1);
			Batch gotBatch = null;
			
			//basic add then get batch test
			batd.add(test1);
			gotBatch = batd.getBatch(test1);
			assertEquals(test1,gotBatch);
			
			//getting a non existent batch
			gotBatch = null;
			Batch badusr = new Batch();
			gotBatch = batd.getBatch(badusr);
			assertEquals(gotBatch,null);
			
			//get one batch after adding multiple batches
			batd.add(test2);
			batd.add(test3);
			gotBatch = null;
			gotBatch = batd.getBatch(test1);
			assertEquals(gotBatch,test1);
			
			
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
			
			Batch test1 = new Batch(1, "file", 3);
			
			//add batch and then add again hopefully throwing exception
			batd.add(test1);
				//this should catch because of column email
			batd.add(test1);
			
			//if this happens we failed
			assertFalse(true);
			
		}catch(DatabaseException e){
			//additional tests
			if(e.getCause() != null){
				assertTrue(e.getCause().getLocalizedMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column file is not unique)"));
				
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
			Batch test1 = new Batch(1, "file", 3);
			Batch test2 = new Batch(2, "difffile", 2);
			Batch test3 = new Batch(3, "thisisit", 1);
			
			List<Batch> testBchs = new ArrayList<Batch>();
			testBchs.add(test1);
			testBchs.add(test2);
			testBchs.add(test3);
			
			batd.add(test1);
			batd.add(test2);
			batd.add(test3);
			
			List<Batch> comp = null;
			comp = (List<Batch>) batd.getAll();
			
			assertEquals(comp,testBchs);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void getProjectsBatchesTest(){
		try{
			db.startTransaction();
			Batch test1 = new Batch(1, "file", 3);
			Batch test2 = new Batch(1, "difffile", 2);
			Batch test3 = new Batch(3, "thisisit", 1);
			
			List<Batch> testBchs = new ArrayList<Batch>();
			testBchs.add(test1);
			testBchs.add(test2);
			
			batd.add(test1);
			batd.add(test2);
			batd.add(test3);
			
			Batch basedOnMe = new Batch(1,"project");
			List<Batch> comp = null;
			comp = batd.getProjectsBatch(basedOnMe);
			
			assertEquals(comp,testBchs);
			
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
			
			Batch test1 = new Batch(1, "file", 3);
			Batch gotBatch = null;
			
			//basic add then get batch test
			batd.add(test1);
			gotBatch = batd.getBatch(test1);
			assertEquals(test1,gotBatch);
			
			//update the password test
			gotBatch = null;
			test1.setStatus(3);
			batd.update(test1);
			gotBatch = batd.getBatch(test1);
			assertEquals(test1,gotBatch);
			
		}catch(DatabaseException e){
			if(e.getCause() != null)
				System.out.println(e.getCause().toString());
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
}
