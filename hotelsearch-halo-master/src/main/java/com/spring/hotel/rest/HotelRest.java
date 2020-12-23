package com.spring.hotel.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.rest.helper.JsonUtils;
import com.core.rest.utils.BaseRestService;
import com.core.rest.utils.HttpStatusUtils;
import com.spring.core.model.hotel.HotelSearchRequest;
import com.spring.core.model.hotel.HotelSearchResponse;
import com.spring.hotel.refrerer.HotelCityImageResponse;
import com.spring.hotel.service.HotelSearchRequestService;
import com.spring.hotel.service.HotelSearchResponseService;
import com.spring.hotel.utils.EnsureParam;
import com.spring.hotel.utils.HotelUtils;
import com.spring.hotel.utils.ObjectMapper;
import com.spring.travelport.service.GatewayTravelport;
import com.spring.travelport.utils.TravelportAction;
import com.travelport.schema.common_v50_0.BaseRsp;
import com.travelport.schema.hotel_v50_0.BaseHotelSearchRsp;
import com.travelport.schema.hotel_v50_0.HotelDetailsReq;
import com.travelport.schema.hotel_v50_0.HotelKeywordReq;
import com.travelport.schema.hotel_v50_0.HotelKeywordRsp;
import com.travelport.schema.hotel_v50_0.HotelMediaLinksReq;
import com.travelport.schema.hotel_v50_0.HotelMediaLinksRsp;
import com.travelport.schema.hotel_v50_0.HotelProperty;
import com.travelport.schema.hotel_v50_0.HotelRulesReq;
import com.travelport.schema.hotel_v50_0.HotelSearchAvailabilityReq;
import com.travelport.schema.hotel_v50_0.HotelSearchResult;

/**
 * @author Xuan Hoang
 *
 */
@RestController
@RequestMapping("/hotl")
public class HotelRest extends BaseRestService {

	@Autowired
	private GatewayTravelport gate;

	@Autowired
	private HotelSearchRequestService requestService;

	@Autowired
	private HotelSearchResponseService responseService;

	@RequestMapping(value = "/searchCity/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchCityV1(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		// check ensurParam
		boolean ensurParm = EnsureParam.checkSearchCity(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}

		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);

