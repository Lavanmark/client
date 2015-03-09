package server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.model.Record;

public class RecordDAOTests {

	private Database db;
	private RecordDAO rcrd;
	
	
	
	
	@Before
	public void setup() {
		db = new Database();
		rcrd = db.getRecordDAO();
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void addTest() {
		try{
			db.startTransaction();
			
			Record test1 = new Record(1,2,3,"Bob");
			Record test2 = new Record(1,2,3,"Larry");
			Record test3 = new Record(2,2,3,"Smith");
			Record gotRecord = null;
			
			//basic add then get Record test
			rcrd.add(test1);
			gotRecord = rcrd.getRecord(test1);
			assertEquals(test1,gotRecord);
			
			//getting a non existent Record
			gotRecord = null;
			Record badrcd = new Record(3,1,2,"Jeff");
			gotRecord = rcrd.getRecord(badrcd);
			assertEquals(gotRecord,null);
			
			//get one Record after adding multiple Records
			rcrd.add(test2);
			rcrd.add(test3);
			gotRecord = null;
			gotRecord = rcrd.getRecord(test1);
			assertEquals(gotRecord,test1);
			
			
		}catch(DatabaseException e){
			e.printStackTrace();
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void getAllTest(){
		try{
			db.startTransaction();

			Record test1 = new Record(1,2,3,"Bob");
			Record test2 = new Record(1,2,3,"Larry");
			Record test3 = new Record(2,2,3,"Smith");
			
			List<Record> testRcrds = new ArrayList<Record>();
			testRcrds.add(test1);
			testRcrds.add(test2);
			testRcrds.add(test3);
			
			rcrd.add(test1);
			rcrd.add(test2);
			rcrd.add(test3);
			
			List<Record> comp = null;
			comp = rcrd.getAll();
			
			assertEquals(comp,testRcrds);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
}
