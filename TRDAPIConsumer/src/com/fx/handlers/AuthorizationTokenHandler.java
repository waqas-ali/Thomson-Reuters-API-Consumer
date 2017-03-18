package com.fx.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.fx.database.DatabaseHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.AuthorizationToken;

public class AuthorizationTokenHandler {
	public static Logger log = Logger.getLogger(AuthorizationTokenHandler.class
			.getName());

	public void handlerToken(AuthorizationToken token) {
		DatabaseHandler dbHandler = new DatabaseHandler();
		try {

			String tokenString = token.getTokenString();
			Date expDate = token.getExpirationDate();
			Date createdDate = token.getCreatedDate();

			/*String expTime = DateHelper.getStandardFormatUTC(expDate);
			String createdTime = DateHelper.getStandardFormatUTC(createdDate);*/
			
			String expTime = DateHelper.getStandardFormat(expDate);
			String createdTime = DateHelper.getStandardFormat(createdDate);

			String query = "insert into trdapi_db.token_management(TOKEN_STRING,TOKEN_EXP_DATE,TOKEN_CREATED_DATE ) "
					+ " values( 	'"+ tokenString+ "' , '"+ expTime+ "' , '"+ createdTime + "' ) ";

			dbHandler.executeQuery(query);

			log.info("token has been saved into db");

		} catch (Exception e) {
			log.warning("Exception occurred : " + e.getMessage());
		} finally {
			dbHandler.releaseConnection();
		}

	}

	public AuthorizationToken getToken() {
		DatabaseHandler dbHandler = new DatabaseHandler();
		try {
			String query = "select * from trdapi_db.token_management order by token_exp_date desc limit 1";
			ResultSet result = dbHandler.getData(query);
			result.next();
			String tokenString = result.getString("TOKEN_STRING");
			Timestamp expTime = result.getTimestamp("TOKEN_EXP_DATE");
			Timestamp createdTime = result.getTimestamp("TOKEN_CREATED_DATE");

			Date expDate = new Date(expTime.getTime());
			Date createdDate = new Date(createdTime.getTime());

			result.close();
			AuthorizationToken token = new AuthorizationToken();
			token.setCreatedDate(createdDate);
			token.setExpirationDate(expDate);
			token.setTokenString(tokenString);
			return token;
		} catch (Exception e) {
			log.info("Exception Occurred while extracting token : "
					+ e.getMessage());
		} finally {
			dbHandler.releaseConnection();
		}
		return null;
	}
	public AuthorizationToken getTokenDifferently() throws Exception {
		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/trdapi_db");
		Connection connection = ds.getConnection();
		
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery("select * from trdapi_db.token_management order by token_exp_date desc");
		if (res.next()) {
			
			String tokenString = res.getString("TOKEN_STRING");
			Timestamp expTime = res.getTimestamp("TOKEN_EXP_DATE");
			Timestamp createdTime = res.getTimestamp("TOKEN_CREATED_DATE");

			Date expDate = new Date(expTime.getTime());
			Date createdDate = new Date(createdTime.getTime());
			
			res.close();
			connection.close();
			AuthorizationToken token = new AuthorizationToken();
			token.setCreatedDate(createdDate);
			token.setExpirationDate(expDate);
			token.setTokenString(tokenString);
			return token;
		}
		return null;
		
	}
}
