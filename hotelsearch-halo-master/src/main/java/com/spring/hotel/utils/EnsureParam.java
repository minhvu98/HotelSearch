package com.spring.hotel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.spring.core.model.hotel.HotelSearchRequest;
import com.spring.core.model.hotel.HotelSearchRequest.HotlCodes;
import com.spring.core.utils.CollectionUtils;
import com.spring.core.utils.DateUtils;

public class EnsureParam {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DATEFORMAT);

	// check search city locat & numAdt & numRoom & checkIn & checkOut & Curency &
	// lang # null
	public static boolean checkSearchCity(HotelSearchRequest input) {
		if (input.getLocat() == null || input.getNumAdt() <= 0 || input.getNumRoom() <= 0 || input.getCheckIn() == null
				|| input.getCheckOut() == null || input.getCurrency() == null || input.getLang() == null) {
			return false;
		}
		boolean result = checkDate(input.getCheckIn(), input.getCheckOut());
		return result;
	}

	public static boolean checkKeyWord(HotelSearchRequest input) {
//		if (input.getHotlChain() == null || input.getHotlCode() == null) {
//			return false;
//		}
		return true;
	}

	public static boolean checkSearchDetail(HotelSearchRequest input) {
		if (input.getHotlChain() == null || input.getHotlCode() == null || input.getNumAdt() <= 0
				|| input.getNumRoom() <= 0 || input.getCheckIn() == null || input.getCheckOut() == null
				|| input.getCurrency() == null || input.getLang() == null) {
			return false;
		}

		boolean result = checkDate(input.getCheckIn(), input.getCheckOut());

		return result;
	}

	public static boolean checkSearchRuleRoom(HotelSearchRequest input) {
		if (input.getHotlChain() == null || input.getHotlCode() == null || input.getRatePlanType()== null) {
			return false;
		}
		return true;
	}

	public static boolean checkSearchLocation(HotelSearchRequest input) {
		if (input.getLatitude() == 0 || input.getLongitude() == 0 || input.getNumAdt() <= 0 || input.getNumRoom() <= 0
				|| input.getCheckIn() == null || input.getCheckOut() == null) {
			return false;
		}
		boolean result = checkDate(input.getCheckIn(), input.getCheckOut());
		return result;
	}

	public static boolean checkSearchDetailFeign(Map<String, Object> input) {
		if (input.get("hotlChain") == null || input.get("hotlCode") == null || ((int) input.get("numAdt")) <= 0
				|| ((int) input.get("numRoom")) <= 0 || input.get("checkIn") == null || input.get("checkOut") == null
				|| input.get("ratePlanType") == null || input.get("currency") == null) {
			return false;
		}
		return true;
	}

	public static boolean checkDate(String checkIn, String checkOut) {
		try {
			Date checkInD = dateFormat.parse(checkIn.trim());
			Date checkOutD = dateFormat.parse(checkOut.trim());
			if (checkInD.before(checkOutD)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkSearchMedia(HotelSearchRequest input) {
		if (CollectionUtils.isEmpty(input.getHotlCodes()) || input.getHotlCodes().get(0).getHotlChain() == null
				|| input.getHotlCodes().get(0).getHotlCode() == null) {
			return false;
		}

		for (HotlCodes holt : input.getHotlCodes()) {
			if (holt.getHotlChain() == null || holt.getHotlCode() == null) {
				return false;
			}
		}
		return true;

	}

}
