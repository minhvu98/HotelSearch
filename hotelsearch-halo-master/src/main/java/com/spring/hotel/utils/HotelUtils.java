package com.spring.hotel.utils;

import com.travelport.schema.hotel_v50_0.BaseHotelSearchRsp;

public class HotelUtils {

	public static BaseHotelSearchRsp sortByPrice(BaseHotelSearchRsp result) {
		result.getHotelSearchResult().stream().sorted((hotel1, hotel2) -> hotel1.getRateInfo().get(0).getMinimumAmount()
				.compareTo(hotel2.getRateInfo().get(0).getMinimumAmount()));
		return result;
	}
	
	
	
}
