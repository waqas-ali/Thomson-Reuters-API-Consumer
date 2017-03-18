package com.fx.request.initializer;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.fx.handlers.EconomicHeadlinesRequestInfoHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.EconomicHeadlinesRequestInfo;
import com.fx.model.EventHeadlinesRequestInfo;

public class EconomicHeadlinesRequestInitializer {
	private static final int PAGINATION_FIRST_PAGE = 1;
	private static final int PAGINATION_MODEST_RECORDS = 25;
	
	public static Logger log = Logger.getLogger(EconomicHeadlinesRequestInitializer.class
			.getName());
	
	public EconomicHeadlinesRequestInfo getEcoHdlInitParameter() {

		Date d = new Date();
		Calendar from = DateHelper.getZeroedTimeDate(d);
		Calendar to = DateHelper.getMaximumTimeDate(d);

		Date fromDate = from.getTime();
		Date toDate = to.getTime();

		EconomicHeadlinesRequestInfoHandler infoHandler = new EconomicHeadlinesRequestInfoHandler();
		EconomicHeadlinesRequestInfo info = infoHandler
				.getRequestInfo(fromDate);

		int pageNo = this.getEcoHdlPageNo(info);
		int records = this.getEcoHdlRecordsPerPage(info);
		if (pageNo == 0 || records == 0) {
			return null;
		} else {
			EconomicHeadlinesRequestInfo info1 = new EconomicHeadlinesRequestInfo();
			info1.setFromDate(fromDate);
			info1.setToDate(toDate);
			info1.setRecordsPerPage(records);
			info1.setPageNo(pageNo);
			return info1;
		}
	}
	
	private int getEcoHdlPageNo(EconomicHeadlinesRequestInfo info) {
		if (info == null) {
			return PAGINATION_FIRST_PAGE;
		} else if (info.getPageNo() < info.getTotalPages()) {
			return info.getPageNo() + 1;
		} else if (info.getPageNo() == info.getTotalPages()) {
			return 0;
		}
		return 0;
	}

	private int getEcoHdlRecordsPerPage(EconomicHeadlinesRequestInfo info) {
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

}
