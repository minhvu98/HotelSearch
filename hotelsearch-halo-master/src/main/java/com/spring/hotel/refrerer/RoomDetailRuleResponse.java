package com.spring.hotel.refrerer;

import com.spring.core.model.common.BaseEntity;
import com.travelport.schema.hotel_v50_0.HotelDetailsRsp;
import com.travelport.schema.hotel_v50_0.HotelRulesRsp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoomDetailRuleResponse extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private HotelDetailsRsp detailRsp;
	private	HotelRulesRsp ruleResp ;
}
