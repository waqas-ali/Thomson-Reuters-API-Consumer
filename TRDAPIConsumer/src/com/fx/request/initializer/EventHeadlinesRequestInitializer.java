package com.fx.request.initializer;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.fx.handlers.EventHeadlinesRequestInfoHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.EventHeadlinesRequestInfo;

public class EventHeadlinesRequestInitializer {
	private static final int PAGINATION_FIRST_PAGE = 1;
	private static final int PAGINATION_MODEST_RECORDS = 25;
	
	public static Logger log = Logger.getLogger(EventHeadlinesRequestInitializer.class
			.getName());
	private EventHeadlinesRequestInfo info;
	
	public EventHeadlinesRequestInfo getEventHdlInitParameter() {

		Date d = new Date();
		Calendar from = DateHelper.getZeroedTimeDate(d);
		Calendar to = DateHelper.getMaximumTimeDate(d);

		Date fromDate = from.getTime();
		Date toDate = to.getTime();

		EventHeadlinesRequestInfoHandler infoHandler = new EventHeadlinesRequestInfoHandler();
		EventHeadlinesRequestInfo info = infoHandler
				.getRequestInfo(fromDate);

		int pageNo = this.getEventHdlPageNo(info);
		int records = this.getEventHdlRecordsPerPage(info);
		if (pageNo == 0 || records == 0) {
			return null;
		} else {
			EventHeadlinesRequestInfo info1 = new EventHeadlinesRequestInfo();
			info1.setFromDate(fromDate);
			info1.setToDate(toDate);
			info1.setRecordsPerPage(records);
			info1.setPageNo(pageNo);
			return info1;
		}
	}
	private int getEventHdlPageNo(EventHeadlinesRequestInfo info) {
		if (info == null) {
			return PAGINATION_FIRST_PAGE;
		} else if (info.getPageNo() < info.getTotalPages()) {
			return info.getPageNo() + 1;
		} else if (info.getPageNo() == info.getTotalPages()) {
			return 0;
		}
		return 0;
	}

	private int getEventHdlRecordsPerPage(EventHeadlinesRequestInfo info) {
		if (info != null) {
			int pageNo = info.getPageNo();
			int totalPages = info.getTotalPages();
			log.info("Page No " + pageNo + " , Total Pages : " + totalPages);

			if (pageNo == totalPages - 1) {
				return info.getRecordsOnLastPage();
			} else if (pageNo == totalPages) {
				return 0;
			}
		}
		return PAGINATION_MODEST_RECORDS;
	}
	public EventHeadlinesRequestInfo getInfo() {
		return info;
	}
	public void setInfo(EventHeadlinesRequestInfo info) {
		this.info = info;
	}
}
