package pw.proj.letsmeet.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pw.proj.letsmeet.config.ApplicationProperties;
import pw.proj.letsmeet.dto.ZoomMeetingSettingsDTO;
import pw.proj.letsmeet.dto.ZoomMeetingsListResponseDTO;
import pw.proj.letsmeet.dto.ZoomMeetingObjectDTO;
import pw.proj.letsmeet.service.ZoomService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Service
public class ZoomServiceImpl implements ZoomService {

//	@Autowired
//	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Override
	public ZoomMeetingObjectDTO createMeeting(ZoomMeetingObjectDTO zoomMeetingObjectDTO) {
		String apiUrl = "https://api.zoom.us/v2/users/" + applicationProperties.getZoomAccountId() + "/meetings";

		// replace with your password or method
		zoomMeetingObjectDTO.setPassword("yourPass");
		// replace email with your email
		zoomMeetingObjectDTO.setHost_email("letsmeetmanagement+test@gmail.com");

		// Optional Settings for host and participant related options
		ZoomMeetingSettingsDTO settingsDTO = new ZoomMeetingSettingsDTO();
		settingsDTO.setJoin_before_host(false);
		settingsDTO.setParticipant_video(true);
		settingsDTO.setHost_video(false);
		settingsDTO.setAuto_recording("cloud");
		settingsDTO.setMute_upon_entry(true);
		zoomMeetingObjectDTO.setSettings(settingsDTO);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + generateZoomJWTToken());
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
		headers.add("Authorization", "Bearer " + generateZoomJWTToken());
		headers.add("content-type", "application/json");
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(listMeetingUrl);
		if(meetingType != null) {
			urlBuilder.queryParam("type", meetingType);
		}
		ResponseEntity<ZoomMeetingsListResponseDTO> response = restTemplate
				.exchange(urlBuilder.toUriString(), HttpMethod.GET, requestEntity, ZoomMeetingsListResponseDTO.class);
		if(response.getStatusCodeValue() == 200) {
			return response.getBody();
		} else if (response.getStatusCodeValue() == 404) {
			throw new RuntimeException("User id or email not found for supplied value");
		}
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
		headers.add("Authorization", "Bearer " + generateZoomJWTToken());
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
	private String generateZoomJWTToken() {
		String id = UUID.randomUUID().toString().replace("-", "");
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		Date creation = new Date(System.currentTimeMillis());
		Date tokenExpiry = new Date(System.currentTimeMillis() + (1000 * 60));

		SecretKey key = new SecretKeySpec(applicationProperties.getZoomApiSecret().getBytes(), signatureAlgorithm.getJcaName());

		return Jwts.builder()
				.setId(id)
				.setIssuer(applicationProperties.getZoomApiKey())
				.setIssuedAt(creation)
				.setSubject("")
				.setExpiration(tokenExpiry)
				.signWith(signatureAlgorithm, key)
				.compact();
	}

}
