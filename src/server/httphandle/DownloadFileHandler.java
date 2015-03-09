package server.httphandle;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DownloadFileHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	
	
	
	/**
	 * Handle HttpExchange from client
	 */
	@Override
	public void handle(HttpExchange httpexchange) throws IOException {
		logger.info("entering download file handler");
		logger.info(httpexchange.getRequestURI().getPath());
		
		File fileToSend = new File("./Data/Records" + httpexchange.getRequestURI().getPath());
		
		if(fileToSend.exists()){
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			Files.copy(fileToSend.toPath(), httpexchange.getResponseBody());
			httpexchange.getResponseBody().close();
			
			logger.info("exiting download file handler OK");
		}else{
			httpexchange.sendResponseHeaders(HttpURLConnection.HTTP_NO_CONTENT, -1);
			
			logger.info("exiting download file handler NO FILE");
		}	
	}
}
