package com.spring.hotel.utils;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.travelport.schema.common_v50_0.BillingPointOfSaleInfo;
import com.travelport.schema.common_v50_0.CreditCard;
import com.travelport.schema.common_v50_0.Guarantee;
import com.travelport.schema.hotel_v50_0.GuestInformation;
import com.travelport.schema.hotel_v50_0.HotelProperty;
import com.travelport.schema.hotel_v50_0.HotelStay;
import com.travelport.schema.universal_v50_0.HotelCreateReservationReq;

public class CreateReservationRequest {
	public static HotelCreateReservationReq createReservation() {
		HotelCreateReservationReq createReservationReq = new HotelCreateReservationReq();
		try {

			BillingPointOfSaleInfo billingPointOfSaleInfo = new BillingPointOfSaleInfo();
			billingPointOfSaleInfo.setOriginApplication("UAPI");

//			BookingTraveler bookingtraveler = new BookingTraveler();
//			BookingTravelerName bookingTravelerName = new BookingTravelerName();
//			bookingTravelerName.setFirst("Harry K");
//			bookingTravelerName.setLast("Lobo");
//			bookingTravelerName.setPrefix("Mr");
//
//			PhoneNumber phoneNumber = new PhoneNumber();
//			phoneNumber.setLocation("Den");
//			phoneNumber.setCountryCode("1");
//			phoneNumber.setAreaCode("303");
//			phoneNumber.setNumber("123456789");
//
//			Email email = new Email();
//			email.setEmailID("johnsmith@travelportuniversalapidemo.com");
//
//			bookingtraveler.setBookingTravelerName(bookingTravelerName);

//			HotelRateDetail hotelRateDetail = new HotelRateDetail();
//			hotelRateDetail.setRatePlanType("A1KRN3");

			HotelProperty hotelProperty = createHotelProperty("hotelChain", "hotelCode");

			GuestInformation guestInformation = new GuestInformation();
			guestInformation.setNumberOfRooms(1);

			Guarantee guarantee = createGuarantee( "expDate", "guatType","carType", "number", "cvv");

			HotelStay hotelStay = createHotelStay("dateIn","dateOut");

			createReservationReq.setHotelProperty(hotelProperty);
			createReservationReq.setGuestInformation(guestInformation);
			createReservationReq.setGuarantee(guarantee);
			createReservationReq.setHotelStay(hotelStay);
			createReservationReq.setBillingPointOfSaleInfo(billingPointOfSaleInfo);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return createReservationReq;

	}

	private static HotelProperty createHotelProperty(String hotelChain, String hotelCode) {
		HotelProperty hotelProperty = new HotelProperty();
		hotelProperty.setHotelChain("58");
		hotelProperty.setHotelCode("96093");
		return hotelProperty;
	}

	private static HotelStay createHotelStay(String dateIn,String dateOut) throws DatatypeConfigurationException {
		HotelStay hotelStay = new HotelStay();

		XMLGregorianCalendar checkInDate;
		checkInDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		checkInDate.setYear(new BigInteger("2021"));
		checkInDate.setMonth(Integer.valueOf(2));
		checkInDate.setDay(Integer.valueOf(1));

		XMLGregorianCalendar checkOutDate = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(new GregorianCalendar());
		checkOutDate.setYear(new BigInteger("2021"));
		checkOutDate.setMonth(Integer.valueOf(2));
		checkOutDate.setDay(Integer.valueOf(3));

		hotelStay.setCheckinDate(checkInDate);
		hotelStay.setCheckoutDate(checkOutDate);
		return hotelStay;
	}

	private static Guarantee createGuarantee(String expDate,String guatType,String carType,String number,String cvv) throws DatatypeConfigurationException {
		
		CreditCard card = new CreditCard();
		XMLGregorianCalendar expDate1;
		expDate1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		expDate1.setYear(new BigInteger("2021"));
		expDate1.setMonth(Integer.valueOf(2));
		expDate1.setDay(Integer.valueOf(2));

		Guarantee guarantee = new Guarantee();
		guarantee.setType("Deposit");
		card.setType("VI");
		card.setNumber("4444333322221111");
		card.setCVV("345");
		card.setExpDate(expDate1);
		guarantee.setCreditCard(card);
		return guarantee;
	}
}
