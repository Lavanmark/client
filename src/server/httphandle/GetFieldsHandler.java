package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.GetFieldsParams;
import shared.communication.GetFieldsResult;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetFieldsHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering get fields handler");
		
		GetFieldsParams params = (GetFieldsParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User resultUser = null;
		List<Field> resultList = null;
		try {
			resultUser = ServerFacade.ValidateUser(new User(params.getUsername(), params.getPassword()));
			if(resultUser != null){
				if(params.isAllFields())
					resultList = ServerFacade.GetFields(new Project(-1));
				else if(params.getProjectID() != -1)
					resultList = ServerFacade.GetFields(new Project(params.getProjectID()));
			}
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		GetFieldsResult resultToSend;
		if(resultUser != null){
			if(resultList != null)
				resultToSend = new GetFieldsResult(resultList);
			else
				resultToSend = new GetFieldsResult();
		}else
			resultToSend = new GetFieldsResult();
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		
		logger.info("exiting get fields handler");
	}
}
