package servertester.controllers;

import java.util.*;

import client.ClientException;
import client.communication.ClientCommunicator;
import servertester.views.*;
import shared.communication.GetSampleImageParams;
import shared.communication.ValidateUserParams;

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
			getView().setResponse("Failed\n");
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
			getView().setResponse("Failed\n");
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
			//TODO make this so that it adds http and host name and port
			getView().setResponse(cc.getSampleImage((new GetSampleImageParams(username,password, projectKey))).toString());
		}catch(ClientException e){
			if(e.getCause() != null)
				System.err.println(e.getCause().toString());
			else
				e.printStackTrace();
			getView().setResponse("Failed\n");
		}
	}
	
	private void downloadBatch() {
		
	}
	
	private void getFields() {
		
	}
	
	private void submitBatch() {
		
	}
	
	private void search() {
		
	}
	
	private void clearReqAndRes(){
		getView().setRequest("");
		getView().setResponse("");
	}
}

