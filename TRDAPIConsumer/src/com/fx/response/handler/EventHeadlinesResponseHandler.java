package com.fx.response.handler;

import java.util.logging.Logger;

import com.fx.handlers.EconomicHeadlinesHandler;
import com.fx.handlers.EconomicHeadlinesRequestInfoHandler;
import com.fx.handlers.EventHeadlinesHandler;
import com.fx.handlers.EventHeadlinesRequestInfoHandler;
import com.fx.model.EconomicHeadlinesRequestInfo;
import com.fx.model.EventHeadlinesRequestInfo;
import com.fx.ws.fetchers.StreetEventsWSFetcher;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicHeadline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicHeadlines;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Headline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Headlines;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.PaginationResult;

public class EventHeadlinesResponseHandler {

	public static Logger log = Logger
			.getLogger(EventHeadlinesResponseHandler.class.getName());

	private static final int PAGINATION_FIRST_PAGE = 1;
	private static final int PAGINATION_MODEST_RECORDS = 25;

	private int requestId;

	public EventHeadlinesResponseHandler(int id ) {
		requestId = id;
	}
	public void parseEventHeadlinesResponse(GetEventHeadlinesResponse response ) {
		Headlines headlines = response.getEventHeadlines();
		PaginationResult pagination = response.getPaginationResult();
		this.handleEventHdlPaginationResult(pagination);
		this.handleEventHeadlines(headlines);
	}
	
	private void handleEventHdlPaginationResult(PaginationResult pr) {
		try {
			int pageNumber = pr.getPageNumber();
			int recordsOnPage = pr.getRecordsOnPage();
			int recordsPerPage = pr.getRecordsPerPage();
			int totalRecords = pr.getTotalRecords();
			int numberOfPages = (totalRecords / PAGINATION_MODEST_RECORDS) + 1;
			int recordsOnLastPage = (totalRecords % PAGINATION_MODEST_RECORDS);
			
			EventHeadlinesRequestInfo info = new EventHeadlinesRequestInfo();
			info.setRecordsOnLastPage(recordsOnLastPage);
			info.setTotalPages(numberOfPages);
			info.setTotalRecords(totalRecords);
			info.setId(requestId);

			EventHeadlinesRequestInfoHandler infoHandler = new EventHeadlinesRequestInfoHandler();
			infoHandler.updateInfo(info);

		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void handleEventHeadlines(Headlines headlines) {
		Headline[] array = headlines.getHeadline();
		int size = array.length;
		for (int i = 0; i < size; i++) {
			Headline hdl = array[i];
			EventHeadlinesHandler ehHandler = new EventHeadlinesHandler(""
					+ hdl.getEventId());
			ehHandler.handleEventHeadlines(hdl);
		}
	}
}
