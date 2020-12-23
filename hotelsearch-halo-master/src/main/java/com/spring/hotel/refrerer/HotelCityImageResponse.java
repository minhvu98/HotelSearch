package com.spring.hotel.refrerer;

import com.spring.core.model.common.BaseEntity;
import com.travelport.schema.hotel_v50_0.BaseHotelSearchRsp;
import com.travelport.schema.hotel_v50_0.HotelMediaLinksRsp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HotelCityImageResponse extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	 private BaseHotelSearchRsp hotels;
	private HotelMediaLinksRsp medias;
}
