package com.fx.handlers;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import com.fx.database.DatabaseHandler;
import com.fx.helpers.DateHelper;
import com.fx.model.EconomicHeadlinesRequestInfo;
import com.fx.model.EventHeadlinesRequestInfo;

public class EventHeadlinesRequestInfoHandler {
	public static Logger log = Logger
			.getLogger(EventHeadlinesRequestInfoHandler.class.getName());

	public int handleRequestInfo(EventHeadlinesRequestInfo info) {

		DatabaseHandler dbHandler = new DatabaseHandler();
		try {
			Date fromDate = info.getFromDate();
			Date toDate = info.getToDate();
			int pn = info.getPageNo();
			int rpp = info.getRecordsPerPage();

			String fromTime = DateHelper.getStandardFormatUTC(fromDate);
			String toTime = DateHelper.getStandardFormatUTC(toDate);

			String query = "insert into trdapi_db.event_headlines_request_info (FROM_DATE, TO_DATE, PAGE_NO, RECORDS_PER_PAGE)"
					+ "values('"
					+ fromTime
					+ "' ,  '"
					+ toTime
					+ "' ,  "
					+ pn
					+ ", " + rpp + ")";
			int id = dbHandler.executeQueryKey(query);
			return id;
		} catch (Exception e) {
			log.warning("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		} finally {
			dbHandler.releaseConnection();
		}
		return -1;
	}

	public EventHeadlinesRequestInfo getRequestInfo(Date fromD) {
		DatabaseHandler dbHandler = new DatabaseHandler();
		try {
			String from = DateHelper.getStandardFormatUTC(fromD);
			String query = "select * from trdapi_db.event_headlines_request_info where from_date = '"
					+ from + "' order by PAGE_NO DESC limit 1";
			ResultSet res = dbHandler.getData(query);
			if (res.next()) {
				Timestamp fromTime = res.getTimestamp("FROM_DATE");
				Timestamp toTime = res.getTimestamp("TO_DATE");
				int pn = res.getInt("PAGE_NO");
				int rpp = res.getInt("RECORDS_PER_PAGE");
				int tr = res.getInt("TOTAL_RECORDS");
				int tp = res.getInt("TOTAL_PAGE");
				int rolp = res.getInt("RECORDS_ON_LAST_PAGE");

				res.close();

				Date fromDate = new Date(fromTime.getTime());
				Date toDate = new Date(toTime.getTime());
				EventHeadlinesRequestInfo info = new EventHeadlinesRequestInfo();
				info.setFromDate(fromDate);
				info.setToDate(toDate);
				info.setPageNo(pn);
				info.setRecordsOnLastPage(rolp);
				info.setRecordsPerPage(rpp);
				info.setTotalRecords(tr);
				info.setTotalPages(tp);

				return info;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		} finally {
			dbHandler.releaseConnection();
		}
		return null;
	}

	public void updateInfo(EventHeadlinesRequestInfo info) {
		DatabaseHandler dbHandler = new DatabaseHandler();
		try {

			String query = "update trdapi_db.event_headlines_request_info set TOTAL_RECORDS = "
					+ info.getTotalRecords()
					+ " , TOTAL_PAGE = "
					+ info.getTotalPages()
					+ " , RECORDS_ON_LAST_PAGE = "
					+ info.getRecordsOnLastPage()
					+ " where id = "
					+ info.getId();
			dbHandler.executeQuery(query);
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		} finally {
			dbHandler.releaseConnection();
		}
	}
}
