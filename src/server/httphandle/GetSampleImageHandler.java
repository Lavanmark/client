package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.GetSampleImageParams;
import shared.communication.GetSampleImageResult;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetSampleImageHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering get sample image handler");
		
		GetSampleImageParams params = (GetSampleImageParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User resultUser = null;
		String resultURL = null;
		try {
			resultUser = ServerFacade.ValidateUser(new User(params.getUsername(), params.getPassword()));
			if(resultUser != null)
				resultURL = ServerFacade.GetSampleImage(params.getProjectID());
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		GetSampleImageResult resultToSend;
		if(resultUser != null){
			if(resultURL != null)
				resultToSend = new GetSampleImageResult(resultURL);
			else
				resultToSend = new GetSampleImageResult();
		}else
			resultToSend = new GetSampleImageResult();
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		
		logger.info("exiting get sample image handler");
	}
}
