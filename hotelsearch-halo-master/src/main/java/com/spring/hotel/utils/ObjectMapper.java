package com.spring.hotel.utils;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.spring.core.model.hotel.HotelSearchRequest;
import com.spring.core.model.hotel.HotelSearchRequest.HotlCodes;
import com.travelport.schema.common_v50_0.BillingPointOfSaleInfo;
import com.travelport.schema.common_v50_0.CoordinateLocation;
import com.travelport.schema.common_v50_0.CreditCard;
import com.travelport.schema.common_v50_0.NextResultReference;
import com.travelport.schema.hotel_v50_0.HotelDetailsModifiers;
import com.travelport.schema.hotel_v50_0.HotelDetailsReq;
import com.travelport.schema.hotel_v50_0.HotelKeywordReq;
import com.travelport.schema.hotel_v50_0.HotelLocation;
import com.travelport.schema.hotel_v50_0.HotelMediaLinksReq;
import com.travelport.schema.hotel_v50_0.HotelProperty;
import com.travelport.schema.hotel_v50_0.HotelRulesReq;
import com.travelport.schema.hotel_v50_0.HotelRulesReq.HotelRulesLookup;
import com.travelport.schema.hotel_v50_0.HotelSearchAvailabilityReq;
import com.travelport.schema.hotel_v50_0.HotelSearchLocation;
import com.travelport.schema.hotel_v50_0.HotelSearchModifiers;
import com.travelport.schema.hotel_v50_0.HotelStay;
import com.travelport.schema.hotel_v50_0.TypeRateRuleDetail;

public class ObjectMapper {

	private final static String ORIGIN_APP = "UAPI";
	private final static String BASE = "USD1.00";

