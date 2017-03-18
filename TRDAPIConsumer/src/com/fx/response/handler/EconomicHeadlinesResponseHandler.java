package com.fx.response.handler;

import java.util.logging.Logger;

import com.fx.handlers.EconomicHeadlinesHandler;
import com.fx.handlers.EconomicHeadlinesRequestInfoHandler;
import com.fx.model.EconomicHeadlinesRequestInfo;
import com.fx.ws.fetchers.StreetEventsWSFetcher;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicHeadline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicHeadlines;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.PaginationResult;

public class EconomicHeadlinesResponseHandler {
	
	public static Logger log = Logger.getLogger(EconomicHeadlinesResponseHandler.class
			.getName());

	private static final int PAGINATION_FIRST_PAGE = 1;
	private static final int PAGINATION_MODEST_RECORDS = 25;

	private static int requestId = 1;
	
	public EconomicHeadlinesResponseHandler(int id) {
		this.requestId = id;
	}
	public void handleResponse(GetEconomicHeadlinesResponse res) {
		PaginationResult pagRes = res.getPaginationResult();
		EconomicHeadlines econHdl = res.getEconomicHeadlines();
		this.handleEcoHdlPaginationResult(pagRes);
		this.handleEconomicHeadlines(econHdl);
	}
	private void handleEcoHdlPaginationResult(PaginationResult pr) {
		try {

			int pageNumber = pr.getPageNumber();
			int recordsOnPage = pr.getRecordsOnPage();
			int recordsPerPage = pr.getRecordsPerPage();
			int totalRecords = pr.getTotalRecords();
			int numberOfPages = (totalRecords / PAGINATION_MODEST_RECORDS) + 1;
			int recordsOnLastPage = (totalRecords % PAGINATION_MODEST_RECORDS);

			EconomicHeadlinesRequestInfo info = new EconomicHeadlinesRequestInfo();
			info.setRecordsOnLastPage(recordsOnLastPage);
			info.setTotalPages(numberOfPages);
			info.setTotalRecords(totalRecords);
			info.setId(requestId);

			EconomicHeadlinesRequestInfoHandler infoHandler = new EconomicHeadlinesRequestInfoHandler();
			infoHandler.updateInfo(info);
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void handleEconomicHeadlines(EconomicHeadlines hdls) {
		EconomicHeadline[] array = hdls.getEconomicHeadline();
		int size = array.length;
		for (int i = 0; i < size; i++) {
			EconomicHeadline headline = array[i];
			int eventId = headline.getEventId();

			EconomicHeadlinesHandler ehHandler = new EconomicHeadlinesHandler(
					"" + eventId);
			ehHandler.handleEconomicHeadline(headline);
		}
	}
	
}
