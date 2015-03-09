package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.facade.ServerFacade;
import server.httphandle.*;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

	private static  int SERVER_PORT_NUMBER = 39640;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	static {
		try {
			InitLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void InitLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("recordindexer"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	private HttpServer server;
	
	
	
	
	private Server() {
		return;
	}
	
	private void run() {
		
		logger.info("Initializing Model");
		
		try{
			ServerFacade.initialize();		
		}catch(ServerException e){
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try{
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
		}catch(IOException e){
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/ValidateUser", this.validateUserHandler);
		server.createContext("/GetAllProjects", this.getProjectsHandler);
		server.createContext("/GetSampleImage", this.getSampleImageHandler);
		server.createContext("/DownloadBatch", this.downloadBatchHandler);
		server.createContext("/SubmitBatch", this.submitBatchHandler);
		server.createContext("/GetFields", this.getFieldsHandler);
		server.createContext("/Search", this.searchHandler);
		server.createContext("/", this.downloadFileHandler);
		
		
		logger.info("Starting HTTP Server");

		server.start();
		
		logger.info("Server Hosted at: " + server.getAddress().getHostName());
		logger.info("Server Running on Port: " + SERVER_PORT_NUMBER);

	}

	private HttpHandler validateUserHandler = new ValidateUserHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler getSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler submitBatchHandler = new SubmitBatchHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler searchHandler = new SearchHandler();
	private HttpHandler downloadFileHandler = new DownloadFileHandler();
	
	public static void main(String[] args) {
		if(args.length > 0)
			SERVER_PORT_NUMBER = Integer.parseInt(args[0]);
		new Server().run();
	}
	
}
