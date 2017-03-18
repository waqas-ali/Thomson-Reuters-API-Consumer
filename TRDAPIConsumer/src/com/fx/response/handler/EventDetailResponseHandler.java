package com.fx.response.handler;

import java.util.logging.Logger;

import com.fx.database.DatabaseHandler;
import com.fx.helpers.DateHelper;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.SymbolCriterion;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.Symbols;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Address;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.AssociatedEvents;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Contact;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Contacts;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.DialInAccess;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Dividend;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Duration;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Event;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EventOverview;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.IPO;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Location;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Organization;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.RsvpInformation;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Split;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.WebcastOverview;

public class EventDetailResponseHandler {
	public static Logger log = Logger
			.getLogger(EventDetailResponseHandler.class.getName());
	private DatabaseHandler dbHandler;

	public EventDetailResponseHandler() {
		dbHandler = new DatabaseHandler();
	}

	public void handleResponse(GetEventResponse response) {
		try {
			Event event = response.getEvent();
			int id = event.getEventId();
			this.saveData(event);

			AssociatedEvents ae = event.getAssociatedEvents();
			AssociatedEventsHandler aeHandler = new AssociatedEventsHandler(id);
			aeHandler.handleAssociatedEvents(ae);

			Contacts con = event.getContacts();
			ContactsHandler cHandler = new ContactsHandler(id);
			cHandler.handleContacts(con);

			Dividend dnd = event.getDividend();
			DividendHandler dHandler = new DividendHandler(id);
			dHandler.handleDividend(dnd);

			Duration duration = event.getDuration();
			DurationHandler durHandler = new DurationHandler(id);
			durHandler.handleDuration(duration);

			RsvpInformation rinfo = event.getRsvpInformation();
			RsvpInformationHandler riHandler = new RsvpInformationHandler(id);
			riHandler.handleRsvpInfo(rinfo);

			IPO ipo = event.getIPO();
			IPOHandler ipoHandler = new IPOHandler(id);
			ipoHandler.handleIPO(ipo);

			Split split = event.getSplit();
			SplitHandler spHandler = new SplitHandler(id);
			spHandler.handleSplit(split);

			Organization org = event.getSponsor();
			OrganizationHandler orgHandler = new OrganizationHandler(id);
			orgHandler.handleOrganization(org);

			DialInAccess ldi = event.getLiveDialIn();
			DialInAccessHandler ldaHandler = new DialInAccessHandler("Live", id);
			ldaHandler.handleDialInAccess(ldi);

			WebcastOverview lwo = event.getLiveWebcast();
			WebcastOverivewHandler lwoHandler = new WebcastOverivewHandler(
					"Live", id);
			lwoHandler.handleWCOHandler(lwo);

			DialInAccess rdi = event.getReplayDialIn();
			DialInAccessHandler rdaHandler = new DialInAccessHandler("Replay",
					id);
			rdaHandler.handleDialInAccess(rdi);

			WebcastOverview rwo = event.getReplayWebcast();
			WebcastOverivewHandler rwoHandler = new WebcastOverivewHandler(
					"Replay", id);
			rwoHandler.handleWCOHandler(rwo);

			Location loc = event.getLocation();
			LocationHandler locHandler = new LocationHandler(id);
			locHandler.handleLocation(loc);
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveData(Event event) throws Exception {
		int id = event.getEventId();
		String code = event.getCountryCode();

		String eventType = event.getEventType().getValue();
		String date = DateHelper.calendarToStringUTC(event.getLastUpdate());
		String eventName = event.getName();
		String notes = event.getNotes();
		String source = event.getSource();
		int fiscalY = -1;
		int quarter = -1;
		if ( event.getFiscalPeriod() != null ) {
			fiscalY = event.getFiscalPeriod().getFiscalYear();
			quarter = event.getFiscalPeriod().getQuarter();
		}
		

		String query = "insert into trdapi_db.event_details (EVENT_ID, COUNTRY_CODE,EVENT_TYPE, EVENT_NAME,UPDATED_DATE,NOTES, SOURCE, FISCAL_YEAR, FISCAL_QUARTER)"
				+ "values ('"
				+ id
				+ "', '"
				+ code
				+ "', '"
				+ eventType
				+ "', '"
				+ eventName
				+ "', '"
				+ date
				+ "', '"
				+ notes
				+ "', '"
				+ source + "', " + fiscalY + ", " + quarter + ")";
		dbHandler.executeQuery(query);

	}
 

	private class AssociatedEventsHandler {
		private int eventId;

		public AssociatedEventsHandler(int id) {
			this.eventId = id;
		}

		public void handleAssociatedEvents(AssociatedEvents ae) {
			try {
				EventOverview[] list = ae.getAssociatedEvent();
				for (int i = 0; i < list.length; i++) {

					EventOverview event = list[0];
					this.saveData(event);
					int id = event.getEventId();

					Duration dur = event.getDuration();
					Organization org = event.getOrganization();

					DurationHandler dHandler = new DurationHandler(id);
					dHandler.handleDuration(dur);

					OrganizationHandler orgHandler = new OrganizationHandler(id);
					orgHandler.handleOrganization(org);

				}
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}

		}

		private void saveData(EventOverview event) throws Exception {

			int id = event.getEventId();
			String type = event.getEventType().getValue();
			String name = event.getName();
			String date = DateHelper.calendarToStringUTC(event.getLastUpdate());

			String query = "insert into trdapi_db.event_details_associated_events (EVENT_ID, EVENT_NAME, EVENT_TYPE, EVENT_UPDATE_DATE, ASSOCIATED_EVENT_ID)"
					+ "values('"
					+ eventId
					+ "', '"
					+ name
					+ "', '"
					+ type
					+ "', '" + date + "', '" + id + "')";

			dbHandler.executeQuery(query);
		}

		private class DurationHandler {
			private int eventId;
			public DurationHandler(int id) {
				this.eventId = id;
			}
			public void handleDuration(Duration duration) {
				try {
					
					String endTime = DateHelper.calendarToStringUTC(duration.getEndDateTime());
					String startTime = DateHelper.calendarToStringUTC(duration.getStartDateTime());
					String startQ = duration.getStartQualifier().getValue();
					String endQ = duration.getEndQualifier().getValue();
					boolean isEst = duration.getIsEstimate();

					int est = (isEst) ? 1 : 0;

					String query = "insert into trdapi_db.event_details_associated_events_duration(EVENT_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
							+ "values( '"
							+ eventId
							+ "', '"
							+ startTime
							+ "', '"
							+ endTime
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

		private class OrganizationHandler {
			private int eventId;

			public OrganizationHandler(int id) {
				eventId = id;
			}

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

					query = "insert into trdapi_db.event_details_associated_events_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
							+ " values('"
							+ eventId
							+ "','"
							+ name
							+ "','"
							+ type + "','" + value + "')";

					dbHandler.executeQuery(query);

					int size = array.length;
					for (int i = 1; i < size; i++) {
						type = array[0].getType();
						value = array[0].getValue();
						query = "insert into trdapi_db.event_details_associated_events_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
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
	}

	private class ContactsHandler {
		private int eventId;
		public ContactsHandler(int id) {
			this.eventId = id;
		}
		public void handleContacts(Contacts cont) {
			try {
				Contact[] conts = cont.getContact();
				for (int i = 0; i < conts.length; i++) {
					Contact con = conts[i];
					int id = this.saveData(con);

					Address add = con.getAddress();
					AddressHandler adHandler = new AddressHandler(id);
					adHandler.handleAddress(add);

				}
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}

		private int saveData(Contact con) throws Exception {

			String email = con.getEmail();
			String fName = con.getFirstName();
			String honor = con.getHonorific();
			String lName = con.getLastName();
			String mName = con.getMiddleName();
			String orgName = con.getOrganizationName();
			String phoneNo = con.getPhoneNumber();
			String suffix = con.getSuffix();
			String title = con.getTitle();
			String url = con.getUrl();

			String query = "insert into trdapi_db.event_details_contacts ( EVENT_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL, HONORIFIC, ORGNIZATION_NAME, PHONE_NUMBER, SUFFIX, TITLE, URL)"
					+ "values('"
					+ eventId
					+ "', '"
					+ fName
					+ "', '"
					+ mName
					+ "', '"
					+ lName
					+ "', '"
					+ email
					+ "', '"
					+ honor
					+ "', '"
					+ orgName
					+ "', '"
					+ phoneNo
					+ "', '"
					+ suffix
					+ "', '"
					+ title + "', '" + url + "')";

			int value = dbHandler.executeQueryKey(query);
			return value;
		}

		private class AddressHandler {
			private int contactId;

			public AddressHandler(int id) {
				this.contactId = id;
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
					log.info("Contact Id : " + contactId);
					query = "insert into trdapi_db.event_details_contacts_address(CONTACT_ID, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, COUNTRY_CODE, POSTAL_CODE, STATE_PROVINCE)"
							+ "values("
							+ contactId
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

	private class DividendHandler {
		private int eventId;
		public DividendHandler(int id) {
			this.eventId = id;
		}
		public void handleDividend(Dividend dnd) {
			try {
				double amount = dnd.getAmount();
				String currency = dnd.getCurrency();
				String payType = dnd.getPaymentType().getValue();
				String secPayType = dnd.getSecondaryPaymentType().getValue();

				String annDate = DateHelper.calendarToStringUTC(dnd.getAnnouncementDate());
				String exDate = DateHelper.calendarToStringUTC(dnd.getExDate());
				String payDate = DateHelper.calendarToStringUTC(dnd.getPaymentDate());
				String recDate = DateHelper.calendarToStringUTC(dnd.getRecordDate());

				String query = "insert into trdapi_db.event_details_dividend( EVENT_ID, ANNOUNCEMENT_DATE, EXP_DATE, RECORD_DATE, PAYMENT_DATE, PAYMENT_TYPE, SECONDARY_PAYMENT_TYPE, AMOUNT, CURRENCY)"
						+ "values('"
						+ eventId
						+ "', '"
						+ annDate
						+ "', '"
						+ exDate
						+ "', '"
						+ recDate
						+ "', '"
						+ payDate
						+ "', '"
						+ payType
						+ "', '"
						+ secPayType
						+ "', '"
						+ amount
						+ "', '" + currency + "')";

				dbHandler.executeQuery(query);

			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private class DurationHandler {
		private int eventId;
		public DurationHandler(int id) {
			this.eventId = id;
		}
		public void handleDuration(Duration duration) {
			try {
				
				String startQ = duration.getStartQualifier().getValue();
				String endQ = duration.getEndQualifier().getValue();
				boolean isEst = duration.getIsEstimate();

				String endTime = DateHelper.calendarToStringUTC(duration.getEndDateTime());
				String startTime = DateHelper.calendarToStringUTC(duration.getStartDateTime());
				
				int est = (isEst) ? 1 : 0;

				String query = "insert into trdapi_db.event_details_duration(EVENT_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
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

	private class RsvpInformationHandler {
		private int eventId;
		public RsvpInformationHandler(int id) {
			this.eventId = id;
		}
		public void handleRsvpInfo(RsvpInformation info) {
			try {
				
				String email = info.getEmail();
				String faxNo = info.getFaxNumber();
				String phoneNo = info.getPhoneNumber();
				String url = info.getUrl();

				String date = DateHelper.calendarToStringUTC(info.getDeadline());
				
				String query = "insert into trdapi_db.event_details_rsvp_information (EVENT_ID, DEADLINE_TIME, EMAIL, FAX_NUMBER, URL,PHONE_NUMBER)"
						+ "values('"
						+ eventId
						+ "', '"
						+ date
						+ "', '"
						+ email
						+ "', '"
						+ faxNo
						+ "', '"
						+ url
						+ "', '"
						+ phoneNo
						+ "')";

				dbHandler.executeQuery(query);
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private class IPOHandler {
		private int eventId;
		public IPOHandler(int id) {
			this.eventId = id;
		}
		public void handleIPO(IPO ipo) {
			try {
				double high = ipo.getFilingPriceHigh();
				double low = ipo.getFilingPriceLow();
				double fprice = ipo.getFinalPrice();
				int shares = ipo.getTotalShares();
				double oprice = ipo.getOfferingPrice();

				String des = ipo.getDescription();
				String exc = ipo.getExchange();
				String filed = ipo.getFormFiled();
				String lead = ipo.getLeadUnderwriter();
				String fDate = DateHelper.calendarToStringUTC(ipo
						.getFilingDate());
				String eDate = DateHelper.calendarToStringUTC(ipo
						.getLockupExpiryDate());
				String pDate = DateHelper.calendarToStringUTC(ipo
						.getPricingDate());
				String wDate = DateHelper.calendarToStringUTC(ipo
						.getWithdrawalDate());

				String query = "insert into trdapi_db.event_details_ipo (EVENT_ID, DESCRIPTION, EXCHANGE, PRICE_HIGH, PRICE_LOW, FINAL_PRICE, FORM_FILED, LEAD_UNDER_WRITER, OFFERING_PRICE, TOTAL_SHARES, FILING_DATE, EXPIRE_DATE, PRICING_DATE, WITHDRAWAL_DATE)"
						+ "values('"
						+ eventId
						+ "', '"
						+ des
						+ "', '"
						+ exc
						+ "', "
						+ high
						+ ", "
						+ low
						+ ", "
						+ fprice
						+ ", '"
						+ filed
						+ "', '"
						+ lead
						+ "', "
						+ oprice
						+ ", "
						+ shares
						+ ", '"
						+ fDate
						+ "', '"
						+ eDate
						+ "', '"
						+ pDate
						+ "', '"
						+ wDate
						+ "')";

				dbHandler.executeQuery(query);

			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private class SplitHandler {
		private int eventId;
		public SplitHandler(int id) {
			this.eventId = id;
		}
		public void handleSplit(Split sp) {
			try {
				double amt = sp.getAmount();
				String date = DateHelper.calendarToStringUTC(sp
						.getAnnouncementDate());
				String eDate = DateHelper.calendarToStringUTC(sp.getExDate());
				String pDate = DateHelper.calendarToStringUTC(sp
						.getPaymentDate());
				String ratio = sp.getRatio();
				String rDate = DateHelper.calendarToStringUTC(sp
						.getRecordDate());

				String query = "insert into trdapi_db.event_details_split (EVENT_ID, AMOUNT, RATIO, ANNOUNCEMENT_DATE, EX_DATE, PAYMENT_DATE, RECORD_DATE)"
						+ "values('"
						+ eventId
						+ "', "
						+ amt
						+ ", '"
						+ ratio
						+ "', '"
						+ date
						+ "', '"
						+ eDate
						+ "','"
						+ pDate
						+ "', '" + rDate + "')";

				dbHandler.executeQuery(query);
			} catch (Exception e) {
				log.warning("Exception Occurred : " + e.getMessage());
				e.printStackTrace();
			}

		}

	}

	private class OrganizationHandler {
		private int eventId;
		public OrganizationHandler(int id) {
			this.eventId = id;
		}
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

				query = "insert into trdapi_db.event_details_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
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
					query = "insert into trdapi_db.event_details_organization(EVENT_ID, ORGANIZATION_NAME, ORGANIZATION_TYPE, ORGANIZATION_VALUE ) "
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
		private int eventId;
		public LocationHandler(int id) {
			this.eventId = id;
		}
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

				String query = "insert into trdapi_db.event_details_location (EVENT_ID, NAME, DESCRIPTION, NOTES, EMAIL, ROOM, URL, ALTERNATIVE_FAX_NUMBER, PRIMARY_FAX_NUMBER, ALTERNATIVE_PHONE_NUMBER, PRIMARY_PHONE_NUMBER ) "
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
					query = "insert into trdapi_db.event_details_location_address(LOCATION_ID, ADDRESS_LINE_1, ADDRESS_LINE_2, CITY, COUNTRY_CODE, POSTAL_CODE, STATE_PROVINCE)"
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
		private int eventId;

		public DialInAccessHandler(String type, int id) {
			this.type = type;
			this.eventId = id;
		}

		public void handleDialInAccess(DialInAccess dia) {

			String apn = dia.getAlternatePhoneNumber();
			String notes = dia.getNotes();
			String pswd = dia.getPassword();
			String pn = dia.getPhoneNumber();
			String status = dia.getStatus().getValue();
			try {
				String query = "";

				query = "insert into trdapi_db.event_details_dial_in_access ( EVENT_ID, DIAL_IN_ACCESS_TYPE, ALTERNATIVE_PHONE_NUMBER, PHONE_NUMBER, NOTES, PASSWORD, STATUS)"
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

					String endDate = DateHelper.calendarToStringUTC(duration.getEndDateTime());
					String startDate = DateHelper.calendarToStringUTC(duration.getStartDateTime());
					int est = (isEst) ? 1 : 0;
					log.info("Dial In Access Id :" + dialInAccessId);
					String query = "insert into trdapi_db.event_details_dail_in_access_duration(DIAL_IN_ACCESS_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
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
		private int eventId;

		public WebcastOverivewHandler(String type, int id) {
			this.type = type;
			this.eventId = id;
		}

		public void handleWCOHandler(WebcastOverview wco) {
			try {

				String webcastId = wco.getWebcastId();
				String status = wco.getStatus().getValue();

				String query = "insert into trdapi_db.event_details_webcast_overview(EVENT_ID, WEBCAST_ID, STATUS, WEBCAST_TYPE)"
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
					//int i = (int) (Math.random() * 1000);
					String query = "insert into trdapi_db.event_details_webcast_overview_duration(WEBCAST_ID, START_DATE_TIME, END_DATE_TIME, START_QUALIFIER, END_QUALIFIER, IS_ESTIMATE)"
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
