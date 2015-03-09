package servertester.controllers;

import java.util.*;

import client.ClientException;
import client.communication.ClientCommunicator;
import servertester.views.*;
import shared.communication.*;
import shared.model.*;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			
			getView().setRequest(username + "\n" + password + "\n");
			getView().setResponse(cc.validateUser(new ValidateUserParams(username,password)).toString());
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getProjects() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			
			getView().setRequest(username + "\n" + password + "\n");
			getView().setResponse(cc.getAllProjects(new ValidateUserParams(username,password)).toString());
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getSampleImage() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			int projectKey = Integer.parseInt(getView().getParameterValues()[2]);
			
			getView().setRequest(username + '\n' + password + '\n' + projectKey + '\n');
			GetSampleImageResult result = cc.getSampleImage((new GetSampleImageParams(username,password, projectKey)));
			if(result.isSuccessful())
				getView().setResponse(cc.getURLPrefix() + "/" + result.toString());
			else
				getView().setResponse(result.toString());
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void downloadBatch() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			int projectKey = Integer.parseInt(getView().getParameterValues()[2]);
		
			getView().setRequest(username + '\n' + password + '\n' + projectKey + '\n');
			GetBatchResult result = cc.downloadBatch((new GetBatchParams(username, password, projectKey)));
			if(result != null)
				getView().setResponse(result.toString(cc.getURLPrefix()));
			else
				getView().setResponse("FAILED\n");
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getFields() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			String projectIn = getView().getParameterValues()[2];
			int projectKey;
			
			GetFieldsResult result = null;
			if(!projectIn.equals("")){
				projectKey = Integer.parseInt(projectIn);
				getView().setRequest(username + '\n' + password + '\n' + projectKey + '\n');
				result = cc.getFields((new GetFieldsParams(username, password, projectKey)));
			}else{
				projectKey = -1;
				getView().setRequest(username + '\n' + password + '\n');
				result = cc.getFields((new GetFieldsParams(username, password)));
			}
			
			if(result != null)
				getView().setResponse(result.toString());
			else
				getView().setResponse("FAILED\n");
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void submitBatch() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			int batchID = Integer.parseInt(getView().getParameterValues()[2]);
			String recordString = getView().getParameterValues()[3];
			
			AddBatchParams params = new AddBatchParams(username, password, new Batch(batchID, "id"), recordString);
			AddBatchResult result = cc.submitBatch(params);
			if(result != null)
				getView().setResponse(result.toString());
			else
				getView().setResponse("FAILED\n");
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void search() {
		clearReqAndRes();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(), getView().getPort());
		try{
			String username = getView().getParameterValues()[0];
			String password = getView().getParameterValues()[1];
			String fieldString = getView().getParameterValues()[2];
			String searchWords = getView().getParameterValues()[3];
			
			SearchParams params = new SearchParams(username, password, fieldString, searchWords);
			SearchResult result = cc.search(params);
			if(result != null)
				getView().setResponse(result.toString());
			else
				getView().setResponse("FAILED\n");
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("FAILED\n");
		}
	}
	
	private void clearReqAndRes(){
		getView().setRequest("");
		getView().setResponse("");
	}
}

