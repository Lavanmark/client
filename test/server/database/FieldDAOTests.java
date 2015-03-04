package server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.model.Field;


public class FieldDAOTests {

	private Database db;
	private FieldDAO fldd;
	
	
	
	
	@Before
	public void setup() {
		db = new Database();
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
			return;
		}
		fldd = db.getFieldDAO();
		
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void addTest() {
		try{
			db.startTransaction();
			
			Field test1 = new Field(1, 2,"firstname","a place","file", 3, 2);
			Field test2 = new Field(2, 5,"lastname","nothing","location", 10, 6);
			Field test3 = new Field(3, 4,"country","something","or something", 300, 23);
			Field gotField = null;
			
			//basic add then get field test
			fldd.add(test1);
			gotField = fldd.getField(test1);
			assertEquals(test1,gotField);
			
			//getting a non existent field
			gotField = null;
			Field badusr = new Field();
			gotField = fldd.getField(badusr);
			assertEquals(gotField,null);
			
			//get one field after adding multiple fields
			fldd.add(test2);
			fldd.add(test3);
			gotField = null;
			gotField = fldd.getField(test1);
			assertEquals(gotField,test1);
			
			
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
			Field test1 = new Field(1, 2,"firstname","a place","file", 3, 2);
			Field test2 = new Field(2, 5,"lastname","nothing","location", 10, 6);
			Field test3 = new Field(3, 4,"country","something","or something", 300, 23);
			
			ArrayList<Field> testUsrs = new ArrayList<Field>();
			testUsrs.add(test1);
			testUsrs.add(test2);
			testUsrs.add(test3);
			
			fldd.add(test1);
			fldd.add(test2);
			fldd.add(test3);
			
			ArrayList<Field> comp = new ArrayList<Field>();
			comp = (ArrayList<Field>) fldd.getAll();
			
			assertEquals(comp,testUsrs);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void getWithoutKnownData(){
		try{
			db.startTransaction();
			Field test1 = new Field(1, 2,"firstname","a place","file", 3, 2);
			//this is the one that would cause problems
			Field test2 = new Field(2, 5,"lastname","nothing",null, 10, 6);
			Field test3 = new Field(3, 4,"country","something","or something", 300, 23);
			Field gotField = null;
			
			fldd.add(test1);
			fldd.add(test2);
			fldd.add(test3);
			
			gotField = fldd.getField(test2);
			
			assertEquals(test2,gotField);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
}