	public static HotelSearchAvailabilityReq searchCity(HotelSearchRequest input) {
		// create HotelSearchAvailabilityReq
		HotelSearchAvailabilityReq hotelSearchAvailabilityReq = new HotelSearchAvailabilityReq();
		try {
			// create HotelLocation
			HotelLocation hotelLocation = new HotelLocation();
			hotelLocation.setLocation(input.getLocat()); // set location to hotelLocation
			// create HotelSearchLocation
			HotelSearchLocation hotelSearchLocation = new HotelSearchLocation();
			hotelSearchLocation.setHotelLocation(hotelLocation); // set hotelLocation to hotelSearchLocation

			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);
			// create hotelSearchModifiers
			HotelSearchModifiers hotelSearchModifiers = new HotelSearchModifiers();
			hotelSearchModifiers.setNumberOfAdults(input.getNumAdt()); // set NumAdt to hotelSearchModifiers
			hotelSearchModifiers.setNumberOfRooms(input.getNumRoom()); // set NumRoom to hotelSearchModifiers
			hotelSearchModifiers.setAvailableHotelsOnly(true);
			hotelSearchModifiers.setReturnAmenities(true);
			hotelSearchModifiers.setPreferredCurrency(input.getCurrency()); // set Currency to hotelSearchModifiers
			// create hotelStay
			HotelStay hotelStay = buildDateInOut(input);
			// check ProviderCode & KeyNextPage # null để tiếp tục search next page
			if (null != input.getProviderCode() && null != input.getKeyNextPage()) {
				// create NextResultReference
				NextResultReference nextResult = new NextResultReference();
				nextResult.setProviderCode(input.getProviderCode()); // set ProviderCode to nextResult
				nextResult.setValue(input.getKeyNextPage()); // set KeyNextPage to nextResult

				hotelSearchAvailabilityReq.getNextResultReference().add(nextResult);

			}

			hotelSearchAvailabilityReq.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			hotelSearchAvailabilityReq.setHotelSearchLocation(hotelSearchLocation);
			hotelSearchAvailabilityReq.setHotelSearchModifiers(hotelSearchModifiers);
			hotelSearchAvailabilityReq.setHotelStay(hotelStay);
			hotelSearchAvailabilityReq.setLanguageCode(input.getLang());
			return hotelSearchAvailabilityReq;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HotelKeywordReq searchKeyWord(HotelSearchRequest input) {
		HotelKeywordReq keyWord = new HotelKeywordReq();
		try {
			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);
			HotelStay hotelStay = buildDateIn(input);

			keyWord.setHotelChain(input.getHotlChain());
			keyWord.setHotelCode(input.getHotlCode());
			keyWord.setCheckinDate(hotelStay.getCheckinDate());
			keyWord.setReturnKeywordList(true);
			keyWord.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			return keyWord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static HotelStay buildDateInOut(HotelSearchRequest input) {
		try {
			HotelStay hotelStay = new HotelStay();

			XMLGregorianCalendar checkInDate;

			String[] checkIn = input.getCheckIn().split("-");
			String[] checkOut = input.getCheckOut().split("-");

			checkInDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			checkInDate.setYear(new BigInteger(checkIn[0]));
			checkInDate.setMonth(Integer.valueOf(checkIn[1]));
			checkInDate.setDay(Integer.valueOf(checkIn[2]));

			XMLGregorianCalendar checkOutDate = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(new GregorianCalendar());
			checkOutDate.setYear(new BigInteger(checkOut[0]));
			checkOutDate.setMonth(Integer.valueOf(checkOut[1]));
			checkOutDate.setDay(Integer.valueOf(checkOut[2]));

			hotelStay.setCheckinDate(checkInDate);
			hotelStay.setCheckoutDate(checkOutDate);
			return hotelStay;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

	private static HotelStay buildDateIn(HotelSearchRequest input) {
		try {
			HotelStay hotelStay = new HotelStay();

			XMLGregorianCalendar checkInDate;

			String[] checkIn = input.getCheckIn().split("-");

			checkInDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			checkInDate.setYear(new BigInteger(checkIn[0]));
			checkInDate.setMonth(Integer.valueOf(checkIn[1]));
			checkInDate.setDay(Integer.valueOf(checkIn[2]));
			return hotelStay;

		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

	private static XMLGregorianCalendar buildExpDate(HotelSearchRequest input) {
		try {
			CreditCard card = new CreditCard();

			XMLGregorianCalendar expDate;

			String[] exp = input.getCheckIn().split("-");

			expDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			expDate.setYear(new BigInteger(exp[0]));
			expDate.setMonth(Integer.valueOf(exp[1]));
			expDate.setDay(Integer.valueOf(exp[2]));

			card.setExpDate(expDate);
			return card.getExpDate();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

	public static HotelDetailsReq searchDetail(HotelSearchRequest input) {
		HotelDetailsReq req = new HotelDetailsReq();
		try {
			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);

			HotelProperty hotelProperty = new HotelProperty();
			hotelProperty.setHotelChain(input.getHotlChain());
			hotelProperty.setHotelCode(input.getHotlCode());

			HotelDetailsModifiers hotelDetailsModifiers = new HotelDetailsModifiers();
			hotelDetailsModifiers.setRateRuleDetail(TypeRateRuleDetail.COMPLETE);
			hotelDetailsModifiers.setNumberOfAdults(input.getNumAdt());
			hotelDetailsModifiers.setNumberOfRooms(input.getNumRoom());
			hotelDetailsModifiers.setPreferredCurrency(input.getCurrency());

			HotelStay hotelStay = buildDateInOut(input);
			hotelDetailsModifiers.setHotelStay(hotelStay);

			req.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			req.setHotelProperty(hotelProperty);
			req.setHotelDetailsModifiers(hotelDetailsModifiers);
			req.setLanguageCode(input.getLang());
			return req;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}

	public static HotelRulesReq searchRuleRoom(HotelSearchRequest input) {
		HotelRulesReq req = new HotelRulesReq();
		try {
			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);

			HotelProperty hotelProperty = new HotelProperty();
			hotelProperty.setHotelChain(input.getHotlChain());
			hotelProperty.setHotelCode(input.getHotlCode());

			HotelRulesLookup hotelRulesLookup = new HotelRulesLookup();
			hotelRulesLookup.setRatePlanType(input.getRatePlanType());
			hotelRulesLookup.setBase(BASE);
			hotelRulesLookup.setHotelProperty(hotelProperty);
			HotelStay hotelStay = buildDateInOut(input);
			hotelRulesLookup.setHotelStay(hotelStay);

			req.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			req.setHotelRulesLookup(hotelRulesLookup);
			return req;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HotelRulesReq searchRuleRoomDetail(HotelSearchRequest input) {
		HotelRulesReq req = new HotelRulesReq();
		try {
			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);

			HotelProperty hotelProperty = new HotelProperty();
			hotelProperty.setHotelChain(input.getHotlChain());
			hotelProperty.setHotelCode(input.getHotlCode());

			HotelRulesLookup hotelRulesLookup = new HotelRulesLookup();
			hotelRulesLookup.setRatePlanType(input.getRatePlanType());
			hotelRulesLookup.setBase(BASE);
			hotelRulesLookup.setHotelProperty(hotelProperty);
			HotelStay hotelStay = buildDateInOut(input);
			hotelRulesLookup.setHotelStay(hotelStay);

			req.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			req.setHotelRulesLookup(hotelRulesLookup);
			return req;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HotelSearchAvailabilityReq searchLocation(HotelSearchRequest input) {
		// TODO Auto-generated method stub

		HotelSearchAvailabilityReq hotelSearchAvailabilityReq = new HotelSearchAvailabilityReq();
		try {

			CoordinateLocation coor = new CoordinateLocation();
			coor.setLatitude(input.getLatitude());
			coor.setLongitude(input.getLongitude());
			HotelSearchLocation hotelSearchLocation = new HotelSearchLocation();
			hotelSearchLocation.setCoordinateLocation(coor);

			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);

			HotelSearchModifiers hotelSearchModifiers = new HotelSearchModifiers();
			hotelSearchModifiers.setNumberOfAdults(input.getNumAdt());
			hotelSearchModifiers.setNumberOfRooms(input.getNumRoom());
			hotelSearchModifiers.setAvailableHotelsOnly(true);
			hotelSearchModifiers.setReturnAmenities(false);
			hotelSearchModifiers.setPreferredCurrency(input.getCurrency());

			HotelStay hotelStay = buildDateInOut(input);

			hotelSearchAvailabilityReq.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			hotelSearchAvailabilityReq.setHotelSearchLocation(hotelSearchLocation);
			hotelSearchAvailabilityReq.setHotelSearchModifiers(hotelSearchModifiers);
			hotelSearchAvailabilityReq.setHotelStay(hotelStay);
			hotelSearchAvailabilityReq.setLanguageCode(input.getLang());
			if (input.getProviderCode() != null && input.getKeyNextPage() != null) {

				NextResultReference nextResult = new NextResultReference();
				nextResult.setProviderCode(input.getProviderCode());
				nextResult.setValue(input.getKeyNextPage());

				hotelSearchAvailabilityReq.getNextResultReference().add(nextResult);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hotelSearchAvailabilityReq;

	}

	public static HotelMediaLinksReq searchMedia(HotelSearchRequest input) {
		HotelMediaLinksReq hotelMediaLinksReq = new HotelMediaLinksReq();
		try {
			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);
			for (HotlCodes hotel : input.getHotlCodes()) {
				HotelProperty hotelProperty = new HotelProperty();
				hotelProperty.setHotelChain(hotel.getHotlChain());
				hotelProperty.setHotelCode(hotel.getHotlCode());
				hotelMediaLinksReq.getHotelProperty().add(hotelProperty);
			}
			hotelMediaLinksReq.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			// check sizeCode # null thì set sizeCode to HotelMediaLinksReq
			if (input.getSizeCode() != null) {
				hotelMediaLinksReq.setSizeCode(input.getSizeCode());
			}
			return hotelMediaLinksReq;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HotelSearchRequest mapSearchDetailFeign(Map<String, Object> inputMap) {
		HotelSearchRequest search = new HotelSearchRequest();
		search.setHotlChain((String) inputMap.get("hotlChain"));
		search.setHotlCode((String) inputMap.get("hotlCode"));
		search.setNumAdt((int) inputMap.get("numAdt"));
		search.setNumRoom((int) inputMap.get("numRoom"));
		search.setRatePlanType((String) inputMap.get("ratePlanType"));
		search.setLang((String) inputMap.get("lang"));
		search.setCheckIn((String) inputMap.get("checkIn"));
		search.setCheckOut((String) inputMap.get("checkOut"));
		search.setCurrency((String) inputMap.get("currency"));
		search.setLang((String) inputMap.get("lang"));

		return search;
	}

	public static HotelMediaLinksReq searchCityImage(List<HotelProperty> hotelproperties, String sizeCode) {
		HotelMediaLinksReq req = new HotelMediaLinksReq();

		BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
		billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);
		req.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
		req.getHotelProperty().addAll(hotelproperties);
		// check sizeCode # null thì set sizeCode to HotelMediaLinksReq
		if (sizeCode != null) {
			req.setSizeCode(sizeCode);
		}
		return req;
	}

	public static HotelMediaLinksReq searchLocationImage(List<HotelProperty> hotelproperties, String sizeCode) {
		HotelMediaLinksReq req = new HotelMediaLinksReq();

		BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
		billingPointOfSaleInfo.setOriginApplication(ORIGIN_APP);
		req.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
		req.getHotelProperty().addAll(hotelproperties);
		// check sizeCode # null thì set sizeCode to HotelMediaLinksReq
		if (sizeCode != null) {
			req.setSizeCode(sizeCode);
		}
		return req;
	}
}