		// create HotelSearchAvailabilityReq
		HotelSearchAvailabilityReq request = ObjectMapper.searchCity(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		// create BaseRsp
		BaseHotelSearchRsp result = (BaseHotelSearchRsp) gate
				.callHotelService(TravelportAction.HOTEL_SEARCH_AVAILABILITY, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}

		result = HotelUtils.sortByPrice(result);

		saveResponse(result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchDetail/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchDetailV1(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		// check ensurParam
		boolean ensurParm = EnsureParam.checkSearchDetail(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);
		// create HotelSearchAvailabilityReq
		HotelDetailsReq request = ObjectMapper.searchDetail(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		// create BaseRsp
		BaseRsp result = (BaseRsp) gate.callHotelService(TravelportAction.HOTEL_SEARCH_DETAIL, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		saveResponse((BaseHotelSearchRsp) result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchRuleRoom/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchRuleRoomV1(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		// check ensurParam
		boolean ensurParm = EnsureParam.checkSearchRuleRoom(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);
		// create HotelSearchAvailabilityReq
		HotelRulesReq request = ObjectMapper.searchRuleRoom(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		// create BaseRsp
		BaseRsp result = (BaseRsp) gate.callHotelService(TravelportAction.HOTEL_SEARCH_RULE, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		saveResponse(result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchLocation/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchLocationV1(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		// check ensurParam
		boolean ensurParm = EnsureParam.checkSearchLocation(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);
		// create HotelSearchAvailabilityReq
		HotelSearchAvailabilityReq request = ObjectMapper.searchLocation(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		// create BaseRsp
		BaseHotelSearchRsp result = (BaseHotelSearchRsp) gate
				.callHotelService(TravelportAction.HOTEL_SEARCH_AVAILABILITY, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		saveResponse(result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchMedia/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchMediaV1(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		boolean ensurParm = EnsureParam.checkSearchMedia(input);

		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);
		HotelMediaLinksReq request = ObjectMapper.searchMedia(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		BaseRsp result = (BaseRsp) gate.callHotelService(TravelportAction.HOTEL_SEARCH_MEDIA, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		saveResponse(result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchKeyWord/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchKeyWord(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		boolean ensurParm = EnsureParam.checkKeyWord(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		HotelKeywordReq request = ObjectMapper.searchKeyWord(input);
		if (request == null) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		HotelKeywordRsp result = (HotelKeywordRsp) gate.callHotelService(TravelportAction.HOTEL_KEYWORD, request);
		if (result == null) {
			return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
		}
		saveResponse(result, id);
		return super.success(result);
	}

	@RequestMapping(value = "/searchCityImage/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchCityImage(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		boolean ensurParm = EnsureParam.checkSearchCity(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		try {
			if (pn100 != null) {
				input.setPn100(pn100);
			}
			ObjectId id = new ObjectId();
			input.set_id(id);
			saveRequest(input);

			HotelSearchAvailabilityReq request = ObjectMapper.searchCity(input);

			CompletableFuture<Object> resultCity = CompletableFuture.supplyAsync(() -> {
				BaseHotelSearchRsp searchCity = (BaseHotelSearchRsp) gate
						.callHotelService(TravelportAction.HOTEL_SEARCH_AVAILABILITY, request);
				return searchCity;
			}).thenApply(hotels -> {
				HotelCityImageResponse result = new HotelCityImageResponse();
				if (hotels == null) {
					return super.error(HttpStatusUtils.NO_CONTENT.value(),
							HttpStatusUtils.NO_CONTENT.getReasonPhrase());
				}
				hotels = HotelUtils.sortByPrice(hotels);
				result.setHotels((BaseHotelSearchRsp) hotels);

				List<HotelProperty> hotelproperties = new ArrayList<>();
				for (HotelSearchResult hotelResul : ((BaseHotelSearchRsp) hotels).getHotelSearchResult()) {
					hotelproperties.addAll(hotelResul.getHotelProperty());
				}
				HotelMediaLinksReq req = ObjectMapper.searchCityImage(hotelproperties, input.getSizeCode());
				BaseRsp media = gate.callHotelService(TravelportAction.HOTEL_SEARCH_MEDIA, req);
				if (media != null) {
					result.setMedias((HotelMediaLinksRsp) media);
				}
				saveResponse(result, id);
				return result;
			});

			return resultCity.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
	}

	@RequestMapping(value = "/searchLocatImage/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object searchLocationImage(@RequestParam(value = "pn100", required = false) String pn100,
			@RequestBody HotelSearchRequest input) {
		boolean ensurParm = EnsureParam.checkSearchLocation(input);
		if (!ensurParm) {
			return super.error(HttpStatusUtils.NO_PARAM.value(), HttpStatusUtils.NO_PARAM.getReasonPhrase());
		}
		if (pn100 != null) {
			input.setPn100(pn100);
		}
		ObjectId id = new ObjectId();
		input.set_id(id);
		saveRequest(input);
		try {
			HotelSearchAvailabilityReq request = ObjectMapper.searchLocation(input);
			CompletableFuture<Object> resultLocation = CompletableFuture.supplyAsync(() -> {
				BaseHotelSearchRsp searchLocation = (BaseHotelSearchRsp) gate
						.callHotelService(TravelportAction.HOTEL_SEARCH_AVAILABILITY, request);
				return searchLocation;
			}).thenApply(hotel -> {
				HotelCityImageResponse result = new HotelCityImageResponse();
				if (hotel == null) {
					return super.error(HttpStatusUtils.NO_CONTENT.value(),
							HttpStatusUtils.NO_CONTENT.getReasonPhrase());
				}
				result.setHotels((BaseHotelSearchRsp) hotel);

				List<HotelProperty> hotelProperties = new ArrayList<>();
				for (HotelSearchResult hotelResul : ((BaseHotelSearchRsp) hotel).getHotelSearchResult()) {
					hotelProperties.addAll(hotelResul.getHotelProperty());
				}
				HotelMediaLinksReq req = ObjectMapper.searchLocationImage(hotelProperties, input.getSizeCode());
				BaseRsp media = gate.callHotelService(TravelportAction.HOTEL_SEARCH_MEDIA, req);
				if (media != null) {
					result.setMedias((HotelMediaLinksRsp) media);
				}
				saveResponse(result, id);
				return result;
			});
			return resultLocation.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(HttpStatusUtils.NO_CONTENT.value(), HttpStatusUtils.NO_CONTENT.getReasonPhrase());
	}

	private void saveRequest(HotelSearchRequest input) {
		CompletableFuture.runAsync(() -> {
			requestService.saveRequest(input);
		});
	}

	private <T> void saveResponse(T result, ObjectId id) {
		try {
			HotelSearchResponse response = new HotelSearchResponse();
			response.set_id(id);
			response.setHotelResp(JsonUtils.serialize(result));
			CompletableFuture.runAsync(() -> {
				responseService.saveResponse(response);
			});
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}