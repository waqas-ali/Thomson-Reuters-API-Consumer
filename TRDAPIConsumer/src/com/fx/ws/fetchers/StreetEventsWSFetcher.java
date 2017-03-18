package com.fx.ws.fetchers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.fx.handlers.EconomicHeadlinesRequestInfoHandler;
import com.fx.handlers.EventHeadlinesRequestInfoHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.AuthorizationToken;
import com.fx.model.EconomicHeadlinesRequestInfo;
import com.fx.model.EventHeadlinesRequestInfo;
import com.fx.request.initializer.EconomicHeadlinesRequestInitializer;
import com.fx.request.initializer.EventDetailRequestInitializer;
import com.fx.request.initializer.EventHeadlinesRequestInitializer;
import com.fx.response.handler.EconomicHeadlinesResponseHandler;
import com.fx.response.handler.EventDetailResponseHandler;
import com.fx.response.handler.EventHeadlinesOverviewResponseHandler;
import com.fx.response.handler.EventHeadlinesResponseHandler;
import com.fx.ws.consumers.StreetsEventWSConsumer;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverviewResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventResponse;

public class StreetEventsWSFetcher {
	public static Logger log = Logger.getLogger(StreetEventsWSFetcher.class
			.getName());

	private static final int PAGINATION_FIRST_PAGE = 1;
	private static final int PAGINATION_MODEST_RECORDS = 25;
	private static int EcoHdlRequestId = 1;
	private static int EventHdlRequestId = 1;

	private String appId;
	private AuthorizationToken token;

	public StreetEventsWSFetcher(String appId, AuthorizationToken token) {
		this.appId = appId;
		this.token = token;
	}

	public void fetchEconomicHeadlines() {
		try {
			int i = 0;
			int j = 50;
			while (i < j) {

				EconomicHeadlinesRequestInfo info = new EconomicHeadlinesRequestInitializer()
						.getEcoHdlInitParameter();
				if (info == null) {
					return;
				}

				EconomicHeadlinesRequestInfoHandler infoHandler = new EconomicHeadlinesRequestInfoHandler();
				int requestId = infoHandler.handleRequestInfo(info);

				StreetsEventWSConsumer seConsumer = new StreetsEventWSConsumer(
						token, appId);

				GetEconomicHeadlinesResponse response = seConsumer
						.getEconomicHeadlines(info.getPageNo(), info
								.getRecordsPerPage(), DateHelper
								.dateToCalendarUTC(info.getFromDate()),
								DateHelper.dateToCalendarUTC(info.getToDate()));

				EconomicHeadlinesResponseHandler responseHandler = new EconomicHeadlinesResponseHandler(
						requestId);
				responseHandler.handleResponse(response);
				i++;
			}
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void fetchEventHeadlines() {
		try {
			int i = 0;
			int j = 50;
			while (i < j) {

				EventHeadlinesRequestInfo info = new EventHeadlinesRequestInitializer()
						.getEventHdlInitParameter();
				if (info == null) {
					return;
				}
				EventHeadlinesRequestInfoHandler infoHandler = new EventHeadlinesRequestInfoHandler();
				int requestId = infoHandler.handleRequestInfo(info);

				StreetsEventWSConsumer seConsumer = new StreetsEventWSConsumer(
						token, appId);
				GetEventHeadlinesResponse response = seConsumer
						.getEventHeadlines(info.getPageNo(), info
								.getRecordsPerPage(), DateHelper
								.dateToCalendarUTC(info.getFromDate()),
								DateHelper.dateToCalendarUTC(info.getToDate()));

				EventHeadlinesResponseHandler responseHandler = new EventHeadlinesResponseHandler(
						requestId);
				responseHandler.parseEventHeadlinesResponse(response);
				i++;
			}
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void fetchEventHeadlinesOverview() {
		try {
			int recordsPerGroup = 1;
			StreetsEventWSConsumer consumer = new StreetsEventWSConsumer(token,
					appId);
			GetEventHeadlinesOverviewResponse response = consumer
					.getEventHeadlinesOverview(recordsPerGroup);

			EventHeadlinesOverviewResponseHandler responseHandler = new EventHeadlinesOverviewResponseHandler();
			responseHandler.handleResponse(response);

		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void fetchEventDetail() {
		try {

			EventDetailRequestInitializer initializer = new EventDetailRequestInitializer();
			ArrayList<Integer> list = initializer.getEventIds();
			for (int id : list) {
				StreetsEventWSConsumer consumer = new StreetsEventWSConsumer(
						token, appId);
				GetEventResponse response = consumer
						.getEventDetailResponse(id);
				EventDetailResponseHandler responseHandler = new EventDetailResponseHandler();
				responseHandler.handleResponse(response);		
			}

		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
