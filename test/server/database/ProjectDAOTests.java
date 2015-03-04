package server.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.model.Project;

public class ProjectDAOTests {

	private Database db;
	private ProjectDAO prjd;
	
	@Before
	public void setup() {
		db = new Database();
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
			return;
		}
		prjd = db.getProjectDAO();
		
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void addTest() {
		try{
			db.startTransaction();
			
			Project test1 = new Project(1,2,3,"ProjUno");
			Project test2 = new Project(1,2,3,"ProjDos");
			Project test3 = new Project(2,2,3,"ProjTres");
			Project gotProject = null;
			
			//basic add then get Project test
			prjd.add(test1);
			gotProject = prjd.getProject(test1);
			assertEquals(test1,gotProject);
			
			//getting a non existent Project
			gotProject = null;
			Project badprj = new Project(3,1,2,"Proj");
			gotProject = prjd.getProject(badprj);
			assertEquals(gotProject,null);
			
			//get one Project after adding multiple Projects
			prjd.add(test2);
			prjd.add(test3);
			gotProject = null;
			gotProject = prjd.getProject(test1);
			assertEquals(gotProject,test1);
			
			
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
			
			Project test1 = new Project(1,2,3,"ProjUno");;
			
			//add user and then add again hopefully throwing exception
			prjd.add(test1);
				//this should catch because of column email
			prjd.add(test1);
			
			//if this happens we failed
			assertFalse(true);
			
		}catch(DatabaseException e){
			if(e.getCause() != null)
				assertTrue(e.getCause().getLocalizedMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column title is not unique)"));
			else
				assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
	@Test
	public void getAllTest(){
		try{
			db.startTransaction();

			Project test1 = new Project(1,2,3,"ProjUno");
			Project test2 = new Project(1,2,3,"ProjDos");
			Project test3 = new Project(2,2,3,"ProjTres");
			
			ArrayList<Project> testRcrds = new ArrayList<Project>();
			testRcrds.add(test1);
			testRcrds.add(test2);
			testRcrds.add(test3);
			
			prjd.add(test1);
			prjd.add(test2);
			prjd.add(test3);
			
			ArrayList<Project> comp = new ArrayList<Project>();
			comp = (ArrayList<Project>) prjd.getAll();
			
			assertEquals(comp,testRcrds);
			
		}catch(DatabaseException e){
			assertFalse(true);
		}finally{
			db.endTransaction(false);
		}
	}
	
}
