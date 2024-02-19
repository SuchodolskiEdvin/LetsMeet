package pw.proj.letsmeet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pw.proj.letsmeet.config.ApplicationProperties;
import pw.proj.letsmeet.dto.*;
import pw.proj.letsmeet.service.ZoomService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

@Service
public class ZoomServiceImpl implements ZoomService {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ZoomTokenDTO zoomTokenDTO;

	@Override
	public ZoomMeetingObjectDTO createMeeting(MeetDTO meetDTO) {
		// Map Meet to ZoomMettingObject
		ZoomMeetingObjectDTO zoomMeetingObjectDTO = new ZoomMeetingObjectDTO();
		zoomMeetingObjectDTO.setHost_email("letsmeetmanagement@gmail.com");
		zoomMeetingObjectDTO.setTopic(meetDTO.getName());

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.appendPattern("yyyy-MM-dd h:mma")
				.toFormatter(Locale.ENGLISH);
		LocalDateTime dateTime = LocalDateTime.parse(meetDTO.getDate() + " " + meetDTO.getTimeStart(), formatter);
		zoomMeetingObjectDTO.setStart_time(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		String[] durationParts = meetDTO.getDuration().split(":");
		zoomMeetingObjectDTO.setDuration(Integer.parseInt(durationParts[0]) * 60 + Integer.parseInt(durationParts[1]));
		zoomMeetingObjectDTO.setTimezone("Europe/Warsaw");

		// Optional Settings for host and participant related options
		ZoomMeetingSettingsDTO settingsDTO = new ZoomMeetingSettingsDTO();
		settingsDTO.setJoin_before_host(false);
		settingsDTO.setParticipant_video(true);
		settingsDTO.setHost_video(false);
		settingsDTO.setAuto_recording("cloud");
		settingsDTO.setMute_upon_entry(true);
		zoomMeetingObjectDTO.setSettings(settingsDTO);

		String apiUrl = "https://api.zoom.us/v2/users/me/meetings";
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
		if (zEntity.getStatusCodeValue() == 201) {
			return zEntity.getBody();
		} else {
			System.out.println("Error while creating zoom meeting {}" + zEntity.getStatusCode());
			return null;
		}
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
		if (userIdOrEmail != null) {
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

	public synchronized String getAuthenticationToken() throws Exception {
		if (zoomTokenDTO.getAccessToken() == null || checkTokenExpiration()) {
			fetchAccessToken();
		}
		return this.zoomTokenDTO.getAccessToken();
	}

	private boolean checkTokenExpiration() {
		return zoomTokenDTO.getExpirationDate() != null && zoomTokenDTO.getExpirationDate().before(new Date(System.currentTimeMillis()));
	}

	private void fetchAccessToken() throws Exception {
		try {
			// Specify the path to your shell script
			String scriptPath = System.getProperty("user.dir") + "/backend/etc/scripts/zoom_get_access_token.sh";

			// Execute the shell script
			Process process = new ProcessBuilder(scriptPath).start();

			// Wait for the process to complete
			int exitCode = process.waitFor();

			// Check if the script executed successfully (exit code 0)
			if (exitCode == 0) {
				// Read the script's output
				String scriptOutput = new String(process.getInputStream().readAllBytes());

				// Map the JSON response to a java object
				ObjectMapper mapper = new ObjectMapper();
				zoomTokenDTO = mapper.readValue(scriptOutput, ZoomTokenDTO.class);
				// Token wygaśnie za 55 minut
				zoomTokenDTO.setExpirationDate(new Date(System.currentTimeMillis() + (1000 * 60 * 55)));
			} else {
				System.err.println("Script execution failed with exit code: " + exitCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
