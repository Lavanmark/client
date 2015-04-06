package client.backend.facade;

import java.util.List;

import shared.communication.AddBatchParams;
import shared.communication.AddBatchResult;
import shared.communication.GetAllProjectResult;
import shared.communication.GetBatchParams;
import shared.communication.GetBatchResult;
import shared.communication.GetFieldsParams;
import shared.communication.GetFieldsResult;
import shared.communication.GetSampleImageParams;
import shared.communication.GetSampleImageResult;
import shared.communication.SearchParams;
import shared.communication.SearchResult;
import shared.communication.SearchTuple;
import shared.communication.ValidateUserParams;
import shared.communication.ValidateUserResult;
import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;
import client.ClientException;
import client.backend.BatchState;
import client.backend.LoginState;
import client.communication.ClientCommunicator;

public class ClientFacade {

	private static ClientCommunicator cc;
		
	//TODO finish adding all of these
	
	
	
	/**
	 * 
	 * @param host
	 * @param port
	 */
	public static void initialize(String host, String port){
		cc = new ClientCommunicator(host, port);
	}
	
	public static String getURLPrefix(){
		return cc.getURLPrefix();
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static User validateUser(String username, String password) throws ClientException{
		ValidateUserParams params = new ValidateUserParams(username, password);
		
		ValidateUserResult result = cc.validateUser(params);
		if(result == null)
			throw new ClientException("Connection failed!");
		if(result.isValid())
			return result.getUsr();
		else
			return null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientException
	 */
	public static List<Project> getAllProjects(String username, String password) throws ClientException{
		ValidateUserParams params = new ValidateUserParams(username, password);
		
		GetAllProjectResult result = cc.getAllProjects(params);
		
		if(result == null)
			throw new ClientException("Connection failed!");
		if(result.isAllIsWell())
			return result.getProjects();
		else
			return null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param projectKey
	 * @return
	 * @throws ClientException
	 */
	public static List<Field> getFields(String username, String password, int projectKey) throws ClientException{
		GetFieldsParams params;
		if(projectKey != -1)
			params = new GetFieldsParams(username, password, projectKey);
		else
			params = new GetFieldsParams(username, password);
		
		GetFieldsResult result = cc.getFields(params);
		
		if(result == null)
			throw new ClientException("Connection failed!");
		if(result.isSuccessful())
			return result.getFields();
		else
			return null;
	}
	
	public static String getSampleImage(String username, String password, int projectKey) throws ClientException{
		GetSampleImageParams params = new GetSampleImageParams(username,password, projectKey);
		
		GetSampleImageResult result = cc.getSampleImage(params);
		
		if(result.isSuccessful())
			return result.getImageurl();
		else
			return null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param fields
	 * @param searchWords
	 * @return
	 * @throws ClientException
	 */
	public static List<SearchTuple> search(String username, String password, String fields, String searchWords) throws ClientException{
		SearchParams params = new SearchParams(username, password, fields, searchWords);
		
		SearchResult result = cc.search(params);
		
		if(result == null)
			throw new ClientException("Connection failed!");
		if(result.isSuccessful())
			return result.getSearchTuples();
		else
			return null;
	}
	
	public static GetBatchResult downloadBatch(String username, String password, int projectKey) throws ClientException{
		GetBatchParams params = new GetBatchParams(username,password, projectKey);
		
		GetBatchResult result = cc.downloadBatch(params);
		
		if(result == null)
			throw new ClientException("Result was null!!");
		
		if(result.isSuccessful()){
			return result;
		}
		
		
		return null;
	}
	
	public static boolean submitBatch() throws ClientException{
		User user = LoginState.getUserOn();
		
		AddBatchParams params = new AddBatchParams(user.getUsername(), user.getPassword(), new Batch(user.getCurrentBatch(),"id"), BatchState.submitBatchString());
		
		AddBatchResult result = cc.submitBatch(params);
		
		if(result.hasSucceded())
			return true;
		return false;
	}
}
