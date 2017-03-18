package com.fx.ws.consumers;

import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.axis2.databinding.types.HexBinary;

import com.fx.model.AuthorizationToken;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.ApplicationIDType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.Authorization;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.AuthorizationType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.TokenValueType;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlinesRequest;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlines_Request_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEconomicHeadlines_Response_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverviewRequest;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverviewResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverview_Request_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesOverview_Response_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesRequest;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlinesResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlines_Request_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventHeadlines_Response_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventRequest;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEventResponse;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEvent_Request_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.GetEvent_Response_1;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1.httpandrkdtoken.StreetEvents_1Stub;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.DateTimeRange;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.OutputSymbolType;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.OutputSymbolTypes;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.SortCriterion;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.SortDirection;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.SymbolCriterion;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_criteria.Symbols;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Classifications;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.CountryCodes;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EconomicClassification;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EventType;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.EventTypes;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.IndustryCodes;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.Pagination;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.RecordsPerGroup;
import com.reuters.www.ns._2009._01._26.webservices.rkd.streetevents_1_events.SchemedIndustryCodes;

public class StreetsEventWSConsumer {
	public static Logger log = Logger.getLogger(StreetsEventWSConsumer.class
			.getName());

	private String appId;
	private AuthorizationToken token;

	public StreetsEventWSConsumer(AuthorizationToken token, String appId)
			throws Exception {
		this.token = token;
		this.appId = appId;
	}

	private StreetEvents_1Stub getSEStub() throws Exception {
		StreetEvents_1Stub stub = new StreetEvents_1Stub();
		stub._getServiceClient().engageModule("addressing");
		return stub;
	}

