package com.fx.database;

import java.sql.ResultSet;
import java.util.logging.Logger;

public class DatabaseHandler {
	public static Logger log = Logger
			.getLogger(DatabaseHandler.class.getName());

	private DatabaseConnectivity connection;

	public DatabaseHandler() {
		connection = new DatabaseConnectivity();
	}

	public void executeQuery(String query) throws Exception {
		connection.insertQuery(query);
	}
	public int executeQueryKey(String query) throws Exception {
		return connection.insertQueryWithKey(query);
	}

	public ResultSet getData(String query) throws Exception {
		ResultSet res = connection.getData(query);
		return res;
	}

	public void releaseConnection() {
		try {
			connection.closeConnection();
		} catch (Exception e) {
			log.warning("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
