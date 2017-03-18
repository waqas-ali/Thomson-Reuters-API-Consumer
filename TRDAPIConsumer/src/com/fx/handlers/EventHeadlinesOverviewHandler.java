package com.fx.handlers;

import java.util.Date;
import java.util.logging.Logger;

import com.fx.database.DatabaseHandler;
import com.fx.helpers.DateHelper;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.SymbolCriterion;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.Symbols;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Address;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.DialInAccess;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Duration;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Headline;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Location;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Organization;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.WebcastOverview;

public class EventHeadlinesOverviewHandler {

	public static Logger log = Logger
			.getLogger(EventHeadlinesOverviewHandler.class.getName());
	private String eventId;
	private String type;
	private DatabaseHandler dbHandler;

	public EventHeadlinesOverviewHandler(String eventId, String type) {
		this.eventId = eventId;
		this.type = type;
		dbHandler = new DatabaseHandler();

	}

	public void handleEventHeadlines(Headline hdl) {
		try {

			this.saveData(hdl);

			Duration duration = hdl.getDuration();
			DurationHandler dHandler = new DurationHandler();
			dHandler.handleDuration(duration);

			DialInAccess ldi = hdl.getLiveDialIn();
			DialInAccessHandler ldaHandler = new DialInAccessHandler("Live");
			ldaHandler.handleDialInAccess(ldi);

			WebcastOverview lwc = hdl.getLiveWebcast();
			WebcastOverivewHandler lwoHandler = new WebcastOverivewHandler(
					"Live");
			lwoHandler.handleWCOHandler(lwc);

			Location loc = hdl.getLocation();
			LocationHandler lHandler = new LocationHandler();
			lHandler.handleLocation(loc);

			DialInAccess rdi = hdl.getReplayDialIn();
			DialInAccessHandler rdaHandler = new DialInAccessHandler("Replay");
			rdaHandler.handleDialInAccess(rdi);

			WebcastOverview rwc = hdl.getReplayWebcast();
			WebcastOverivewHandler rwoHandler = new WebcastOverivewHandler(
					"Replay");
			rwoHandler.handleWCOHandler(rwc);

			Organization org = hdl.getOrganization();
			OrganizationHandler oHandler = new OrganizationHandler();
			oHandler.handleOrganization(org);

		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveData(Headline hdl) {
		try {
			String code = hdl.getCountryCode();
			String eventType = hdl.getEventType().getValue();
			String name = hdl.getName();
			boolean rsvp = hdl.getRsvpRequired();

			int value = (rsvp) ? 1 : 0;
			String update = DateHelper.calendarToStringUTC(hdl.getLastUpdate());

			String query = "insert into trdapi_db.event_headlines_overview (EVENT_ID, COUNTRY_CODE, EVENT_TYPE, EVENT_NAME, RSVP_REQUIRED, UPDATED_DATE,OVERVIEW_TYPE)"
					+ "values ('"
					+ eventId
					+ "', '"
					+ code
					+ "', '"
					+ eventType
					+ "', '"
					+ name
					+ "', '"
					+ value
					+ "', '"
					+ update + "', '" + type + "')";
			dbHandler.executeQuery(query);
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private class DurationHandler {

		public void handleDuration(Duration duration) {
			try {
				String startQ = duration.getStartQualifier().getValue();
				String endQ = duration.getEndQualifier().getValue();
				boolean isEst = duration.getIsEstimate();

				String endTime = DateHelper.calendarToStringUTC(duration.getEndDateTime());
				String startTime = DateHelper.calendarToStringUTC(duration.getStartDateTime());
				int est = (isEst) ? 1 : 0;

				String query = "insert into trdapi_db.event_headlines_overview_duration(EVENT_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
						+ "values( '"
						+ eventId
						+ "', '"
						+ startTime
						+ "', '"
						+ endTime
						+ "', '"
						+ startQ
						+ "', '"
						+ endQ
						+ "', "
						+ est + ")";
				dbHandler.executeQuery(query);
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private class OrganizationHandler {
		public void handleOrganization(Organization org) {
			try {
				String query = "";
				String name = org.getName();
				Symbols symbols = org.getSymbols();
				SymbolCriterion[] array = symbols.getSymbol();

				String type = "";
				String value = "";

				if (array != null && array.length > 0) {
					type = array[0].getType();
					value = array[0].getValue();
				}

				query = "insert into trdapi_db.event_headlines_overview_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
						+ " values('"
						+ eventId
						+ "','"
						+ name
						+ "','"
						+ type
						+ "','" + value + "')";

				dbHandler.executeQuery(query);

				int size = array.length;
				for (int i = 1; i < size; i++) {
					type = array[0].getType();
					value = array[0].getValue();
					query = "insert into trdapi_db.event_headlines_overview_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
							+ " values('"
							+ eventId
							+ "','"
							+ name
							+ "','"
							+ type + "','" + value + "')";
					dbHandler.executeQuery(query);
				}

			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private class LocationHandler {

		public void handleLocation(Location loc) {
			try {
				String afn = loc.getAlternateFaxNumber();
				String apn = loc.getAlternatePhoneNumber();
				String des = loc.getDescription();
				String email = loc.getEmail();
				String name = loc.getName();
				String notes = loc.getNotes();
				String pfn = loc.getPrimaryFaxNumber();
				String ppn = loc.getPrimaryPhoneNumber();
				String room = loc.getRoom();
				String url = loc.getUrl();

				String query = "insert into trdapi_db.event_headlines_overview_location (EVENT_ID, NAME, DESCRIPTION, NOTES, EMAIL, ROOM, URL, ALTERNATIVE_FAX_NUMBER, PRIMARY_FAX_NUMBER, ALTERNATIVE_PHONE_NUMBER, PRIMARY_PHONE_NUMBER ) "
						+ " values ('"
						+ eventId
						+ "', '"
						+ name
						+ "', '"
						+ des
						+ "', '"
						+ notes
						+ "', '"
						+ email
						+ "', '"
						+ room
						+ "', '"
						+ url
						+ "', '"
						+ afn
						+ "', '"
						+ pfn
						+ "','"
						+ apn
						+ "', '"
						+ ppn + "')";
				int value = dbHandler.executeQueryKey(query);

				Address add = loc.getAddress();
				AddressHandler aHandler = new AddressHandler(value);
				aHandler.handleAddress(add);
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}

		private class AddressHandler {
			private int locationId;

			public AddressHandler(int id) {
				this.locationId = id;
			}

			public void handleAddress(Address add) {
				try {
					String al1 = add.getAddressLine1();
					String al2 = add.getAddressLine2();
					String city = add.getCity();
					String code = add.getCountryCode();
					String pc = add.getPostalCode();
					String sp = add.getStateProvince();

					String query = "";
					log.info("Location Id : " + locationId);
					query = "insert into trdapi_db.event_headlines_overview_location_address(LOCATION_ID, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, COUNTRY_CODE, POSTAL_CODE, STATE_PROVINCE)"
							+ "values("
							+ locationId
							+ ",'"
							+ al1
							+ "', '"
							+ al2
							+ "', '"
							+ city
							+ "', '"
							+ code
							+ "', '"
							+ pc
							+ "', '" + sp + "')";
					dbHandler.executeQuery(query);

				} catch (Exception e) {
					log.warning("Exception Occurred : " + e.getMessage());
					e.printStackTrace();
				}

			}
		}
	}

	private class DialInAccessHandler {
		private String type;

		public DialInAccessHandler(String type) {
			this.type = type;
		}

		public void handleDialInAccess(DialInAccess dia) {
			try {
				String apn = dia.getAlternatePhoneNumber();
				String notes = dia.getNotes();
				String pswd = dia.getPassword();
				String pn = dia.getPhoneNumber();
				String status = dia.getStatus().getValue();

				String query = "";

				query = "insert into trdapi_db.event_headlines_overview_dial_in_access ( EVENT_ID, DIAL_IN_ACCESS_TYPE, ALTERNATIVE_PHONE_NUMBER, PHONE_NUMBER, NOTES, PASSWORD, STATUS)"
						+ "values('"
						+ eventId
						+ "', '"
						+ type
						+ "', '"
						+ apn
						+ "', '"
						+ pn
						+ "', '"
						+ notes
						+ "', '"
						+ pswd
						+ "', '"
						+ status + "')";

				int id = dbHandler.executeQueryKey(query);

				Duration duration = dia.getDuration();
				DurationHandler dHandler = new DurationHandler(id);
				dHandler.handleDuration(duration);

			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}

		private class DurationHandler {
			private int dialInAccessId;

			public DurationHandler(int id) {
				dialInAccessId = id;
			}

			public void handleDuration(Duration duration) {

				try {
					
					String startQ = duration.getStartQualifier().getValue();
					String endQ = duration.getEndQualifier().getValue();
					boolean isEst = duration.getIsEstimate();
					String endDate = DateHelper.calendarToStringUTC( duration.getEndDateTime());
					String startDate = DateHelper.calendarToStringUTC(duration.getStartDateTime());
					
					int est = (isEst) ? 1 : 0;
					log.info("Dial In Access Id :" + dialInAccessId);
					String query = "insert into trdapi_db.event_headlines_overview_dial_in_access_duration(DIAL_IN_ACCESS_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
							+ "values( "
							+ dialInAccessId
							+ ", '"
							+ startDate
							+ "', '"
							+ endDate
							+ "', '"
							+ startQ
							+ "', '"
							+ endQ + "', " + est + ")";
					dbHandler.executeQuery(query);
				} catch (Exception e) {
					log.warning("Exception Occurred : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	private class WebcastOverivewHandler {
		private String type;

		public WebcastOverivewHandler(String type) {
			this.type = type;
		}

		public void handleWCOHandler(WebcastOverview wco) {
			try {

				String webcastId = wco.getWebcastId();
				String status = wco.getStatus().getValue();

				String query = "insert into trdapi_db.event_headlines_overview_webcast_overview(EVENT_ID, WEBCAST_ID, STATUS, WEBCAST_TYPE)"
						+ "values('"
						+ eventId
						+ "', '"
						+ webcastId
						+ "', '"
						+ status + "', '" + type + "')";

				int value = dbHandler.executeQueryKey(query);

				Duration duration = wco.getDuration();
				DurationHandler dHandler = new DurationHandler(value);
				dHandler.handleDuration(duration);
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}

		private class DurationHandler {
			private int wcoId;

			public DurationHandler(int id) {
				this.wcoId = id;
			}

			public void handleDuration(Duration duration) {
				try {
					String startQ = duration.getStartQualifier().getValue();
					String endQ = duration.getEndQualifier().getValue();
					boolean isEst = duration.getIsEstimate();

					String endDate = DateHelper.calendarToStringUTC(duration.getEndDateTime());
					String startDate = DateHelper.calendarToStringUTC(duration.getStartDateTime());
					int est = (isEst) ? 1 : 0;
					
					String query = "insert into trdapi_db.event_headlines_overview_webcast_overview_duration(WEBCAST_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
							+ "values( "
							+ wcoId
							+ ", '"
							+ startDate
							+ "', '"
							+ endDate
							+ "', '"
							+ startQ
							+ "', '"
							+ endQ
							+ "', "
							+ est + ")";
					dbHandler.executeQuery(query);
				} catch (Exception e) {
					log.warning("Exception Occurred : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
