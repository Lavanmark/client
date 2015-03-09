package server.facade;

import java.util.ArrayList;
import java.util.List;
import server.ServerException;
import server.database.Database;
import server.database.DatabaseException;
import shared.communication.GetBatchResult;
import shared.communication.SearchTuple;
import shared.model.*;


public class ServerFacade {

	/**
	 * Initializes Database
	 * @throws ServerException
	 */
	public static void initialize() throws ServerException {		
		try{
			Database.Initialize();		
		}catch(DatabaseException e){
			throw new ServerException(e.getMessage(), e);
		}			
	}
	
	/**
	 * Validates a user based on username and password
	 * @param user
	 * @return Validated user
	 * @throws ServerException
	 */
	public static User ValidateUser(User user) throws ServerException{
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
	public static List<Project> GetAllProjects() throws ServerException{
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
	 * Gets a sample image based on the project key
	 * @param projectKey
	 * @return the image url requested
	 * @throws ServerException
	 */
	public static String GetSampleImage(int projectKey) throws ServerException{
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
				return null;
		}catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * finds a batch based on the user and project key
	 * @param user
	 * @param projectKey
	 * @return the found batch
	 * @throws ServerException
	 */
	public static GetBatchResult DownloadBatch(User user, int projectKey) throws ServerException{
		Database db = new Database();
		
		if(user.getCurrentBatch() == -1){
			try{
				db.startTransaction();
				
				//If project entered exists
				Project resultProject = db.getProjectDAO().getProject(new Project(projectKey));
				if(resultProject != null){
					
					//get all batches for specific project
					List<Batch> resultBatches = db.getBatchDAO().getProjectsBatch(new Batch(projectKey,"project"));
					
					//if there were any batches
					if(resultBatches.size() > 0){
						
						//go through all batches till you find an unused one
						for(int i = 0; i < resultBatches.size(); i++){
							
							if(resultBatches.get(i).getStatus() == 0){
								GetBatchResult resultToReturn;
								
								//Update the batches state
								resultBatches.get(i).setStatus(1);
								db.getBatchDAO().update(resultBatches.get(i));
								
								//Update the users current batch
								user.setCurrentBatch(resultBatches.get(i).getId());
								db.getUserDAO().update(user);
								
								//gets a list of all fields for the project
								List<Field> fields = db.getFieldDAO().getProjectsFields(new Field(projectKey,"project"));
								
								//commit the changes
								db.endTransaction(true);
								
								resultToReturn = new GetBatchResult(resultBatches.get(i), resultProject, fields);
								//return the batch
								return resultToReturn;
							}
						}
					}
						//there weren't any batches in existence or available
						db.endTransaction(false);
						return null;
				}
				
				//There were no projects
				db.endTransaction(false);
				return null;
			}catch(DatabaseException e){
				//something bad happened
				e.printStackTrace();
				db.endTransaction(false);
				return null;
			}
		}
		//if the user has an active batch 
		return null;
	}
	
	/**
	 * Inputs batch to the database
	 * @param batchToSubmit
	 * @return true if submitted false otherwise
	 * @throws ServerException
	 */
	public static boolean SubmitBatch(Batch batchToSubmit, String recordData, User user) throws ServerException{
		Database db = new Database();
		try{
			db.startTransaction();
			Batch batchToUpdate = db.getBatchDAO().getBatch(batchToSubmit);
			if(batchToUpdate != null && batchToUpdate.getStatus() == 1){
				//build list of records
				List<Record> recordsToSubmit = ParseRecordData(recordData, batchToUpdate, db);
				
				//add records
				for(int i = 0; i < recordsToSubmit.size(); i++){
					db.getRecordDAO().add(recordsToSubmit.get(i));
				}
				
				//update batch
				batchToUpdate.setStatus(2);
				db.getBatchDAO().update(batchToUpdate);
				
				//update user
				user.setCurrentBatch(0);
				Project batchesProject = db.getProjectDAO().getProject(new Project(batchToUpdate.getProjectKey()));
				if(batchesProject == null){
					//if the project was null it wasn't there and none of this should've happened...
					db.endTransaction(false);
					return false;
				}
				int recordsJustIndexed = batchesProject.getRecordsPerImage();
				user.setRecordsIndexed(user.getRecordsIndexed()+recordsJustIndexed);
				db.getUserDAO().update(user);
				
				//end and return true
				db.endTransaction(true);
				return true;
			}else{
				db.endTransaction(false);
				return false;
			}
		}catch(DatabaseException e){
			e.printStackTrace();
			db.endTransaction(false);
			return false;
		}catch(Exception e){
			e.printStackTrace();
			db.endTransaction(false);
			return false;
		}
	}
	
	private static List<Record> ParseRecordData(String recordData, Batch batch, Database db) throws DatabaseException{
		List<Field> fields = db.getFieldDAO().getProjectsFields(new Field(batch.getProjectKey(),"project"));
		String[] outter = recordData.split(";");
		List<Record> resultList = new ArrayList<Record>();
		for(int i = 0; i < outter.length; i++){
			String[] inner = outter[i].split(",");
			for(int k = 0; k < inner.length; k++){
				resultList.add(new Record(batch.getProjectKey(),batch.getId(),fields.get(k).getId(),inner[k]));
			}
		}
		return resultList;
	}
	
	/**
	 * Makes a list of all fields in a project
	 * @param fieldsProject
	 * @return List of Fields
	 * @throws ServerException
	 */
	public static List<Field> GetFields(Project fieldsProject) throws ServerException{
		Database db = new Database();
		try{
			List<Field> resultFields = null;
			if(fieldsProject.getKey() == -1){
				db.startTransaction();
				resultFields = db.getFieldDAO().getAll();
				db.endTransaction(false);
			}else{
				Field fieldParam = new Field(fieldsProject.getKey(), "project");
				db.startTransaction();
				resultFields = db.getFieldDAO().getProjectsFields(fieldParam);
				db.endTransaction(false);
			}
			if(resultFields != null){
				if(resultFields.size() > 0)
					return resultFields;
				else
					return null;
			}else
				return null;
		}catch(DatabaseException e){
			//something bad happened
			e.printStackTrace();
			db.endTransaction(false);
			return null;
		}
	}
	
	/**
	 * Searches the Records for the searchword
	 * @param searchWord
	 * @return record containing searchword
	 * @throws ServerException
	 */
	public static List<SearchTuple> Search(String fieldsToSearch, String searchWord) throws ServerException{
		Database db = new Database();
		try{
			db.startTransaction();
			
			List<SearchTuple> resultList = new ArrayList<SearchTuple>();
			List<Field> fields = ParseFieldsToSearch(fieldsToSearch,db);
			List<String> wordList = ParseSearchWords(searchWord);
			List<Record> records = db.getRecordDAO().getAll();
			
			if(fields == null){
				System.out.println("failed fields null");
				db.endTransaction(false);
				return null;
			}
			
			//go through all fields
			for(int f = 0; f < fields.size(); f++){
				//go through all records
				for(int r = 0; r < records.size(); r++){
					//if records field is same as the field
					
					if(records.get(r).getFieldID() == fields.get(f).getId()){
						//go through all the search words
						for(int w = 0; w < wordList.size(); w++){
							//if the records data equals the search word
							if(records.get(r).getData().toLowerCase().equals(wordList.get(w).toLowerCase())){
								//add a search tuple of the data to the list
								resultList.add(new SearchTuple(records.get(r).getImageKey(),
										db.getBatchDAO().getBatch(new Batch(records.get(r).getImageKey(),"id")).getImgFile(),
										records.get(r).getRecordNum(),
										records.get(r).getFieldID()));
							}
						}
					}
				}
			}
			db.endTransaction(false);
			return resultList;
		}catch(DatabaseException e){
			e.printStackTrace();
			db.endTransaction(false);
			return null;
		}
	}
	
	private static List<Field> ParseFieldsToSearch(String fieldsToSearch, Database db) throws DatabaseException{
		String[] outter = fieldsToSearch.split(",");
		List<Field> fieldsToReturn = new ArrayList<Field>();
		try{
			for(int i = 0; i < outter.length; i++){
				Field addme = db.getFieldDAO().getField(new Field(Integer.parseInt(outter[i]),"id"));
				if(addme != null)
					fieldsToReturn.add(addme);
				else
					return null;
			}
			return fieldsToReturn;
		}catch(DatabaseException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<String> ParseSearchWords(String searchWord){
		String[] outter = searchWord.split(",");
		List<String> wordList = new ArrayList<String>();
		for(int i = 0; i < outter.length; i++){
			wordList.add(outter[i]);
		}
		return wordList;
	}
}
