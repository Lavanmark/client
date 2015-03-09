package server.httphandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.facade.ServerFacade;
import shared.communication.SearchParams;
import shared.communication.SearchResult;
import shared.communication.SearchTuple;
import shared.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering search handler");
		
		SearchParams params = (SearchParams)xmlStream.fromXML(httpexchange.getRequestBody());
		User resultUser = null;
		SearchResult resultToSend = null;
		try {
			resultUser = ServerFacade.ValidateUser(new User(params.getUsername(), params.getPassword()));
			if(resultUser != null){
				Set<SearchTuple> srchResult = ServerFacade.Search(params.getFields(), params.getSearchwords());
				if(srchResult != null)
					resultToSend = new SearchResult(srchResult);
				else
					resultToSend = new SearchResult();
			}else
				resultToSend = new SearchResult();
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
			
		httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(resultToSend, httpexchange.getResponseBody());
		httpexchange.getResponseBody().close();
		
		logger.info("exiting search handler");
	}
}
