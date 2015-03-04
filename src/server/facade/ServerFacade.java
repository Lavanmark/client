package server.facade;

import java.io.File;
import java.util.List;

import server.ServerException;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.*;


public class ServerFacade {

	/**
	 * Initializes Database
	 * @throws ServerException
	 */
	public static void initialize() throws ServerException {		
		try{
			Database.initialize();		
		}catch(DatabaseException e){
			throw new ServerException(e.getMessage(), e);
		}			
	}
	
	/**
	 * Validates a user based on username and password
	 * @param username
	 * @param password
	 * @return Validated user object
	 * @throws ServerException
	 */
	public static User validateUser(User user) throws ServerException{
		Database db = new Database();
		try{
			db.startTransaction();
			User result = db.getUserDAO().getUser(user);
			db.endTransaction(true);
			return result;
		}catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Gets all the projects 
	 * @return List of Projects
	 * @throws ServerException
	 */
	public static List<Project> getAllProjects() throws ServerException{
		Database db = new Database();
		try{
			db.startTransaction();
			List<Project> result = db.getProjectDAO().getAll();
			db.endTransaction(true);
			return result;
		}catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Gets a sample image based on the string
	 * @param imageFileLocation
	 * @return the image requested
	 * @throws ServerException
	 */
	public static String getSampleImage(int projectKey) throws ServerException{
		Database db = new Database();
		try{
			db.startTransaction();
			Batch bchparam = new Batch();
			bchparam.setProjectKey(projectKey);
			List<Batch> result = db.getBatchDAO().getProjectsBatch(bchparam);
			db.endTransaction(true);
			if(result.size() > 0)
				return result.get(0).getImgFile();
			else
				return "";
		}catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	/**
	 * finds a batch based on the string
	 * @param whichBatch
	 * @return the found batch
	 * @throws ServerException
	 */
	public Batch downloadBatch(String whichBatch) throws ServerException{
		return null;
	}
	/**
	 * Inputs batch to the database
	 * @param batchToSubmit
	 * @return true if submitted false otherwise
	 * @throws ServerException
	 */
	public boolean submitBatch(Batch batchToSubmit) throws ServerException{
		return false;
	}
	/**
	 * Makes a list of all fields in a project
	 * @param fieldsProject
	 * @return List of Fields
	 * @throws ServerException
	 */
	public List<Field> getFields(Project fieldsProject) throws ServerException{
		return null;
	}
	/**
	 * Searches the Records for the searchword
	 * @param searchWord
	 * @return record containing searchword
	 * @throws ServerException
	 */
	public Record search(String searchWord) throws ServerException{
		return null;
	}
	/**
	 * Gets file based on file location
	 * @param fileLocation
	 * @return File that was requested
	 * @throws ServerException
	 */
	public File downloadFile(String fileLocation) throws ServerException{
		return null;
	}
}
