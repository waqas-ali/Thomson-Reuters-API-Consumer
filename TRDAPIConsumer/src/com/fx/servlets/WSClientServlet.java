package com.fx.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.fx.handlers.AuthorizationTokenHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.AuthorizationToken;
import com.fx.ws.consumers.TokenManagmentWSConsumer;
import com.fx.ws.fetchers.StreetEventsWSFetcher;

@SuppressWarnings("serial")
public class WSClientServlet extends HttpServlet {
	public static Logger log = Logger
			.getLogger(WSClientServlet.class.getName());
	//Handler fileHandler = new FileHandler("/var/log/tomcat/myapp.log");
	

	public static int requestId = 1;
	private static final String CREDENTIAL_FILE_NAME = "credentials.xml";
	private static String userId;
	private static String password;
	private static String appId;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			log.info("inside servlet");
			//initCredentials();

			AuthorizationTokenHandler atHandler = new AuthorizationTokenHandler();
			//AuthorizationToken token = atHandler.getToken();
			AuthorizationToken token = atHandler.getTokenDifferently();
			Calendar cal = DateHelper.dateToCalendarUTC(token
					.getExpirationDate());
			Calendar now = DateHelper.dateToCalendar(new Date());

			if (token == null || cal.before(now)) {
				/*TokenManagmentWSConsumer consumer = new TokenManagmentWSConsumer(
						userId, password, appId);
				token = consumer.getToken();
				atHandler.handlerToken(token);*/
				resp.getWriter().write("New Token : " + token.getTokenString());
			} else {
				resp.getWriter().write(
						"Token extracted from DB : " + token.getTokenString());
			}
			
			/*StreetEventsWSFetcher fetcher = new StreetEventsWSFetcher(appId,
					token);
			fetcher.fetchEconomicHeadlines();
			fetcher.fetchEventHeadlines();
			fetcher.fetchEventHeadlinesOverview();
			fetcher.fetchEventDetail();*/
			
			
		} catch (Exception e) {
			log.warning("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}



	private void initCredentials() throws JDOMException, IOException {

		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(new File(CREDENTIAL_FILE_NAME));
		Element root = doc.getRootElement();
		Element em = root.getChild("webservices");
		userId = em.getChild("user-id").getText();
		appId = em.getChild("application-id").getText();
		password = em.getChild("password").getText();

		log.info("XML file parsing has been completed");

	}
}
