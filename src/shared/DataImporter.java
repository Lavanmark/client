package shared;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.commons.io.FileUtils;

import server.database.Database;
import server.database.DatabaseException;
import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import shared.model.Record;
import shared.model.User;

public class DataImporter {

	private Database db;
	private File recordXML;
	
	
	
	/**
	 * Imports record indexer data from xml file
	 * @param fileLoc
	 */
	public DataImporter(String fileLoc){
		//Create the database
		db = new Database();
		
		//Clear the database
		clearDatabase();
		
		//parse the files
		tryParsing(fileLoc);
		
		//copy the files to server locations
		copyFilesOver();
	}
	
	private void clearDatabase(){
		try {
			Database.Initialize();
			db.startTransaction();
			db.createDatabase();
			db.endTransaction(true);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private void tryParsing(String fileLoc){
		recordXML = new File(fileLoc);
		try{
			db.startTransaction();
			parseXML();
			db.endTransaction(true);
		}catch(SAXException | IOException | ParserConfigurationException e){
			e.printStackTrace();
			db.endTransaction(false);
		}catch(DatabaseException e){
			e.printStackTrace();
			db.endTransaction(false);
		}
	}
	
	private void copyFilesOver(){
		File copyFrom = recordXML.getParentFile();
		File copyTo = new File("./Data/Records");
		if(copyFrom.isDirectory() && copyTo.isDirectory()){
			try {
				//delete any existing directories where we are moving files
				File[] deleteOld = copyTo.listFiles();
				for(int i = 0; i < deleteOld.length; i++){
					if(deleteOld[i].isDirectory())
						FileUtils.deleteDirectory(deleteOld[i]);
					else if(deleteOld[i].isFile())
						FileUtils.forceDelete(deleteOld[i]);
				}
				
				//copy directory information over to new place
				File[] copyFromFiles = copyFrom.listFiles();
				for(int i = 0; i < copyFromFiles.length; i++){
					if(copyFromFiles[i].isDirectory())
						FileUtils.copyDirectoryToDirectory(copyFromFiles[i], copyTo);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("error");
		}
	}
	
	
	/**
	 * Parses through an xml file
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DatabaseException
	 */
	private void parseXML() throws SAXException, IOException, ParserConfigurationException, DatabaseException{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc = builder.parse(recordXML);
		
		NodeList userList = doc.getElementsByTagName("user");
		buildUsers(userList);
		NodeList projectList = doc.getElementsByTagName("project");
		buildProjects(projectList);
	}
	
	/**
	 * builds users from nodeList we will make in the parse xml
	 * @param userList
	 * @throws DatabaseException
	 */
	private void buildUsers(NodeList userList) throws DatabaseException{
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < userList.getLength(); ++i) {
			
			Element userElem = (Element)userList.item(i);
			
			Element usernameElem = (Element)userElem.getElementsByTagName("username").item(0);
			Element passwordElem = (Element)userElem.getElementsByTagName("password").item(0);
			Element firstNameElem = (Element)userElem.getElementsByTagName("firstname").item(0);
			Element lastNameElem = (Element)userElem.getElementsByTagName("lastname").item(0);
			Element emailElem = (Element)userElem.getElementsByTagName("email").item(0);
			Element recordsElem = (Element)userElem.getElementsByTagName("indexedrecords").item(0);

			String username = usernameElem.getTextContent();
			String password = passwordElem.getTextContent();
			String firstName = firstNameElem.getTextContent();
			String lastName = lastNameElem.getTextContent();
			String email = emailElem.getTextContent();
			int indexedRecords = Integer.parseInt(recordsElem.getTextContent());
			
			users.add(new User(username,password,firstName,lastName,email,indexedRecords, -1));
			db.getUserDAO().add(users.get(i));
		}
	}
	
	/**
	 * builds projects from nodeList in the parse xml
	 * @param projectList
	 * @throws DatabaseException
	 */
	private void buildProjects(NodeList projectList) throws DatabaseException{
		List<Project> projects = new ArrayList<Project>();
		for (int i = 0; i < projectList.getLength(); ++i) {
			
			Element projectElem = (Element)projectList.item(i);
			
			Element titleElem = (Element)projectElem.getElementsByTagName("title").item(0);
			Element rpImageElem = (Element)projectElem.getElementsByTagName("recordsperimage").item(0);
			Element firstYCoordElem = (Element)projectElem.getElementsByTagName("firstycoord").item(0);
			Element recordHeightElem = (Element)projectElem.getElementsByTagName("recordheight").item(0);
			
			String title = titleElem.getTextContent();
			int rpImage = Integer.parseInt(rpImageElem.getTextContent());
			int firstYCoord = Integer.parseInt(firstYCoordElem.getTextContent());
			int recordHeight = Integer.parseInt(recordHeightElem.getTextContent());
			
			projects.add(new Project(rpImage, firstYCoord, recordHeight, title));
			db.getProjectDAO().add(projects.get(i));
			
			buildFields(projects.get(i).getKey(), projectElem.getElementsByTagName("field"));
			buildBatches(projects.get(i).getKey(), projectElem.getElementsByTagName("image"));
		}
	}
	
	/**
	 * builds batches from projects
	 * @param projectKey
	 * @param batchList
	 * @throws DatabaseException
	 */
	private void buildBatches(int projectKey, NodeList batchList) throws DatabaseException{
		List<Batch> batches = new ArrayList<Batch>();
		for (int i = 0; i < batchList.getLength(); ++i) {
			
			Element batchElem = (Element)batchList.item(i);
			
			Element fileElem = (Element)batchElem.getElementsByTagName("file").item(0);
						
			String imgURL = fileElem.getTextContent();
			
			batches.add(new Batch(projectKey, imgURL, 0));
			db.getBatchDAO().add(batches.get(i));
			buildRecords(projectKey, batches.get(i).getId(), batchElem.getElementsByTagName("record"));
		}
	}
	
	/**
	 * builds fields from projects
	 * @param projectKey
	 * @param fieldList
	 * @throws DatabaseException
	 */
	public void buildFields(int projectKey, NodeList fieldList) throws DatabaseException{
		List<Field> fields = new ArrayList<Field>();
		for (int i = 0; i < fieldList.getLength(); ++i) {
			
			Element fieldElem = (Element)fieldList.item(i);
			
			Element titleElem = (Element)fieldElem.getElementsByTagName("title").item(0);
			Element xCoordElem = (Element)fieldElem.getElementsByTagName("xcoord").item(0);
			Element widthElem = (Element)fieldElem.getElementsByTagName("width").item(0);
			Element helpHtmlElem = (Element)fieldElem.getElementsByTagName("helphtml").item(0);
			Element knownDataElem = (Element)fieldElem.getElementsByTagName("knowndata").item(0);
			
			String title = titleElem.getTextContent();
			int xCoord = Integer.parseInt(xCoordElem.getTextContent());
			int width = Integer.parseInt(widthElem.getTextContent());
			String helpHtml = helpHtmlElem.getTextContent();
			
			if(knownDataElem != null){
				String knownData = knownDataElem.getTextContent();
				fields.add(new Field(projectKey, i+1, title, helpHtml, knownData, xCoord, width));
			}else{
				fields.add(new Field(projectKey, i+1, title, helpHtml, xCoord, width));
			}
			
			db.getFieldDAO().add(fields.get(i));
		}
	}
	
	/**
	 * builds records from batches
	 * @param projectKey
	 * @param batchID
	 * @param recordList
	 * @throws DatabaseException
	 */
	private void buildRecords(int projectKey, int batchID, NodeList recordList) throws DatabaseException{
		if(recordList != null){
			List<Record> records = new ArrayList<Record>();
			List<Field> fields = db.getFieldDAO().getProjectsFields(new Field(projectKey, "project"));
			for (int i = 0; i < recordList.getLength(); ++i) {
				
				Element recordElem = (Element)recordList.item(i);
				
				NodeList valueList = recordElem.getElementsByTagName("value");
				for(int f = 0; f < fields.size(); f++){
					int order = fields.get(f).getRecordOrder();
					Element valueElem = (Element)valueList.item(order-1);
					
					String data = valueElem.getTextContent();
					records.add(new Record(i+1, projectKey, batchID, fields.get(f).getId(), data));
					db.getRecordDAO().add(records.get(((i+1)*(f+1))-1));
				}
			}
		}
	}
	
	public static void main(String[] args){
		if(args.length > 0)
			new DataImporter(args[0]);
		else
			System.out.println("PLEASE specify a file location");
	}
}
