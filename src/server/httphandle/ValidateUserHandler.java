package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.ValidateUserParams;
import shared.communication.ValidateUserResult;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ValidateUserHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering validate user handler");
		ValidateUserParams params = (ValidateUserParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User usr = new User(params.getUsername(), params.getPassword());
		User resultUser = null;
		try {
			resultUser = ServerFacade.validateUser(usr);
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		ValidateUserResult resultToSend;
		if(resultUser != null)
			resultToSend = new ValidateUserResult(true, resultUser);
		else
			resultToSend = new ValidateUserResult(false);
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		logger.info("exiting validate user handler");
	}

}
