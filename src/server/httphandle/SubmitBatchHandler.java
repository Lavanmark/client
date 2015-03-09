package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.AddBatchParams;
import shared.communication.AddBatchResult;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SubmitBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering submit batch handler");
		
		AddBatchParams params = (AddBatchParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User resultUser = null;
		AddBatchResult resultToSend = null;
		try {
			resultUser = ServerFacade.ValidateUser(new User(params.getUsername(), params.getPassword()));
			if(resultUser != null)
				resultToSend = new AddBatchResult(ServerFacade.SubmitBatch(params.getBatch(),params.getRecordData(), resultUser));
			else
				resultToSend = new AddBatchResult();
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		
		logger.info("exiting submit batch handler");
	}
}
