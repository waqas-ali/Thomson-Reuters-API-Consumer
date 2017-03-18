package com.fx.response.handler;

import java.util.logging.Logger;

import com.fx.handlers.EventHeadlinesOverviewHandler;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverviewResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Headline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Headlines;

public class EventHeadlinesOverviewResponseHandler {
	public static Logger log = Logger
			.getLogger(EventHeadlinesOverviewResponseHandler.class.getName());

	public void handleResponse(GetEventHeadlinesOverviewResponse response) {
		this.handleRecentEventHeadlines(response.getRecentEventHeadlines());
		this.handleUpcomingEventHeadlines(response.getUpcomingEventHeadlines());

	}

	private void handleUpcomingEventHeadlines(Headlines headlines) {
		try {
			Headline[] hdls = headlines.getHeadline();
			int size = hdls.length;
			for (int i = 0; i < size; i++) {
				Headline hdl = hdls[i];
				EventHeadlinesOverviewHandler ehoHandler = new EventHeadlinesOverviewHandler(""+hdl.getEventId(), "upcoming");
				ehoHandler.handleEventHeadlines(hdl);
			}
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void handleRecentEventHeadlines(Headlines headlines) {
		try {
			Headline[] hdls = headlines.getHeadline();
			int size = hdls.length;
			for (int i = 0; i < size; i++) {
				Headline hdl = hdls[i];
				EventHeadlinesOverviewHandler ehoHandler = new EventHeadlinesOverviewHandler(""+hdl.getEventId(), "recent");
				ehoHandler.handleEventHeadlines(hdl);
			}
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
