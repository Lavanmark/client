package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.GetAllProjectResult;
import shared.communication.ValidateUserParams;
import shared.model.Project;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetProjectsHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering get projects handler");
		
		ValidateUserParams params = (ValidateUserParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User resultUser = null;
		List<Project> resultList = null;
		try {
			resultUser = ServerFacade.ValidateUser(new User(params.getUsername(), params.getPassword()));
			if(resultUser != null)
				resultList = ServerFacade.GetAllProjects();
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		GetAllProjectResult resultToSend;
		if(resultUser != null)
			resultToSend = new GetAllProjectResult(resultList);
		else
			resultToSend = new GetAllProjectResult();
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		
		logger.info("exiting get projects handler");
	}
}
