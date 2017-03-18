package com.fx.request.initializer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.fx.database.DatabaseHandler;

public class EventDetailRequestInitializer {
	public static Logger log = Logger
			.getLogger(EventDetailRequestInitializer.class.getName());

	public ArrayList<Integer> getEventIds() {
		try {
			
			String query = "select event_id from trdapi_db.event_headlines";
			DatabaseHandler dbHandler = new DatabaseHandler();
			ResultSet res = dbHandler.getData(query);
			ArrayList<Integer> list = new ArrayList<Integer>();
			
			while (res.next() ) {
				int id = Integer.parseInt(res.getString("EVENT_ID"));
				list.add(id);
			}
			return list;
		} catch (Exception e) {
			log.warning("Exception Occurred : " + e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<Integer>();
	}
}
