package com.fx.handlers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import com.fx.database.DatabaseHandler;
import com.fx.helpers.DateHelper;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Duration;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicHeadline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicIndicator;

public class EconomicHeadlinesHandler {
	public static Logger log = Logger.getLogger(EconomicHeadlinesHandler.class
			.getName());
	private String eventId;
	private DatabaseHandler dbHandler;

	public EconomicHeadlinesHandler(String eventId) {
		this.eventId = eventId;
		dbHandler = new DatabaseHandler();
	}
	

	public void handleEconomicHeadline(EconomicHeadline headline) {
		try {

			String eventType = headline.getEventType().getValue();
			String eventName = headline.getEventName().replaceAll("'", "''");
			String countryCode = headline.getCountryCode();
			this.saveHeadline(eventType, eventName, countryCode);

			DurationHandler dHandler = new DurationHandler();
			dHandler.handleDuration(headline.getDuration());

			EconomicIndicatorHandler eiHandler = new EconomicIndicatorHandler();
			eiHandler.handleEconomicIndicator(headline.getEconomicIndicator());

		} catch (Exception e) {
			log.warning("Error Occurred : " + e.getMessage());
			e.printStackTrace();
		}finally {
			dbHandler.releaseConnection();
		}
	}

	public EconomicHeadline getEconomicHeadline() {
		return null;
	}

	private void saveHeadline(String eventType, String eventName,
			String countryCode) throws Exception {

		String query = "insert into trdapi_db.economic_headlines(EVENT_ID, EVENT_TYPE, EVENT_NAME, COUNTRY_CODE) "
				+ "values ('"+ eventId+ "', '"+ eventType+ "', '"+ eventName + "', '" + countryCode + "')";
		dbHandler.executeQuery(query);

	}

	private class DurationHandler {

		public void handleDuration(Duration duration) throws Exception {

			Date endDate = duration.getEndDateTime().getTime();
			Date startDate = duration.getStartDateTime().getTime();
			String endQualifier = duration.getEndQualifier().getValue();
			String startQualifier = duration.getStartQualifier().getValue();
			boolean isEst = duration.getIsEstimate();

			String endTime = DateHelper.getStandardFormatUTC(endDate);
			String startTime = DateHelper.getStandardFormatUTC(startDate);
			int value = (isEst) ? 1 : 0;
			
			String query = "insert into trdapi_db.economic_headlines_duration(EVENT_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
					+ "values( '"+ eventId+ "', '"+ startTime+ "', '"+ endTime+ "', '"+ startQualifier+ "', '"+ endQualifier+ "', " + value + ")";
			dbHandler.executeQuery(query);
		}

		public Duration getDuration() {
			return null;
		}

	}

	private class EconomicIndicatorHandler {

		public void handleEconomicIndicator(EconomicIndicator indicator)
				throws Exception {

			BigDecimal actual = indicator.getActualValue();
			BigDecimal expected = indicator.getExpectedValue();
			BigDecimal prior = indicator.getPriorValue();
			
			String unit = indicator.getUnit();
			String period = indicator.getPeriod();
			String scale = indicator.getScale();
			String mnemonic = indicator.getDataStreamMnemonic();

			// Will see later
			/*
			 * Classifications clss = indicator.getClassifications();
			 * EconomicClassification[] array = clss.getClassification();
			 */

			String query = "insert into trdapi_db.economic_headlines_economic_idicator ( EVENT_ID, EI_UNIT, EI_SCALE, EI_PERIOD, EI_DATA_STREAM_MNEMONIC, EI_ACTUAL_VALUE, EI_EXPECTED_VALUE, EI_PRIOR_VALUE)"
					+ " values ('"
					+ eventId
					+ "' ,   '"
					+ unit
					+ "' ,  '"
					+ scale
					+ "' ,  '"
					+ period
					+ "' ,  '"
					+ mnemonic
					+ "' ,  "
					+ actual
					+ " ,  "
					+ expected
					+ " ,  "
					+ prior + ")";

			dbHandler.executeQuery(query);

		}

		public EconomicIndicator getEconomicIndicator() {
			return null;
		}
	}
}
