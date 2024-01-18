package pw.proj.letsmeet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import pw.proj.letsmeet.config.ApplicationProperties;
import pw.proj.letsmeet.dto.*;
import pw.proj.letsmeet.service.ZoomService;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ZoomServiceImpl implements ZoomService {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ZoomTokenDTO zoomTokenDTO;

	private long tokenExpiryTime;

	@Override
	public ZoomMeetingObjectDTO createMeeting(ZoomMeetingObjectDTO zoomMeetingObjectDTO) {
		String apiUrl = "https://api.zoom.us/v2/users/" + applicationProperties.getZoomAccountId() + "/meetings";

		// replace with your password or method
		zoomMeetingObjectDTO.setPassword("!3tsm33T");
		// replace email with your email
		zoomMeetingObjectDTO.setHost_email("letsmeetmanagement@gmail.com");

		// Optional Settings for host and participant related options
		ZoomMeetingSettingsDTO settingsDTO = new ZoomMeetingSettingsDTO();
		settingsDTO.setJoin_before_host(false);
		settingsDTO.setParticipant_video(true);
		settingsDTO.setHost_video(false);
		settingsDTO.setAuto_recording("cloud");
		settingsDTO.setMute_upon_entry(true);
		zoomMeetingObjectDTO.setSettings(settingsDTO);

		zoomMeetingObjectDTO.setAgenda("My Meeting");
		zoomMeetingObjectDTO.setDuration(60);
		zoomMeetingObjectDTO.setStart_time("2024-03-25T07:32:55Z");
		zoomMeetingObjectDTO.setTimezone("America/Los_Angeles");
		zoomMeetingObjectDTO.setTopic("My Meeting");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Authorization", "Bearer " + getAuthenticationToken());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		headers.add("content-type", "application/json");
		HttpEntity<ZoomMeetingObjectDTO> httpEntity = new HttpEntity<>(zoomMeetingObjectDTO, headers);
		ResponseEntity<ZoomMeetingObjectDTO> zEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, ZoomMeetingObjectDTO.class);
		if(zEntity.getStatusCodeValue() == 201) {
			return zEntity.getBody();
		} else {
			System.out.println("Error while creating zoom meeting {}" + zEntity.getStatusCode());
		}
		return zoomMeetingObjectDTO;
	}

	/**
	 * Request to list all meetings by userId/email of the user
	 *
	 * @param userIdOrEmail optional userId/email value
	 *
	 * @param meetingType scheduled/live/upcoming
	 *
	 * @return zoomMeetingsListResponseDTO the dto containing list of meetings
	 */
	@Override
	public ZoomMeetingsListResponseDTO listMeetings(String userIdOrEmail, String meetingType) {
		// replace me with user id in case, listing meetings for a different user than admin
		String listMeetingUrl = "https://api.zoom.us/v2/users/me/meetings";
		// replace either user Id or email here with your user id/email
		if(userIdOrEmail != null) {
			listMeetingUrl = "https://api.zoom.us/v2/users/"+ userIdOrEmail +"/meetings";
		}
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Authorization", "Bearer " + getAuthenticationToken());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		headers.add("content-type", "application/json");
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(listMeetingUrl);
		if(meetingType != null) {
			urlBuilder.queryParam("type", meetingType);
		}
//		ResponseEntity<ZoomMeetingsListResponseDTO> response = restTemplate
//				.exchange(urlBuilder.toUriString(), HttpMethod.GET, requestEntity, ZoomMeetingsListResponseDTO.class);
//		if(response.getStatusCodeValue() == 200) {
//			return response.getBody();
//		} else if (response.getStatusCodeValue() == 404) {
//			throw new RuntimeException("User id or email not found for supplied value");
//		}
		return null;
	}

	/**
	 * Get ZoomMeeting by Meeting id
	 *
	 * @param meetingId the id of meetting from Zoom
	 * @return the meetingObjectDTO with meeting details
	 */
	@Override
	public ZoomMeetingObjectDTO getZoomMeetingById(String meetingId) {
		String getMeetingUrl = "https://api.zoom.us/v2/meetings/" + meetingId;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Authorization", "Bearer " + getAuthenticationToken());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		headers.add("content-type", "application/json");
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<ZoomMeetingObjectDTO> zoomEntityRes =  restTemplate
				.exchange(getMeetingUrl, HttpMethod.GET, requestEntity, ZoomMeetingObjectDTO.class);
		if(zoomEntityRes.getStatusCodeValue() == 200) {
			return zoomEntityRes.getBody();
		} else if (zoomEntityRes.getStatusCodeValue() == 404) {
			throw new RuntimeException("User id or email not found for supplied value");
		}
		return null;
	}


	/**
	 * Generate JWT token for Zoom using api credentials
	 *
	 * @return JWT Token String
	 */