	public GetEconomicHeadlinesResponse getEconomicHeadlines(int pageNo,
			int records, Calendar from, Calendar to) {
		try {
			StreetEvents_1Stub stub = this.getSEStub();
			Authorization auth = getAuthorization();
			GetEconomicHeadlines_Request_1 req1 = this.getEcoHdlReq(pageNo,
					records, from, to);
			GetEconomicHeadlines_Response_1 res1 = stub.getEconomicHeadlines_1(
					req1, null, auth);
			GetEconomicHeadlinesResponse res = res1
					.getGetEconomicHeadlines_Response_1();
			return res;
		} catch (Exception e) {
			log.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public GetEventHeadlinesResponse getEventHeadlines(int pageNo, int records,
			Calendar from, Calendar to) {
		try {
			StreetEvents_1Stub stub = this.getSEStub();
			Authorization auth = getAuthorization();
			GetEventHeadlines_Request_1 req1 = this.getEventHdlReq(pageNo,
					records, from, to);
			GetEventHeadlines_Response_1 res = stub.getEventHeadlines_1(req1,
					null, auth);
			return res.getGetEventHeadlines_Response_1();
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public GetEventHeadlinesOverviewResponse getEventHeadlinesOverview(
			int recordsPerGroup) {
		try {
			StreetEvents_1Stub stub = this.getSEStub();
			Authorization auth = getAuthorization();

			GetEventHeadlinesOverview_Request_1 req1 = this
					.getEventHeadlinesOverviewReq(recordsPerGroup);

			GetEventHeadlinesOverview_Response_1 res = stub
					.getEventHeadlinesOverview_1(req1, null, auth);
			return res.getGetEventHeadlinesOverview_Response_1();
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public GetEventResponse getEventDetailResponse(int eventId) {
		try {

			StreetEvents_1Stub stub = this.getSEStub();
			Authorization auth = getAuthorization();

			GetEvent_Request_1 req1 = this.getEventReq(eventId);

			GetEvent_Response_1 res = stub.getEvent_1(req1, null, auth);
			return res.getGetEvent_Response_1();

		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private GetEvent_Request_1 getEventReq(int eventId) {
		
		GetEvent_Request_1 req1 = new GetEvent_Request_1();
		GetEventRequest req = new GetEventRequest();
		req.setEventId(eventId);
		
		req1.setGetEvent_Request_1(req);
		return req1;
	}

	private GetEventHeadlinesOverview_Request_1 getEventHeadlinesOverviewReq(
			int rpg) {
		GetEventHeadlinesOverview_Request_1 req1 = new GetEventHeadlinesOverview_Request_1();

		GetEventHeadlinesOverviewRequest req = new GetEventHeadlinesOverviewRequest();
		req.setEventTypes(null);
		req.setRecordsPerGroup(this.getRecordPerGroup(rpg));
		req.setSymbol(this.getSymbolCriterion());
		req.setUTCIndicatorInResponse(false);

		req1.setGetEventHeadlinesOverview_Request_1(req);
		return req1;
	}

	private Authorization getAuthorization() {

		ApplicationIDType idType = new ApplicationIDType();
		idType.setApplicationIDType(appId);

		HexBinary binary = new HexBinary(token.getTokenString());
		TokenValueType tvType = new TokenValueType();
		tvType.setTokenValueType(binary);

		AuthorizationType authType = new AuthorizationType();
		authType.setApplicationID(idType);
		authType.setToken(tvType);

		Authorization auth = new Authorization();
		auth.setAuthorization(authType);
		return auth;
	}

	private GetEconomicHeadlines_Request_1 getEcoHdlReq(int pageNo,
			int records, Calendar from, Calendar to) {
		GetEconomicHeadlinesRequest req = new GetEconomicHeadlinesRequest();
		req.setDateTimeRange(this.getDateTimeRange(from, to));
		req.setPagination(this.getPagination(pageNo, records));
		req.setSortOrder(this.getSortCrt());
		req.setClassifications(null); // this.getClassifications()
		req.setUTCIndicatorInResponse(false);
		req.setMarketCountryCodes(null); // this.getCodes()

		GetEconomicHeadlines_Request_1 req1 = new GetEconomicHeadlines_Request_1();
		req1.setGetEconomicHeadlines_Request_1(req);

		return req1;
	}

	private GetEventHeadlines_Request_1 getEventHdlReq(int pageNo, int records,
			Calendar from, Calendar to) {
		GetEventHeadlinesRequest req = new GetEventHeadlinesRequest();
		req.setDateTimeRange(this.getDateTimeRange(from, to));
		req.setEventTypes(null); // this.getEventTypes()
		req.setIndustryCodes(null); // this.getSICodes()
		req.setMarketCountryCodes(null); // this.getCodes()
		req.setOutputSymbolTypes(null); // this.getOSTypes()
		req.setPagination(this.getPagination(pageNo, records));
		req.setSortOrder(this.getSortCrt());
		req.setSymbols(null); // this.getSymbols()
		req.setUTCIndicatorInResponse(true);

		GetEventHeadlines_Request_1 req1 = new GetEventHeadlines_Request_1();
		req1.setGetEventHeadlines_Request_1(req);

		return req1;

	}

	private DateTimeRange getDateTimeRange(Calendar fromDate, Calendar toDate) {
		DateTimeRange dateTimeRange = new DateTimeRange();
		dateTimeRange.setFrom(fromDate);
		dateTimeRange.setTo(toDate);
		return dateTimeRange;
	}

	private Pagination getPagination(int pageNo, int records) {
		Pagination pgn = new Pagination();
		pgn.setPageNumber(pageNo);
		pgn.setRecordsPerPage(records);
		return pgn;
	}

	private SortCriterion getSortCrt() {
		SortCriterion sc = new SortCriterion();
		sc.setBy("Date");
		sc.setDirection(SortDirection.Ascending);
		return sc;
	}

	private Classifications getClassifications() {
		EconomicClassification[] ec = new EconomicClassification[1];
		ec[0] = EconomicClassification.ConsumerSector;

		Classifications cls = new Classifications();
		cls.setClassification(ec);

		return cls;
	}

	private CountryCodes getCodes() {
		String[] codes = new String[1];
		codes[0] = "US";
		CountryCodes cc = new CountryCodes();
		cc.setCountryCode(codes);
		return cc;
	}

	private EventTypes getEventTypes() {
		EventType[] type = new EventType[1];
		type[0] = EventType.Conferences;

		EventTypes types = new EventTypes();
		types.setEventType(type);
		return types;
	}

	private SchemedIndustryCodes getSICodes() {
		String[] codes = new String[1];
		codes[0] = "1";

		IndustryCodes icode = new IndustryCodes();
		icode.setIndustryCode(codes);

		SchemedIndustryCodes siCodes = new SchemedIndustryCodes();
		siCodes.setIndustryCodes(icode);
		siCodes.setScheme("GICS");
		return siCodes;
	}

	private OutputSymbolTypes getOSTypes() {

		OutputSymbolType[] array = new OutputSymbolType[2];
		array[0] = OutputSymbolType.ExchangeTicker;
		array[1] = OutputSymbolType.RIC;

		OutputSymbolTypes types = new OutputSymbolTypes();
		types.setOutputSymbolType(array);

		return types;
	}

	private Symbols getSymbols() {
		SymbolCriterion[] array = new SymbolCriterion[1];
		SymbolCriterion sc = new SymbolCriterion();
		sc.setType("CIK");
		sc.setValue("CIK");
		array[0] = sc;
		Symbols sb = new Symbols();
		sb.setSymbol(array);

		return sb;
	}

	private SymbolCriterion getSymbolCriterion() {
		SymbolCriterion sc = new SymbolCriterion();
		sc.setType("RIC");
		sc.setValue("RIC");
		return sc;
	}

	private RecordsPerGroup getRecordPerGroup(int value) {
		RecordsPerGroup rpg = new RecordsPerGroup();
		rpg.setRecordsPerGroup(value);
		return rpg;
	}
}
