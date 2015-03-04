package client.communication;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import shared.communication.*;
import client.ClientException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ClientCommunicator {

	private static String SERVER_HOST;
	private static int SERVER_PORT;
	private static String URL_PREFIX;
	private static final String HTTP_POST = "POST";
	
	
	private XStream xmlStream;
	
	/**
	 * Initializes the xml stream
	 */
	public ClientCommunicator(){
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST = "localhost";
		SERVER_PORT = 0;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	
	public ClientCommunicator(String host, String port){
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST = host;
		SERVER_PORT = Integer.parseInt(port);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	/**
	 * Validates the users information
	 * @return Validate User Result object
	 * @throws ClientException
	 */
	public ValidateUserResult validateUser(ValidateUserParams params) throws ClientException{
		return (ValidateUserResult)doPost("/ValidateUser", params);
	}
	/**
	 * Gets all the projects in the database
	 * @return Get all project result object
	 * @throws ClientException
	 */
	public GetAllProjectResult getAllProjects(ValidateUserParams params) throws ClientException{
		return (GetAllProjectResult)doPost("/GetAllProjects", params);
	}
	/**
	 * Based on parameters it will get a sample image
	 * @param params
	 * @return Get Sample Image Result object
	 * @throws ClientException
	 */
	public GetSampleImageResult getSampleImage(GetSampleImageParams params) throws ClientException{
		return (GetSampleImageResult)doPost("/GetSampleImage", params);
	}
	/**
	 * Based on parameters it will download a batch
	 * @param params
	 * @return Get batch result object (the downloaded batch)
	 * @throws ClientException
	 */
	public GetBatchResult downloadBatch(GetBatchParams params) throws ClientException{
		return (GetBatchResult)doPost("/DownloadBatch", params);
	}
	/**
	 * Based on parameters it will submit a batch to be updated to database
	 * @param params
	 * @return Add Batch Result object (true/false)
	 * @throws ClientException
	 */
	public AddBatchResult submitBatch(AddBatchParams params) throws ClientException{
		return (AddBatchResult)doPost("/SubmitBatch",params);
	}
	/**
	 * Based on parameters it gets all the fields of a project
	 * @param params
	 * @return Get Fields Result object
	 * @throws ClientException
	 */
	public GetFieldsResult getFields(GetFieldsParams params) throws ClientException{
		return (GetFieldsResult)doPost("/GetFields",params);
	}
	/**
	 * Based on parameters it searches the database and return result
	 * @param params
	 * @return Search result object
	 * @throws ClientException
	 */
	public SearchResult search(SearchParams params) throws ClientException{
		return (SearchResult)doPost("/Search",params);
	}
	/**
	 * Based on parameters it downloads a file from server
	 * @param params
	 * @return Download file result object
	 * @throws ClientException
	 */
	public DownloadFileResult downloadFile(DownloadFileParams params) throws ClientException{
		return (DownloadFileResult)doPost("/DownloadFile",params);
	}
	
	/**
	 * Creates a http Connection and sends object to server
	 * @param urlPath
	 * @param postData
	 * @throws ClientException
	 */
	private Object doPost(String urlPath, Object postData) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}else{
				throw new ClientException(String.format("doPost failed: %s (http code %d)", urlPath, connection.getResponseCode()));
			}
		}catch(IOException e){
			throw new ClientException(String.format("doPost failed: %s", e.getMessage()), e);
		}
	}
	
}