//	private String generateZoomJWTToken() {
//		String tokenUrl = "https://zoom.us/oauth/token";
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		String credentials = applicationProperties.getZoomApiKey() + ":" + applicationProperties.getZoomApiSecret();
////		TODO: spróbować ustawić pola ZoomGetAuthDTO na public!
//		ZoomGetAuthDTO zoomGetAuthDTO = new ZoomGetAuthDTO("account_credentials", applicationProperties.getZoomAccountId());
//
////		JSONObject request = new JSONObject();
////		request.put("grant_type", "account_credentials");
////		request.put("account_id", applicationProperties.getZoomAccountId());
//
//		headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes()));
//		headers.add("Content-Type", "application/x-www-form-urlencoded");
//		HttpEntity<?> requestEntity = new HttpEntity<>(zoomGetAuthDTO, headers);
//
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//		messageConverters.add(converter);
//		restTemplate.setMessageConverters(messageConverters);
//
//		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(tokenUrl);
//		ResponseEntity<ZoomTokenDTO> response = restTemplate
//				.exchange(urlBuilder.toUriString(), HttpMethod.POST, requestEntity, ZoomTokenDTO.class);
//		if(response.getStatusCodeValue() == 200) {
//			return response.getBody().getAccess_token();
//		} else if (response.getStatusCodeValue() == 404) {
//			throw new RuntimeException("User id or email not found for supplied value");
//		}
//
//		return null;
//	}

	public synchronized String getAuthenticationToken() throws Exception {
		if (zoomTokenDTO.getAuthorizationToken() == null) {
			fetchAuthorizationToken();
		}
//		if (zoomTokenDTO.getAccessToken() == null || checkIfTokenWillExpire()) {
//			fetchAccessToken();
//		}
//		return this.zoomTokenDTO.getAccessToken();
		return "asd";
	}

	private void fetchAuthorizationToken() throws Exception {
//		https://zoom.us/oauth/authorize?client_id=5YeVQsuQMqgTRUOxh3NKA&response_type=code&redirect_uri=http://localhost:5900/zoom/callback

//		https://zoom.us/oauth/authorize?client_id=5YeVQsuQMqgTRUOxh3NKA&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A5900%2Fzoom%2Fcallback

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://zoom.us/oauth/authorize")
				.queryParam("client_id", "{client_id}")
				.queryParam("response_type", "{response_type}")
				.queryParam("redirect_uri", "{redirect_uri}")
				.encode()
				.toUriString();

		Map<String, String> params = new HashMap<>();
		params.put("client_id", applicationProperties.getZoomApiKey());
		params.put("response_type", "code");
		params.put("redirect_uri", "http://localhost:5900/zoom/callback");

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> response = restTemplate.exchange(
				urlTemplate,
				HttpMethod.GET,
				entity,
				String.class,
				params
		);
		System.out.println(response);

//		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://zoom.us/oauth/authorize")
//				.queryParam("client_id", applicationProperties.getZoomApiKey())
//				.queryParam("response_type", "code")
//				.queryParam("redirect_uri", "http://localhost:5900/zoom/callback");
//
//		RestTemplate restTemplate = new RestTemplate();
//		try {
//			restTemplate.getForEntity(builder.build().toUri(), String.class);
//		} catch (HttpClientErrorException e) {
//			ResponseEntity<String> res = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
//			throw new Exception(
//					(String.format("Unable to get authentication token due to %s. Response code: %d", res.getBody(),
//							res.getStatusCodeValue())));
//		}
	}

	private boolean checkIfTokenWillExpire() {

		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		long differenceInMillis = tokenExpiryTime - now.getTimeInMillis();

		// Token is already expired
		if (differenceInMillis < 0) {
			return true;
		}
		//Token has less than 20 minutes to expire
		if (20 > TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)) {
			return true;
		}

		return false;
	}

	private void fetchAccessToken() throws Exception {
		String credentials = applicationProperties.getZoomApiKey() + ":" + applicationProperties.getZoomApiSecret();
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_FORM_URLENCODED));
		headers.add("Authorization", "Basic " + encodedCredentials);
		headers.add("Content-Type", "application/x-www-form-urlencoded");

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("code", zoomTokenDTO.getAccessToken());
		map.add("grant_type", "authorization_code");
		map.add("redirect_uri", "http://localhost:5900/zoom/callback");
		map.add("code_verifier", "randomString");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		String url = applicationProperties.getZoomIssuer() + "/token";
		RestTemplate restTemplate = new RestTemplate();
		try {
			zoomTokenDTO = restTemplate.exchange(url, HttpMethod.POST, entity,
					ZoomTokenDTO.class).getBody();
		} catch (HttpClientErrorException e) {
			ResponseEntity<String> res = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
			throw new Exception(
					(String.format("Unable to get authentication token due to %s. Response code: %d", res.getBody(),
							res.getStatusCodeValue())));
		}

		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		this.tokenExpiryTime = now.getTimeInMillis() + (zoomTokenDTO.getExpiresIn() - 10) * 1000;
	}

}
