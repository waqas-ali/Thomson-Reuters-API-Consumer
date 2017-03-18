package com.fx.database;

import java.io.*;

import java.sql.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.logging.*;

public class DatabaseConnectivity {

	public static Logger log = Logger.getLogger(DatabaseConnectivity.class
			.getName());

	private static final String CREDENTIAL_FILE_NAME = "credentials.xml";
	private static Connection connection = null;
	private static String driverName = "";
	private static String userName = "";
	private static String userPassword = "";
	private static String url = "";

	public DatabaseConnectivity() {
		createConnection();
	}

	public void createConnection() {
		try {
			if (connection == null) {
				initCredentials();
				Class.forName(driverName).newInstance();
				connection = DriverManager.getConnection(url, userName,
						userPassword);
				log.info("new connection has been created");
			} else {
				log.info("connection already exists");
			}
		} catch (Exception e) {
			log.warning("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
			log.info("connection has been closed");
		}
	}

	private static void initCredentials() throws JDOMException, IOException {

		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(new File(CREDENTIAL_FILE_NAME));
		Element root = doc.getRootElement();
		Element em = root.getChild("database");
		driverName = em.getChild("driver-name").getText();
		userName = em.getChild("user-name").getText();
		userPassword = em.getChild("user-password").getText();
		url = em.getChild("url").getText();

		log.info("XML file parsing has been completed");

	}

	public void insertQuery(String query) throws Exception {
		log.info("Query to be executed : " + query);
		PreparedStatement pStmt = connection.prepareStatement(query);
		int i = pStmt.executeUpdate();
		pStmt.close();
		log.info("Row inserted : " + i);

	}

	public int insertQueryWithKey(String query) throws Exception {
		log.info("Query to be executed : " + query);
		PreparedStatement pStmt = connection.prepareStatement(query,
				Statement.RETURN_GENERATED_KEYS);
		int i = pStmt.executeUpdate();
		log.info("Row inserted : " + i);
		ResultSet rs = pStmt.getGeneratedKeys();
		int value = -1;
		if (rs.next()) {
			value = rs.getInt(1);
		}
		rs.close();
		pStmt.close();
		return value;

	}

	public ResultSet getData(String query) throws Exception {
		log.info("Query : " + query);
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery(query);
		return res;
	}

}
