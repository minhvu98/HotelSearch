
package com.spring.hotel.rest.feign;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.core.rest.utils.BaseRestService;
import com.spring.core.model.hotel.HotelSearchRequest;
import com.spring.core.utils.CollectionUtils;
import com.spring.hotel.refrerer.RoomDetailRuleResponse;
import com.spring.hotel.utils.EnsureParam;
import com.spring.hotel.utils.ObjectMapper;
import com.spring.travelport.service.GatewayTravelport;
import com.spring.travelport.utils.TravelportAction;
import com.travelport.schema.hotel_v50_0.HotelDetailsReq;
import com.travelport.schema.hotel_v50_0.HotelDetailsRsp;
import com.travelport.schema.hotel_v50_0.HotelRateDetail;
import com.travelport.schema.hotel_v50_0.HotelRulesReq;
import com.travelport.schema.hotel_v50_0.HotelRulesRsp;

@RestController
@RequestMapping("/fegHotl")
public class FeignHotelRest extends BaseRestService {

	@Autowired
	private GatewayTravelport gate;

//    "hotlChain" :"SB",
//    "hotlCode" :"96093",
//    "numAdt":1,
//    "numRoom":1,
//    "checkIn":"2021-02-01",
//    "checkOut":"2021-02-03",
//    "ratePlanType":"A1KRN3",
//    "lang":"EN",
//    "currency" :"USD"
	@RequestMapping(value = "/roomlRulDtail/v1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object roomRuleDetail(@RequestBody Map<String, Object> inputMap) {
		try {
			RoomDetailRuleResponse result = new RoomDetailRuleResponse();
			boolean ensurParm = EnsureParam.checkSearchDetailFeign(inputMap);
			if (!ensurParm) {
				return null;
			}
			HotelSearchRequest input = ObjectMapper.mapSearchDetailFeign(inputMap);
			HotelRulesReq ruleReq = ObjectMapper.searchRuleRoomDetail(input);
			HotelDetailsReq dtalReq = ObjectMapper.searchDetail(input);
			
			// completable cho HotelDetail
			CompletableFuture<HotelDetailsRsp> resultDetail = CompletableFuture.supplyAsync(() -> {
				HotelRateDetail resultHotelRateDetail = null;
				HotelDetailsRsp detailRsp = (HotelDetailsRsp) gate
						.callHotelService(TravelportAction.HOTEL_SEARCH_DETAIL, dtalReq);
				if (detailRsp != null && detailRsp.getRequestedHotelDetails() != null
						&& !CollectionUtils.isEmpty(detailRsp.getRequestedHotelDetails().getHotelRateDetail())) {
					result.setDetailRsp(detailRsp);
					List<HotelRateDetail> fhotlRates = detailRsp.getRequestedHotelDetails().getHotelRateDetail();
					for (HotelRateDetail rate : fhotlRates) {
						if (rate.getRatePlanType().equals(inputMap.get("ratePlanType"))) { // check hotel rate detail
																							// with
							resultHotelRateDetail = rate;
							break;
						}
					}
					detailRsp.getRequestedHotelDetails().getHotelRateDetail().removeAll(fhotlRates);
					detailRsp.getRequestedHotelDetails().getHotelRateDetail().add(resultHotelRateDetail);
				}
				return detailRsp;
			});
			// completable cho Room Rule
			CompletableFuture<HotelRulesRsp> resultRule = CompletableFuture.supplyAsync(() -> {
				HotelRulesRsp ruleRsp = (HotelRulesRsp) gate.callHotelService(TravelportAction.HOTEL_SEARCH_RULE,
						ruleReq);
				return ruleRsp;
			});

			CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(resultDetail, resultRule);
			combinedFuture.get();
			
			result.setDetailRsp(resultDetail.get());
			result.setRuleResp(resultRule.get());
			return result;
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		return null;
	}
}
